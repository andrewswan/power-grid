/*
 * Created on 18/12/2007
 */
package com.andrewswan.powergrid.domain.impl.plant;

import com.andrewswan.powergrid.domain.Plant;
import com.andrewswan.powergrid.domain.ResourceMarket.Resource;

/**
 * A {@link Plant} fired by coal and/or oil
 */
public class HybridPlant extends AbstractPlant {

  /**
   * Constructor
   *
   * @param minimumPrice
   * @param fuelUsage
   * @param capacity
   */
  public HybridPlant(final int minimumPrice, final int fuelUsage, final int capacity) {
    super(minimumPrice, fuelUsage, PlantType.HYBRID, capacity);
  }

  @Override
  protected boolean isUsable(final Resource resource) {
    return Resource.COAL.equals(resource) || Resource.OIL.equals(resource);
  }
}
