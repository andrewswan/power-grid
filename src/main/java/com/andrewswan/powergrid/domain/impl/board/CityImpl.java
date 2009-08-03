/*
 * Created on 07/02/2008
 */
package com.andrewswan.powergrid.domain.impl.board;

import static com.andrewswan.powergrid.domain.Game.Step.ONE;
import static com.andrewswan.powergrid.domain.Game.Step.THREE;
import static com.andrewswan.powergrid.domain.Game.Step.TWO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.andrewswan.powergrid.Utils;
import com.andrewswan.powergrid.domain.Area;
import com.andrewswan.powergrid.domain.City;
import com.andrewswan.powergrid.domain.Player;
import com.andrewswan.powergrid.domain.Game.Step;

import edu.uci.ics.jung.graph.impl.SparseVertex;

/**
 * The implementation of a {@link City}
 */
public class CityImpl extends SparseVertex implements City {

  // Constants
  private static final int[] CONNECTION_COSTS = {10, 15, 20};
  private static final Map<Step, Integer> CONNECTIONS_PER_STEP;

  static {
    CONNECTIONS_PER_STEP = new HashMap<Step, Integer>();
    // An alternative would be to add a Step#getMaxCityConnections method, but
    // that would be adding City-related concerns to the Step class.
    CONNECTIONS_PER_STEP.put(ONE, 1);
    CONNECTIONS_PER_STEP.put(TWO, 2);
    CONNECTIONS_PER_STEP.put(THREE, 3);
  }

  // Properties
  private final Area area;
  private final String displayName;
  private final String internalName;
  private final List<Player> connectedPlayers;

  /**
   * Constructor for a city with the same internal and display names (always the
   * case except for multi-node cities like Paris)
   *
   * @param area the area in which the city is located; can't be
   *   <code>null</code>
   * @param displayName can't be blank
   */
  public CityImpl(final Area area, final String displayName) {
    this(area, displayName, null);
  }

  /**
   * Constructor for a city with different internal and display names
   *
   * @param area the area in which the city is located; can't be
   *   <code>null</code>
   * @param displayName can't be blank
   * @param internalName if blank, the display name is used
   */
  public CityImpl(
  		final Area area, final String displayName, final String internalName)
  {
    Utils.checkNotNull(area);
    Utils.checkNotBlank(displayName);
    this.area = area;
    this.connectedPlayers = new ArrayList<Player>();
    this.displayName = displayName;
    this.internalName = StringUtils.defaultIfEmpty(internalName, displayName);
  }

  public Area getArea() {
    return area;
  }

  public String getDisplayName() {
    return displayName;
  }

  public String getInternalName() {
    return internalName;
  }

  @Override
  public boolean equals(final Object object) {
    if (object == this) {
      return true;
    }
    if (!(object instanceof City)) {
      return false;
    }
    final City otherCity = (City) object;
    return internalName.equalsIgnoreCase(otherCity.getInternalName());
  }

  @Override
  public int hashCode() {
    return internalName.toLowerCase(Locale.getDefault()).hashCode();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
  }

  public int connect(final Player player, final Step step) {
    if (player == null || connectedPlayers.contains(player)) {
      throw new IllegalArgumentException("Invalid player " + player);
    }
    if (isFull(step)) {
      throw new IllegalStateException("City is full");
    }
    final int cost = getConnectionCost(player, step); // shouldn't be null here
    connectedPlayers.add(player);
    return cost;
  }

  public List<Player> getConnectedPlayers() {
    return new ArrayList<Player>(connectedPlayers); // defensive copy
  }

  public boolean isFull(final Step step) {
    return connectedPlayers.size() >= CONNECTIONS_PER_STEP.get(step);
  }

  public boolean isConnected(final Player player) {
    Utils.checkNotNull(player);
    return connectedPlayers.contains(player);
  }

  public boolean canConnect(final Player player, final Step step) {
    return !isFull(step) && !isConnected(player);
  }

  public Integer getConnectionCost(final Player player, final Step step) {
    if (!canConnect(player, step)) {
      return null;
    }
    return CONNECTION_COSTS[connectedPlayers.size()];
  }
}
