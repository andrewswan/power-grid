/*
 * Created on 18/12/2007
 */
package com.andrewswan.powergrid.domain;

import com.andrewswan.powergrid.domain.Game.Step;

/**
 * The market where {@link Plant}s are bought
 */
public interface PlantMarket {

  /**
   * Returns the {@link Plant}s that can be bought now
   *
   * @return a non-<code>null</code> array (very rarely empty)
   */
  Plant[] getCurrentMarket();

  /**
   * Returns the {@link Plant}s in the future market, i.e. may soon enter the
   * current market
   *
   * @return a non-<code>null</code> array (empty if in step three)
   */
  Plant[] getFutureMarket();

  /**
   * Buys the given plant in the current market (in Phase 2)
   *
   * @param plant the plant to buy; must be in the current market
   */
  void buyPlant(Plant plant);

  /**
   * Tells this plant market that no plants were bought in the auction phase
   * (Phase 2)
   */
  void noPlantsBought();

  /**
   * Removes any plants from the market that have been made obsolete by a player
   * connecting the given number of cities (in Phase 4)
   *
   * @param citiesConnected the number of cities connected
   * @return the plants made obsolete (can be empty but not <code>null</code>)
   */
  Plant[] removeObsoletePlants(int citiesConnected);

  /**
   * Signals the market that a game turn has ended (in other words, Phase 5
   * Bueaucracy is taking place)
   *
   * @param step the current step of the game; can't be <code>null</code>
   */
  void endTurn(Step step);
}
