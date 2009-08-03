/*
 * Created on 07/02/2008
 */
package com.andrewswan.powergrid.domain;

import java.util.List;

import com.andrewswan.powergrid.domain.Game.Step;

import edu.uci.ics.jung.graph.Vertex;

/**
 * A city on the map to which players can connect
 */
public interface City extends Vertex {

  /**
   * The maximum number of connections to a city
   */
  int MAX_CONNECTIONS = 3;

  /**
   * Returns the area in which this city is located
   *
   * @return a non-<code>null</code> area
   */
  Area getArea();

  /**
   * Returns the name by which this city is known to the players, e.g. all three
   * nodes in Paris are called "Paris"
   *
   * @return the non-blank display name
   * @see #getInternalName()
   */
  String getDisplayName();

  /**
   * Returns the unique internal name of this city (for multi-node cities like
   * Paris, these will be something like ParisNW, ParisNE, and ParisS).
   *
   * @return the non-blank internal name
   * @see #getDisplayName()
   */
  String getInternalName();

  /**
   * Returns the players who have connected to this city
   *
   * @return a non-<code>null</code> list in the order they connected; if the
   *   list has three elements, the city is full
   */
  List<Player> getConnectedPlayers();

  /**
   * Indicates whether the given player is connected to this city
   *
   * @param player the player to check; can't be <code>null</code>
   * @return see above
   */
  boolean isConnected(Player player);

  /**
   * Indicates whether the city already has the maximum number of connections
   * allowed in the given step of the game
   *
   * @param step the current step of the game; can't be <code>null</code>
   * @return see above
   */
  boolean isFull(Step step);

  /**
   * Indicates whether this city would allow the given player to connect to it
   *
   * @param player the player to check; can't be <code>null</code>
   * @param step the current step of the game; can't be <code>null</code>
   * @return <code>false</code> if the city is full or the player is already
   *   connected
   */
  boolean canConnect(Player player, Step step);

  /**
   * Connects the given player to this city; the city can't already be full
   *
   * @param player can't be <code>null</code> or one of the players returned by
   *   {@link #getConnectedPlayers()}
   * @param step the current step of the game; can't be <code>null</code>
   * @return the cost in elektro of the connected place in this city (i.e.
   *   doesn't include any link costs)
   */
  int connect(Player player, Step step);

  /**
   * Returns the amount it would cost the given player to connect this city,
   * excluding any link costs
   *
   * @param player the player who is prospectively connecting; can't be
   *   <code>null</code>
   * @param step the current step of the game; can't be <code>null</code>
   * @return the cost in elektro of the connected place in this city (i.e.
   *   doesn't include any link costs), or <code>null</code> if the player can't
   *   connect there (e.g. is already connected or there's no free spots right
   *   now)
   */
  Integer getConnectionCost(Player player, Step step);
}
