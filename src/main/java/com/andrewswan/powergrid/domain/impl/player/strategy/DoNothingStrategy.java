/*
 * Created on 02/03/2008
 */
package com.andrewswan.powergrid.domain.impl.player.strategy;

import com.andrewswan.powergrid.Utils;
import com.andrewswan.powergrid.domain.Game;
import com.andrewswan.powergrid.domain.Plant;
import com.andrewswan.powergrid.domain.PlayerStrategy;
import com.andrewswan.powergrid.domain.ResourcePool;

/**
 * A player strategy that involves them doing nothing (useful only for testing).
 */
public class DoNothingStrategy implements PlayerStrategy {
  
  // Properties
  private final Game game;

  /**
   * Constructor
   * 
   * @param game the game being played; can't be <code>null</code>
   */
  public DoNothingStrategy(Game game) {
    Utils.checkNotNull(game);
    this.game = game;
  }

  public Integer bidOnPlant(Plant plant, int minimumBid, boolean canPass) {
    // Never bid on anything if you don't have to
    if (canPass) {
      return null;
    }
    return minimumBid;
  }

  public ResourcePool getResourcesToBuy() {
    // Never buy any resources
    return null;
  }

  public String[] getCitiesToConnect() {
    // Never connect any cities
    return new String[0];
  }

  public int[] getPlantsToOperate() {
    // Never power any cities
    return new int[0];
  }

  public Plant selectPlantForAuction(boolean mandatory) {
    // Never put a plant up for auction unless required to
    if (!mandatory) {
      return null;
    }
    // Put up the cheapest plant
    return game.getCurrentMarket()[0];
  }

  public void redistributeResources(Plant[] plants, Plant plantBeingReplaced) {
    // Do nothing
  }
}
