/*
 * Created on 28/03/2008
 */
package com.andrewswan.powergrid.domain.impl;

import com.andrewswan.powergrid.domain.CostedResourcePool;
import com.andrewswan.powergrid.domain.ResourcePool;

/**
 * Implementation of a {@link CostedResourcePool}
 */
public class CostedResourcePoolImpl extends ResourcePoolImpl
  implements CostedResourcePool
{
  // Properties
  private final int cost;

  /**
   * Constructor
   * 
   * @param resources
   * @param cost
   */
  public CostedResourcePoolImpl(ResourcePool resources, int cost) {
    super(resources);
    this.cost = cost;
  }

  public int getCost() {
    return cost;
  }
}
