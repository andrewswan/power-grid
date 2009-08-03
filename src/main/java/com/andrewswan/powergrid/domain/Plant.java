/*
 * Created on 18/12/2007
 */
package com.andrewswan.powergrid.domain;

import com.andrewswan.powergrid.domain.ResourceMarket.Resource;

/**
 * A power plant
 */
public interface Plant {

  /**
   * The types of plant in the game
   *
   * @see Resource
   */
  public enum PlantType {
    COAL,
    ECOLOGICAL, // e.g. wind and fusion
    GARBAGE,
    HYBRID,     // burns coal and/or oil
    NUCLEAR,
    OIL
  }

  /**
   * Adds a unit of the given resource to this plant, e.g. after buying it from
   * the resource market or moving it from another plant
   *
   * @param resource the resource to add; can't be <code>null</code>
   * @return <code>true</code> if the resource was added (e.g. was of the right
   *   type and there was room for it)
   */
  boolean addResource(Resource resource);

  /**
   * Returns the number of cities this plant can power right now, which for
   * plants that require fuel will depend on whether they have enough fuel
   *
   * @return either zero or the plant's maximum capacity (plants can't be run at
   *   below their maximum output)
   */
  int getCurrentlyPoweredCities();

  /**
   * Returns the number of cities this plant can power
   *
   * @return one or more
   */
  int getCapacity();

  /**
   * Returns the minimum auction price for this plant
   *
   * @return zero or more
   */
  int getMinimumPrice();

  /**
   * Returns the unique number of this plant
   *
   * @return see above
   */
  int getNumber();

  /**
   * Returns the number of fuel units currently stored in this plant
   *
   * @return a non-<code>null</code> map of resources to their quantities
   */
  ResourcePool getFuelStock();

  /**
   * Returns the total number of resources that can be stored in this plant at
   * once
   *
   * @return zero or more
   */
  int getMaximumFuelStock();

  /**
   * Returns what type of plant this is
   *
   * @return a non-<code>null</code> plant type
   */
  PlantType getType();

  /**
   * Returns the number of fuel units required to run this plant
   *
   * @return zero or more
   */
  int getFuelUsage();
}
