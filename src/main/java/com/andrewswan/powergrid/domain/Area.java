/*
 * Created on 07/02/2008
 */
package com.andrewswan.powergrid.domain;

import java.util.Set;

/**
 * An area on the game map containing a number of cities
 */
public interface Area extends Set<City> {
  
  /**
   * Returns the unique name of this Area within its {@link Board}
   * 
   * @return a non-blank name (could be its map colour)
   */
  String getName();
  
  /**
   * Adds the given area as an adjacent area
   * 
   * @param area can't be <code>null</code> or itself
   */
  void addAdjacentArea(Area area);
  
  /**
   * Returns the areas adjacent to this one (both in and out of play)
   * 
   * @return a non-<code>null</code> set
   */
  Set<Area> getAdjacentAreas();
}
