/*
 * Created on 18/12/2007
 */
package com.andrewswan.powergrid.domain.impl.plant;

import com.andrewswan.powergrid.domain.Plant;
import com.andrewswan.powergrid.domain.ResourceMarket.Resource;

/**
 * An oil-fired {@link Plant}
 */
public class OilPlant extends AbstractPlant {

  /**
   * Constructor
   *
   * @param minimumPrice
   * @param fuelUsage
   * @param capacity
   */
  public OilPlant(final int minimumPrice, final int fuelUsage, final int capacity) {
    super(minimumPrice, fuelUsage, PlantType.OIL, capacity);
  }

  @Override
  protected boolean isUsable(final Resource resource) {
    return Resource.OIL.equals(resource);
  }
}
