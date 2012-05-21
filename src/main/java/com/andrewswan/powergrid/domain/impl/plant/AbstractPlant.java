/*
 * Created on 18/12/2007
 */
package com.andrewswan.powergrid.domain.impl.plant;

import com.andrewswan.powergrid.Utils;
import com.andrewswan.powergrid.domain.Plant;
import com.andrewswan.powergrid.domain.ResourceMarket.Resource;
import com.andrewswan.powergrid.domain.ResourcePool;
import com.andrewswan.powergrid.domain.impl.ResourcePoolImpl;

/**
 * Generic {@link Plant} implementation
 */
public abstract class AbstractPlant implements Plant {

  // Properties
  private final int capacity;   // cities powered
  private final int fuelUsage;
  private final int number;
  private final PlantType type;

  private final ResourcePool fuelStock;

  /**
   * Constructor
   *
   * @param minimumPrice
   * @param fuelUsage
   * @param type
   * @param capacity
   */
  protected AbstractPlant(
      final int minimumPrice, final int fuelUsage, final PlantType type, final int capacity)
  {
    this.capacity = capacity;
    this.fuelStock = new ResourcePoolImpl();
    this.fuelUsage = fuelUsage;
    this.number = minimumPrice;
    this.type = type;
  }

  public boolean addResource(final Resource resource) {
    Utils.checkNotNull(resource);
    if (!isUsable(resource)) {
      return false;
    }
    if (getTotalResources() == getMaximumFuelStock()) {
      // The plant is already full
      return false;
    }
    // Add the resource
    fuelStock.addResource(resource, 1);
    return true;
  }

  /**
   * Returns this plant's stock of the given resource
   *
   * @param resource can't be <code>null</code>
   * @return zero or more
   */
  protected final int getStock(final Resource resource) {
    return fuelStock.getQuantity(resource);
  }

  /**
   * Indicates whether the given type of resource is usable by this plant
   *
   * @param resource can't be <code>null</code>
   * @return see above
   */
  protected abstract boolean isUsable(Resource resource);

  /**
   * Returns the total number of resource units currenly stocked by this plant
   *
   * @return zero or more
   */
  protected final int getTotalResources() {
    return fuelStock.getTotalQuantity();
  }

  public int getCapacity() {
    return capacity;
  }

  public int getCurrentlyPoweredCities() {
    if (getTotalResources() < fuelUsage) {
      // This plant doesn't have enough resources (fuel) to operate
      return 0;
    }
    return capacity;
  }

  public ResourcePool getFuelStock() {
    return fuelStock.getCopy(); // defensive copy
  }

  public int getFuelUsage() {
    return fuelUsage;
  }

  public int getMaximumFuelStock() {
    return fuelUsage * 2;
  }

  public int getMinimumPrice() {
    return number;
  }

  public PlantType getType() {
    return type;
  }

  public int getNumber() {
    return number;
  }

  @Override
  public boolean equals(final Object object) {
    if (object == this) {
      return true;
    }
    if (!(object instanceof Plant)) {
      return false;
    }
    final Plant otherPlant = (Plant) object;
    return number == otherPlant.getNumber();
  }

  @Override
  public int hashCode() {
    return number;
  }

  @Override
  public String toString() {
    return String.valueOf(number);
  }
}
