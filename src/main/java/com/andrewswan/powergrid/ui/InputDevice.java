/*
 * Created on 29/03/2008
 */
package com.andrewswan.powergrid.ui;

import java.io.Closeable;

import com.andrewswan.powergrid.domain.Plant;
import com.andrewswan.powergrid.domain.Player.Colour;
import com.andrewswan.powergrid.domain.PlayerStrategy;
import com.andrewswan.powergrid.domain.ResourcePool;

/**
 * A component that accepts input from one or more humans involved in the game
 */
public interface InputDevice extends Closeable {

  /**
   * Invites the person playing the given colour to bid on the given plant
   *
   * @param colour the colour being played; can't be <code>null</code>
   * @param plant the plant up for auction; can't be <code>null</code>
   * @param minimumBid the minimum amount of any bid
   * @param canPass whether the player is allowed to pass
   * @return the player's bid, or <code>null</code> if passing
   * @see PlayerStrategy#bidOnPlant(Plant, int, boolean)
   */
  Integer bidOnPlant(
      Colour colour, Plant plant, int minimumBid, boolean canPass);

  /**
   * Invites the person playing the given colour to buy resources
   *
   * @param colour the colour being played; can't be <code>null</code>
   * @return <code>null</code> or an empty resource pool to buy none
   * @see PlayerStrategy#getResourcesToBuy()
   */
  ResourcePool getResourcesToBuy(Colour colour);

  /**
   * Invites the person playing the given colour to connect cities
   *
   * @param colour the colour being played; can't be <code>null</code>
   * @return the internal names of the cities to connect (case doesn't matter),
   *   in the desired order of connection; can be <code>null</code> or empty for
   *   none
   * @see PlayerStrategy#getCitiesToConnect()
   */
  String[] getCitiesToConnect(Colour colour);

  /**
   * Asks the person playing the given colour which plants they want to operate
   *
   * @param colour the colour being played; can't be <code>null</code>
   * @return the plant numbers of the plants to power; can be <code>null</code>
   *   or an empty array for none
   * @see PlayerStrategy#getPlantsToOperate()
   */
  int[] getPlantsToOperate(Colour colour);

  /**
   * Asks the person playing the given colour which plant they want to put up
   * for auction
   *
   * @param colour the colour being played; can't be <code>null</code>
   * @param currentMarket the plants from which the player can choose; can be
   *   <code>null</code> for none, any <code>null</code> elements are ignored
   * @param mandatory whether the player must put a plant for auction
   * @return the number of the nominated plant, or <code>null</code> for none
   */
  Integer selectPlantForAuction(
      Colour colour, Plant[] currentMarket, boolean mandatory);
}
