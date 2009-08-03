/*
 * Created on 18/12/2007
 */
package com.andrewswan.powergrid.domain.impl.plant;

import com.andrewswan.powergrid.domain.Plant;
import com.andrewswan.powergrid.domain.ResourceMarket.Resource;

/**
 * A nuclear-fuelled {@link Plant}
 */
public class NuclearPlant extends AbstractPlant {

  /**
   * Constructor
   *
   * @param minimumPrice
   * @param fuelUsage
   * @param capacity
   */
  public NuclearPlant(final int minimumPrice, final int fuelUsage, final int capacity) {
    super(minimumPrice, fuelUsage, PlantType.NUCLEAR, capacity);
  }

  @Override
  protected boolean isUsable(final Resource resource) {
    return Resource.URANIUM.equals(resource);
  }
}
