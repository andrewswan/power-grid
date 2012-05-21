/*
 * Created on 19/03/2008
 */
package com.andrewswan.powergrid.domain.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.andrewswan.powergrid.Utils;
import com.andrewswan.powergrid.domain.ResourceMarket.Resource;
import com.andrewswan.powergrid.domain.ResourcePool;

/**
 * Implementation of a {@link ResourcePool}
 */
public class ResourcePoolImpl implements ResourcePool {

  // Properties
  private final Map<Resource, Integer> resources;

  /**
   * Constructor for an empty resource pool
   */
  public ResourcePoolImpl() {
    this((ResourcePool) null);
  }

  /**
   * Constructor for a resource pool containing the given resources. This is
   * what Joshua Bloch calls a "copy constructor", and is preferable to cloning.
   *
   * @param resources the initial resources; can be <code>null</code> for none
   */
  public ResourcePoolImpl(final Map<Resource, Integer> resources) {
    this.resources = new HashMap<Resource, Integer>();
    if (resources != null) {
      this.resources.putAll(resources);
    }
  }

  /**
   * Constructor for a resource pool containing the given resources. This is
   * what Joshua Bloch calls a "copy constructor", and is preferable to cloning.
   *
   * @param resources the initial resources; can be <code>null</code> for none
   */
  public ResourcePoolImpl(final ResourcePool resources) {
    this.resources = new HashMap<Resource, Integer>();
    if (resources != null) {
      for (final Entry<Resource, Integer> entry : resources.getContents()) {
        addResource(entry.getKey(), entry.getValue());
      }
    }
  }

  public int getQuantity(final Resource resource) {
    final Integer stock = resources.get(resource);
    if (stock == null) {
      return 0;
    }
    return stock;
  }

  public int getTotalQuantity() {
    int totalStock = 0;
    for (final Integer stock : resources.values()) {
      if (stock != null) {
        totalStock += stock;
      }
    }
    return totalStock;
  }

  public int removeResource(final Resource resource, final int quantity) {
    Utils.checkNotNull(resource);
    if (quantity < 0) {
      throw new IllegalArgumentException("Invalid quantity " + quantity);
    }
    final Integer stock = resources.get(resource);
    if (stock == null) {
      return 0;
    }
    final int quantityRemoved = Math.min(quantity, stock);
    resources.put(resource, stock - quantityRemoved);
    return quantityRemoved;
  }

  public void addResource(final Resource resource, final int quantity) {
    Utils.checkNotNull(resource);
    if (quantity < 0) {
      throw new IllegalArgumentException("Invalid quantity " + quantity);
    }
    final int stock = Utils.nullToZero(resources.get(resource));
    resources.put(resource, stock + quantity);
  }

  public ResourcePool getCopy() {
    return new ResourcePoolImpl(resources);
  }

  public Iterable<Entry<Resource, Integer>> getContents() {
    return new HashMap<Resource, Integer>(resources).entrySet();
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof ResourcePool)) {
      return false;
    }
    return EqualsBuilder.reflectionEquals(this, obj);
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this);
  }
}
