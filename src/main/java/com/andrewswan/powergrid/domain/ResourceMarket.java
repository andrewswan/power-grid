/*
 * Created on 09/02/2008
 */
package com.andrewswan.powergrid.domain;

import com.andrewswan.powergrid.domain.Game.Step;

/**
 * The market from which {@link Player} buy resources to power their
 * {@link Plant}s.
 */
public interface ResourceMarket {
  
  /**
   * The types of resources that can be bought at a {@link ResourceMarket} 
   */
  public enum Resource {
    COAL,
    GARBAGE,
    OIL,
    URANIUM
  }
  
  /**
   * Returns the current price of the given resource
   * 
   * @param resource the resource whose price is being found; can't be
   *   <code>null</code>
   * @return the cost in elektros (one or more), or <code>null</code> if there's
   *   none for sale
   */
  Integer getPrice(Resource resource);
  
  /**
   * Returns the number of units of the given Resource that can be bought
   * 
   * @param resource can't be <code>null</code>
   * @return zero if it has run out
   */
  int getStock(Resource resource);
  
  /**
   * Returns the given resources to the spare pool for this market, for example
   * after a player uses them to power a plant or sells the plant that they were
   * stored in.
   * 
   * @param resources the resources to return; can be <code>null</code> for none
   */
  void returnAsSpares(ResourcePool resources);
  
  /**
   * Restocks this resource market with the number of resources for the given
   * step of the game (or less if there aren't that many spare resources in the
   * market)
   * 
   * @param step can't be <code>null</code>
   */
  void restock(Step step);

  /**
   * Buys the given resources from this market by spending up to the given
   * amount of money
   * 
   * @param resources the resources to buy; can be <code>null</code> for none
   * @param maxElektros the maximum number of elektros to spend
   * @return the actual resources bought and their cost
   */
  CostedResourcePool buy(ResourcePool resources, int maxElektros);
}
