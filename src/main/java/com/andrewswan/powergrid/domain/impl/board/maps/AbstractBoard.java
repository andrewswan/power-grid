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
import com.andrewswan.powergrid.domain.Game.Step;
import com.andrewswan.powergrid.domain.Link;
import com.andrewswan.powergrid.domain.Player;
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
  protected AbstractBoard(final String name, final int players) {
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
  protected final Area addArea(final String areaName) {
    final Area area = new AreaImpl(areaName);
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
  protected final City addCity(final String cityName, final Area area) {
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
      final String displayName, final String internalName, final Area area)
  {
    Utils.checkNotNull(area);
    areas.add(area);    // might already be there, doesn't matter
    final City city = (City) addVertex(new CityImpl(area, displayName, internalName));
    area.add(city);
    return city;
  }

  protected final void addLink(final City from, final City to, final int cost) {
    Utils.checkNotNull(from, to);
    addEdge(new LinkImpl(from, to, cost));
  }

  public Set<City> getConnectedCities(final Player player) {
    final Set<City> connectedCities = new HashSet<City>();
    for (final Area area : areas) {
      for (final City city : area) {
        if (city.getConnectedPlayers().contains(player)) {
          connectedCities.add(city);
        }
      }
    }
    return connectedCities;
  }

  public void connectCity(final String cityName, final Player player, final Step step) {
    // Get the city to be connected
    final Integer connectionCost = getConnectionCost(cityName, player, step);
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
  public int getTotalLinkCost(final City source, final City destination) {
    Utils.checkNotNull(source, destination);
    if (source.equals(destination)) {
      return 0;
    }
    final Number linkCost = distanceCalculator.getDistance(source, destination);
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
    for (final Object object : getEdges()) {
      final Edge edge = (Edge) object;
      final Pair cities = edge.getEndpoints();
      final City from = (City) cities.getFirst();
      final City to = (City) cities.getSecond();
      final Area fromArea = from.getArea();
      final Area toArea = to.getArea();
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
    final int numberOfAreasInPlay = getNumberOfAreasInPlay();
    if (areas.size() < numberOfAreasInPlay) {
      throw new IllegalStateException(
          "There are only " + areas.size() + " areas on the board. " +
          numberOfAreasInPlay + " are required for " + players + " players");
    }
    do {
      // Randomly choose the required number of areas ...
      final List<Area> unchosenAreas = new ArrayList<Area>(areas);
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
    for (final Area area : areasInPlay) {
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
  protected final Area getArea(final String areaName) {
    for (final Area area : areas) {
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
  protected final City getCity(final String internalName) {
    for (final Object vertex : getVertices()) {
      final City city = (City) vertex;
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

  public Integer getConnectionCost(final String cityName, final Player player, final Step step) {
    // Get the city to be connected
    final City city = getCity(cityName);

    // Get the player's existing cities (if any)
    final Set<City> connectedCities = getConnectedCities(player);

    // Get the base city cost
    final int cityCost = city.getConnectionCost(player, step);

    // Add the cheapest link cost(s) from the player's existing cities (if any)
    int linkCost = 0;   // the link cost of the player's first city
    for (final City source : connectedCities) {
      final int aLinkCost = getTotalLinkCost(source, city);
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
  public boolean equals(final Object object) {
    if (object == this) {
      return true;
    }
    if (!(object instanceof Board)) {
      return false;
    }
    final Board otherBoard = (Board) object;
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
    LinkCostValue() {
      // Empty
    }

    public Number getNumber(final ArchetypeEdge edge) {
      if (!(edge instanceof Link)) {
        throw new UnsupportedOperationException(
            "Unsupported edge type " + edge.getClass());
      }
      final Link link = (Link) edge;
      return link.getCost();
    }

    public void setNumber(final ArchetypeEdge edge, final Number number) {
      throw new UnsupportedOperationException("Link costs are immutable");
    }
  }
}
