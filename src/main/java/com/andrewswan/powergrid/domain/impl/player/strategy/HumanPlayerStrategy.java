/*
 * Created on 29/03/2008
 */
package com.andrewswan.powergrid.domain.impl.player.strategy;

import com.andrewswan.powergrid.Utils;
import com.andrewswan.powergrid.domain.Game;
import com.andrewswan.powergrid.domain.Plant;
import com.andrewswan.powergrid.domain.PlayerStrategy;
import com.andrewswan.powergrid.domain.ResourcePool;
import com.andrewswan.powergrid.domain.Player.Colour;
import com.andrewswan.powergrid.ui.InputDevice;

/**
 * A player strategy that delegates the decision-making to a real person
 */
public class HumanPlayerStrategy implements PlayerStrategy {

  // Properties
  private final Colour colour;
  private final Game game;
  private final InputDevice inputDevice;

  /**
   * Constructor
   *
   * @param colour the colour being played by this person; can't be
   *   <code>null</code>
   * @param game the game being played; can't be <code>null</code>
   * @param inputDevice the device from which the user's inputs are to be read;
   *   can't be <code>null</code>
   */
  public HumanPlayerStrategy(
      final Colour colour, final Game game, final InputDevice inputDevice)
  {
    Utils.checkNotNull(colour, game, inputDevice);
    this.colour = colour;
    this.game = game;
    this.inputDevice = inputDevice;
  }

  public Integer bidOnPlant(final Plant plant, final int minimumBid, final boolean canPass) {
    return inputDevice.bidOnPlant(colour, plant, minimumBid, canPass);
  }

  public ResourcePool getResourcesToBuy() {
    return inputDevice.getResourcesToBuy(colour);
  }

  public String[] getCitiesToConnect() {
    return inputDevice.getCitiesToConnect(colour);
  }

  public int[] getPlantsToOperate() {
    return inputDevice.getPlantsToOperate(colour);
  }

  public void redistributeResources(final Plant[] plants, final Plant plantBeingReplaced) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Not implemented");
  }

  public Plant selectPlantForAuction(final boolean mandatory) {
    final Plant[] currentMarket = game.getCurrentMarket();
    final Integer plantNumber =
        inputDevice.selectPlantForAuction(colour, currentMarket, mandatory);
    if (plantNumber == null) {
      if (mandatory) {
        throw new IllegalStateException("Input device " + inputDevice +
            " allowed the user not to choose a plant when it was mandatory");
      }
      return null;
    }
    for (final Plant plant : currentMarket) {
      if (plant.getNumber() == plantNumber) {
        return plant;
      }
    }
    // If we get here, the plant number is neither null nor valid; re-prompt
    return selectPlantForAuction(mandatory);
  }
}
