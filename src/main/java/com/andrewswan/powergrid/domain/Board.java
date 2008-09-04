/*
 * Created on 07/02/2008
 */
package com.andrewswan.powergrid.domain;

import java.util.Set;

import com.andrewswan.powergrid.domain.Game.Step;

import edu.uci.ics.jung.graph.UndirectedGraph;

/**
 * The topological map on which the game is played; does not include the turn
 * order track, the number of connections track, or the resource market. Not
 * called "Map" so as to avoid confusion with {@link java.util.Map}. 
 */
public interface Board extends UndirectedGraph {

  /**
   * Returns the name of this map
   * 
   * @return a non-blank name, e.g. "Germany"
   */
  String getName();
  
  /**
   * Returns the areas of the map that are in play
   * 
   * @return a non-empty set
   */
  Set<Area> getAreasInPlay();
  
  /**
   * Returns the number of elektros that it would cost the given player to
   * connect the given city to their network
   * 
   * @param cityName the internal name of the city to be connected; must be a
   *   valid city internal name
   * @param player the player connecting the city; can't be <code>null</code>
   * @param step the current step of the game; can't be <code>null</code>
   * @return the cost in Elektros, including any link costs, zero if the
   *   player is already connected, or <code>null</code> if the player can't
   *   connect to it for any reason (e.g. no more connections are available
   *   right now)
   */
  Integer getConnectionCost(String cityName, Player player, Step step);
  
  /**
   * Connects the given city to the given player's network
   * 
   * @param cityName the internal name of the city to be connected; must be a
   *   valid city internal name
   * @param player the player connecting the city; can't be <code>null</code>
   * @param step the current step of the game; can't be <code>null</code>
   */
  void connectCity(String cityName, Player player, Step step);
  
  /**
   * Returns the cities to which the given player has connected
   * 
   * @param player can't be <code>null</code>
   * @return a non-<code>null</code> set
   */
  Set<City> getConnectedCities(Player player);

  /**
   * Returns the total link cost between the two given cities
   * 
   * @param source one city; can't be <code>null</code>
   * @param destination the other city; can't be <code>null</code>
   * @return a number zero or more
   */
  int getTotalLinkCost(City source, City destination);
}
