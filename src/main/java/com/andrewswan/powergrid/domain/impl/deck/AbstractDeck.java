/*
 * Created on 08/02/2008
 */
package com.andrewswan.powergrid.domain.impl.deck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.andrewswan.powergrid.Utils;
import com.andrewswan.powergrid.domain.Deck;
import com.andrewswan.powergrid.domain.Plant;
import com.andrewswan.powergrid.domain.exceptions.StepThreeStartingException;


/**
 * Base implementation of a {@link Deck}
 */
public abstract class AbstractDeck implements Deck {

  // Constants
  protected static final Log LOGGER = LogFactory.getLog(Deck.class);

  // Properties
  private final List<Plant> drawPile;
  private final Set<Plant> stepThreePlants;

  /**
   * Constructor
   *
   * @param plantsInDeck the plants that start the game in the deck (before any
   *   are removed based on the number of players); this array can't be empty
   * @param plantNumberOnTop the plant number of the plant to be put on top of
   *   the deck; must match one of the given plants
   * @param players the number of people playing
   */
  protected AbstractDeck(
      final Plant[] plantsInDeck, final int plantNumberOnTop, final int players)
  {
    this.drawPile = new ArrayList<Plant>();
    this.stepThreePlants = new HashSet<Plant>();
    Plant plantOnTop = null;
    for (final Plant plant : plantsInDeck) {
      if (plant.getMinimumPrice() == plantNumberOnTop) {
        plantOnTop = plant;
      }
      else {
        drawPile.add(plant);
      }
    }
    if (plantOnTop == null) {
      throw new IllegalArgumentException(
          "Plant number " + plantNumberOnTop + " was not among those given");
    }
    Collections.shuffle(drawPile);
    final int plantsToRemove = getPlantsToRemove(players);
    for (int i = 0; i < plantsToRemove; i++) {
      try {
        drawPile.remove(0);
      }
      catch (final IndexOutOfBoundsException ex) {
        throw new IllegalArgumentException("There must be at least " +
            plantsToRemove + " plants in the deck for " + players + " players");
      }
    }
    drawPile.add(0, plantOnTop);
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("Starting deck: " + drawPile);
    }
  }

  /**
   * Returns the number of plants to remove from the deck before the game
   * starts, based on the given number of players. This implementation returns
   * the numbers from the standard rules.
   *
   * @param players the numer of players in the game
   * @return zero or more
   */
  protected int getPlantsToRemove(final int players) {
    switch (players) {
      case 2:
        return 8;
      case 3:
        return 8;
      case 4:
        return 4;
      case 5:
        return 0;
      case 6:
        return 0;
      default:
        throw new IllegalArgumentException(
            "Invalid number of players: " + players);
    }
  }

  public Plant getNextPlant() throws StepThreeStartingException {
    if (drawPile.isEmpty()) {
      if (stepThreePlants.isEmpty()) {
        return null;    // no plants left in the deck
      }
      // Step three has been triggered
      drawPile.addAll(stepThreePlants);
      stepThreePlants.clear();
      Collections.shuffle(drawPile);
      throw new StepThreeStartingException();
    }
    return drawPile.remove(0);
  }

  public void assignToStepThree(final Plant plant) {
    Utils.checkNotNull(plant);
    stepThreePlants.add(plant);
  }
}
