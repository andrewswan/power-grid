/*
 * Created on 07/02/2008
 */
package com.andrewswan.powergrid.domain.impl.board.maps;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.andrewswan.powergrid.Utils;
import com.andrewswan.powergrid.domain.Area;
import com.andrewswan.powergrid.domain.Board;
import com.andrewswan.powergrid.domain.City;
import com.andrewswan.powergrid.domain.Link;
import com.andrewswan.powergrid.domain.Player;
import com.andrewswan.powergrid.domain.Game.Step;
import com.andrewswan.powergrid.domain.impl.board.AreaImpl;
import com.andrewswan.powergrid.domain.impl.board.CityImpl;
import com.andrewswan.powergrid.domain.impl.board.LinkImpl;

import edu.uci.ics.jung.algorithms.shortestpath.DijkstraDistance;
import edu.uci.ics.jung.algorithms.shortestpath.Distance;
import edu.uci.ics.jung.graph.ArchetypeEdge;
import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.decorators.NumberEdgeValue;
import edu.uci.ics.jung.graph.impl.UndirectedSparseGraph;
import edu.uci.ics.jung.utils.Pair;

/**
 * Superclass for specific boards. This implementation delegates its link cost
 * calculations to a Jung {@link Graph}. 
 */
public abstract class AbstractBoard extends UndirectedSparseGraph
  implements Board
{
  // Properties
  // -- There's no harm in subclasses having access to these 
  protected final int players;
  protected final Distance distanceCalculator;
  protected final String name;
  // -- But we keep the collections private to improve encapsulation
  private final Set<Area> areas;
  private final Set<Area> areasInPlay;
  
  /**
   * Constructor for a board with the given name and no areas
   * 
   * @param name can't be blank
   * @param players the number of people playing; this determines how many areas
   *   of the board will be in play
   */
  protected AbstractBoard(String name, int players) {
    if (StringUtils.isBlank(name)) {
      throw new IllegalArgumentException("Invalid name '" + name + "'");
    }
    this.areas = new HashSet<Area>();
    this.areasInPlay = new HashSet<Area>();
    this.distanceCalculator = new DijkstraDistance(this, new LinkCostValue());
    this.name = name;
    this.players = players;
  }
  
  /**
   * Adds an area with the given name to the board
   * 
   * @param areaName can't be blank, must not already be on this board
   * @return the added area
   */
  protected final Area addArea(String areaName) {
    Area area = new AreaImpl(areaName);
    if (!areas.add(area)) {
      throw new IllegalArgumentException(
          "Already an area called '" + areaName + "'");
    }
    return area;
  }
  
  /**
   * Adds a city with the given name to the given area of the board
   * 
   * @param cityName can't be blank
   * @param area can't be <code>null</code>
   * @return the added City
   */
  protected final City addCity(String cityName, Area area) {
    // Use the given name as both the display and internal name
    return addCity(cityName, null, area);
  }
  
  /**
   * Adds a city with the given display and internal names to the given area of
   * the board
   * 
   * @param displayName can't be blank
   * @param internalName if blank, the display name is used as the internal name
   * @param area can't be <code>null</code>
   * @return the added City
   */
  protected final City addCity(
      String displayName, String internalName, Area area)
  {
    Utils.checkNotNull(area);
    areas.add(area);    // might already be there, doesn't matter
    City city = (City) addVertex(new CityImpl(area, displayName, internalName));
    area.add(city);
    return city;
  }
  
  protected final void addLink(City from, City to, int cost) {
    Utils.checkNotNull(from, to);
    addEdge(new LinkImpl(from, to, cost));
  }
  
  public Set<City> getConnectedCities(Player player) {
    Set<City> connectedCities = new HashSet<City>();
    for (Area area : areas) {
      for (City city : area) {
        if (city.getConnectedPlayers().contains(player)) {
          connectedCities.add(city);
        }
      }
    }
    return connectedCities;
  }

  public void connectCity(String cityName, Player player, Step step) {
    // Get the city to be connected
    Integer connectionCost = getConnectionCost(cityName, player, step);
    if (connectionCost != null) {
      getCity(cityName).connect(player, step);
    }
  }

  /**
   * Returns the total link cost between the two given cities
   * 
   * @param source one city; can't be <code>null</code>
   * @param destination the other city; can't be <code>null</code>
   * @return a number zero or more
   */
  public int getTotalLinkCost(City source, City destination) {
    Utils.checkNotNull(source, destination);
    if (source.equals(destination)) {
      return 0;
    }
    Number linkCost = distanceCalculator.getDistance(source, destination);
    if (linkCost == null) {
      throw new IllegalStateException(
          "No route from " + source + " to " + destination);
    }
    return linkCost.intValue();
  }

  public Set<Area> getAreasInPlay() {
    if (areasInPlay.isEmpty()) {
      setAreaAdjacencies();
      chooseAreasInPlay();
    }
    return new HashSet<Area>(areasInPlay);  // defensive copy
  }

  /**
   * Sets up the adjacencies of the known areas to each other 
   */
  void setAreaAdjacencies() {
    for (Object object : getEdges()) {
      Edge edge = (Edge) object;
      Pair cities = edge.getEndpoints();
      City from = (City) cities.getFirst();
      City to = (City) cities.getSecond();
      Area fromArea = from.getArea();
      Area toArea = to.getArea();
      if (!fromArea.equals(toArea)) {
        // Found an inter-area link => these two areas are adjacent
        fromArea.addAdjacentArea(toArea);
        toArea.addAdjacentArea(fromArea);
      }
    }
  }

  /**
   * Chooses which areas of the board will be in play; subclasses can override
   * this method to choose them some other way. This implementation chooses them
   * randomly, ensuring that all selected areas are connected somehow.
   */
  protected void chooseAreasInPlay() {
    if (areas.isEmpty()) {
      throw new IllegalStateException(
          "Have to add areas before choosing which are in play");
    }
    int numberOfAreasInPlay = getNumberOfAreasInPlay();
    if (areas.size() < numberOfAreasInPlay) {
      throw new IllegalStateException(
          "There are only " + areas.size() + " areas on the board. " +
          numberOfAreasInPlay + " are required for " + players + " players");
    }
    do {
      // Randomly choose the required number of areas ...
      List<Area> unchosenAreas = new ArrayList<Area>(areas);
      // ... by shuffling all the areas and getting the first "n"
      Collections.shuffle(unchosenAreas);
      areasInPlay.clear();
      for (int i = 0; i < numberOfAreasInPlay; i++) {
        areasInPlay.add(unchosenAreas.get(i));
      }
    } while (!areasInPlayAreAdjacent());
  }
  
  /**
   * Indicates whether the areas currently selected as being in play are all
   * adjacent to at least one other area in play
   * 
   * @return <code>false</code> if any of those areas is not adjacent to at
   *   least one other
   */
  protected final boolean areasInPlayAreAdjacent() {
    for (Area area : areasInPlay) {
      if (Collections.disjoint(areasInPlay, area.getAdjacentAreas())) {
        // None of this area's adjacent areas are in play
        return false;
      }
    }
    return true;
  }

  /**
   * Returns the area having the given name
   * 
   * @param areaName the area name to look for
   * @return a non-<code>null</code> area
   */
  protected final Area getArea(String areaName) {
    for (Area area : areas) {
      if (area.getName().equalsIgnoreCase(areaName)) {
        return area;
      }
    }
    throw new IllegalArgumentException("Unknown area '" + areaName + "'");
  }

  /**
   * Returns the city having the given internal name
   * 
   * @param internalName the internal name to look for
   * @return a non-<code>null</code> city
   */
  protected final City getCity(String internalName) {
    for (Object vertex : getVertices()) {
      City city = (City) vertex;
      if (city.getInternalName().equalsIgnoreCase(internalName)) {
        return city;
      }
    }
    throw new IllegalArgumentException("Unknown city '" + internalName + "'");
  }

  /**
   * Returns the number of areas of the board that will be in play for the given
   * number of players. Subclasses can override this method to vary the standard
   * Power Grid rule.
   *  
   * @param players
   * @return a number greater than zero
   */
  int getNumberOfAreasInPlay() {
    switch (players) {
      case 2:
        // Falls through
      case 3:
        return 3;
      case 4:
        return 4;
      case 5:
        // Falls through
      case 6:
        return 5;
      default:
        throw new IllegalArgumentException(
            "Invalid number of players " + players);
    }
  }

  public Integer getConnectionCost(String cityName, Player player, Step step) {
    // Get the city to be connected
    City city = getCity(cityName);
    
    // Get the player's existing cities (if any)
    Set<City> connectedCities = getConnectedCities(player);
    
    // Get the base city cost
    int cityCost = city.getConnectionCost(player, step);
    
    // Add the cheapest link cost(s) from the player's existing cities (if any)
    int linkCost = 0;   // the link cost of the player's first city
    for (City source : connectedCities) {
      int aLinkCost = getTotalLinkCost(source, city);
      if (linkCost == 0 || aLinkCost < linkCost) {
        // This link is the first found, or is cheaper than the cheapest so far
        linkCost = aLinkCost;
      }
    }
    return cityCost + linkCost;
  }

  public String getName() {
    return name;
  }
  
  @Override
  public boolean equals(Object object) {
    if (object == this) {
      return true;
    }
    if (!(object instanceof Board)) {
      return false;
    }
    Board otherBoard = (Board) object;
    return name.equalsIgnoreCase(otherBoard.getName());
  }
  
  @Override
  public int hashCode() {
    return name.toLowerCase(Locale.getDefault()).hashCode();
  }
  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
  }

  /**
   * A component of the link cost calculations; defines how to get the weight
   * of a given edge
   */
  static class LinkCostValue implements NumberEdgeValue {
    
    /**
     * Constructor
     */
    private LinkCostValue() {
      // Empty
    }

    public Number getNumber(ArchetypeEdge edge) {
      if (!(edge instanceof Link)) {
        throw new UnsupportedOperationException(
            "Unsupported edge type " + edge.getClass());
      }
      Link link = (Link) edge;
      return link.getCost();
    }

    public void setNumber(ArchetypeEdge edge, Number number) {
      throw new UnsupportedOperationException("Link costs are immutable");
    }
  }
}
