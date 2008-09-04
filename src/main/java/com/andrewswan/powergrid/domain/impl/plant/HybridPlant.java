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
  public HybridPlant(int minimumPrice, int fuelUsage, int capacity) {
    super(minimumPrice, fuelUsage, PlantType.HYBRID, capacity);
  }
  
  @Override
  protected boolean isUsable(Resource resource) {
    return Resource.COAL.equals(resource) || Resource.OIL.equals(resource);
  }
}
