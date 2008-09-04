/*
 * Created on 09/02/2008
 */
package com.andrewswan.powergrid.domain.impl;

import static com.andrewswan.powergrid.domain.ResourceMarket.Resource.COAL;
import static com.andrewswan.powergrid.domain.ResourceMarket.Resource.GARBAGE;
import static com.andrewswan.powergrid.domain.ResourceMarket.Resource.OIL;
import static com.andrewswan.powergrid.domain.ResourceMarket.Resource.URANIUM;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.andrewswan.powergrid.Utils;
import com.andrewswan.powergrid.domain.CostedResourcePool;
import com.andrewswan.powergrid.domain.ResourceMarket;
import com.andrewswan.powergrid.domain.ResourcePool;
import com.andrewswan.powergrid.domain.Game.Step;

/**
 * The standard implementation of a {@link ResourceMarket}
 */
public abstract class AbstractResourceMarket implements ResourceMarket {
  
  // Properties
  protected final ResourcePool resources;       // for sale
  protected final ResourcePool spareResources;  // not for sale
  protected final Map<Step, ResourcePool> restockRates;

  /**
   * Constructor
   * 
   * @param startingResources the resources available for sale at the start of
   *   the game; can't be <code>null</code>
   * @param startingSpareResources the spare resources at the start of the game;
   *   can't be <code>null</code>
   * @param restockRates the rates at which resources are restocked for a given
   *   {@link Step} of the game; can't be <code>null</code>, must have the same
   *   number of entries as there are steps in the game
   */
  protected AbstractResourceMarket(ResourcePool startingResources,
      ResourcePool startingSpareResources, Map<Step, ResourcePool> restockRates)
  {
    Utils.checkNotNull(startingResources, startingSpareResources, restockRates);
    this.resources = startingResources.getCopy();
    this.spareResources = startingSpareResources.getCopy();
    this.restockRates = new HashMap<Step, ResourcePool>();
    if (restockRates.size() != Step.values().length) {
      throw new IllegalArgumentException(
          "Invalid restock rates " + restockRates);
    }
    this.restockRates.putAll(restockRates);
  }

  public Integer getPrice(Resource resource) {
    int stock = getStock(resource);
    if (stock <= 0) {
      // Unknown resource or it has simply run out
      return null;
    }
    return getPrice(resource, stock);
  }

  /**
   * Returns the price of the given Resource when there is the given number of
   * units left in stock
   * 
   * @param resource can't be <code>null</code>
   * @param stock the amount of stock left (one or more)
   * @return the price in Elektros
   */
  protected abstract int getPrice(Resource resource, int stock);
  
  public void returnResources(Map<Resource, Integer> returnedResources) {
    if (returnedResources == null) {
      return;
    }
    for (Entry<Resource, Integer> entry : returnedResources.entrySet()) {
      int returnedSpares = Utils.nullToZero(entry.getValue());
      spareResources.addResource(entry.getKey(), returnedSpares);
    }
  }
  
  public void restock(Step step) {
    Utils.checkNotNull(step);
    ResourcePool stepRestockRates = restockRates.get(step);
    if (stepRestockRates == null) {
      throw new IllegalStateException("Null restock rates for step " + step);
    }
    for (Entry<Resource, Integer> entry : stepRestockRates.getContents()) {
      Resource resource = entry.getKey();
      int restockRate = Utils.nullToZero(entry.getValue());
      if (restockRate < 0) {
        throw new IllegalStateException("Invalid restock rate " + restockRate);
      }
      // Move up to this number of units from the spares to the current market
      // We assume this can never result in too many of any one resource.
      int resourcesToAdd = spareResources.removeResource(resource, restockRate);
      resources.addResource(resource, resourcesToAdd);
    }
  }
  
  public int getStock(Resource resource) {
    Utils.checkNotNull(resource);
    return resources.getQuantity(resource);
  }
  
  public void returnAsSpares(ResourcePool resourcePool) {
    for (Entry<Resource, Integer> entry : resourcePool.getContents()) {
      int quantityToReturn = Utils.nullToZero(entry.getValue());
      spareResources.addResource(entry.getKey(), quantityToReturn);
    }
  }
  
  public CostedResourcePool buy(ResourcePool resourcesToBuy, int maxElektros) {
    if (resourcesToBuy == null || maxElektros <= 0) {
      return new CostedResourcePoolImpl(null, 0);
    }
    ResourcePool resourcesBought = new ResourcePoolImpl();
    int elektrosLeft = maxElektros;
    /*
     * Buy coal, then oil, then garbage, then uranium; this order is fairly
     * arbitrary, but matches the likely order of cost. In any case the player
     * should have given us accurate information about what to buy if money was
     * short.
     */
    for (Resource resource : new Resource[] {COAL, OIL, GARBAGE, URANIUM}) {
      Integer currentPrice = getPrice(resource);
      while (resourcesToBuy.getQuantity(resource) > 0
          && currentPrice != null && currentPrice <= elektrosLeft)
      {
        resources.removeResource(resource, 1);
        resourcesToBuy.removeResource(resource, 1);
        resourcesBought.addResource(resource, 1);
        elektrosLeft -= currentPrice;
        // Get the new price
        currentPrice = getPrice(resource);
      }
    }
    return new CostedResourcePoolImpl(
        resourcesBought, maxElektros - elektrosLeft);
  }
}
