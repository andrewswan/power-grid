/*
 * Created on 28/03/2008
 */
package com.andrewswan.powergrid.domain;

/**
 * A {@link ResourcePool} that has a cost associated with it
 */
public interface CostedResourcePool extends ResourcePool {

  /**
   * Returns the cost of this resource pool in elektros
   *
   * @return zero or more
   */
  int getCost();
}
