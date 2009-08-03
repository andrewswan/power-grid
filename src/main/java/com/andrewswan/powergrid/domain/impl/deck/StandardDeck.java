/*
 * Created on 08/02/2008
 */
package com.andrewswan.powergrid.domain.impl.deck;

import com.andrewswan.powergrid.domain.Plant;
import com.andrewswan.powergrid.domain.impl.plant.CoalPlant;
import com.andrewswan.powergrid.domain.impl.plant.FusionPlant;
import com.andrewswan.powergrid.domain.impl.plant.GarbagePlant;
import com.andrewswan.powergrid.domain.impl.plant.HybridPlant;
import com.andrewswan.powergrid.domain.impl.plant.NuclearPlant;
import com.andrewswan.powergrid.domain.impl.plant.OilPlant;
import com.andrewswan.powergrid.domain.impl.plant.WindPlant;


/**
 * The standard power plant deck
 */
public class StandardDeck extends AbstractDeck {

  // Constants
  public static final int PLANT_ON_TOP = 13;

  private static final Plant[] PLANTS_IN_DECK = {
    // Coal
    new CoalPlant(15, 2, 3),
    new CoalPlant(20, 3, 5),
    new CoalPlant(25, 2, 5),
    new CoalPlant(31, 3, 6),
    new CoalPlant(36, 3, 7),
    new CoalPlant(42, 2, 6),
    // Fusion
    new FusionPlant(50, 6),
    // Garbage
    new GarbagePlant(14, 2, 2),
    new GarbagePlant(19, 2, 3),
    new GarbagePlant(24, 2, 4),
    new GarbagePlant(30, 3, 6),
    new GarbagePlant(38, 3, 7),
    // Hybrid
    new HybridPlant(12, 2, 2),
    new HybridPlant(21, 2, 4),
    new HybridPlant(29, 1, 4),
    new HybridPlant(46, 3, 7),
    // Nuclear
    new NuclearPlant(11, 1, 2),
    new NuclearPlant(17, 1, 2),
    new NuclearPlant(23, 1, 3),
    new NuclearPlant(28, 1, 4),
    new NuclearPlant(34, 1, 5),
    new NuclearPlant(39, 1, 6),
    // Oil
    new OilPlant(16, 2, 3),
    new OilPlant(26, 2, 5),
    new OilPlant(32, 3, 6),
    new OilPlant(35, 1, 5),
    new OilPlant(40, 2, 6),
    // Wind
    new WindPlant(13, 1),
    new WindPlant(18, 2),
    new WindPlant(22, 2),
    new WindPlant(27, 3),
    new WindPlant(33, 4),
    new WindPlant(37, 4),
    new WindPlant(44, 5)
  };

  /**
   * Constructor
   *
   * @param players the number of players in the game
   */
  public StandardDeck(final int players) {
    super(PLANTS_IN_DECK, PLANT_ON_TOP, players);
  }
}
