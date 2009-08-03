/*
 * Created on 10/02/2008
 */
package com.andrewswan.powergrid.domain;

import java.util.Map.Entry;

import com.andrewswan.powergrid.domain.ResourceMarket.Resource;

/**
 * A pool of {@link Resource}s, possibly of different types
 */
public interface ResourcePool {

  /**
   * Adds the given quantity of the given resource to this resource pool
   *
   * @param resource the type of resource to add; can't be <code>null</code>
   * @param quantity the number to add; can't be negative
   */
  void addResource(Resource resource, int quantity);

  /**
   * Returns the quantity of the given type of resources in the pool
   *
   * @param resource can't be <code>null</code>
   * @return zero or more
   */
  int getQuantity(Resource resource);

  /**
   * Returns the total quantity of {@link Resource}s in the pool
   *
   * @return zero or more
   */
  int getTotalQuantity();

  /**
   * Removes up to the given quantity of the given resource from this pool,
   * until that resource runs out
   *
   * @param resource the type of resource to remove; can't be <code>null</code>
   * @param quantity the amount to remove; can't be negative
   * @return the number actually removed
   */
  int removeResource(Resource resource, int quantity);

  /**
   * Returns an iterator over the contents of this pool
   *
   * @return a non-null iterator of a copy of this pool
   */
  Iterable<Entry<Resource, Integer>> getContents();

  /**
   * Returns a copy of this resource pool. Easier than messing with
   * {@link Cloneable}.
   *
   * @return a non-<code>null</code> copy
   */
  ResourcePool getCopy();
}
