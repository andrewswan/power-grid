/*
 * Created on 18/12/2007
 */
package com.andrewswan.powergrid.domain.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.andrewswan.powergrid.Utils;
import com.andrewswan.powergrid.domain.Deck;
import com.andrewswan.powergrid.domain.Game.Phase;
import com.andrewswan.powergrid.domain.Game.Step;
import com.andrewswan.powergrid.domain.Plant;
import com.andrewswan.powergrid.domain.PlantMarket;
import com.andrewswan.powergrid.domain.exceptions.StepThreeStartingException;

/**
 * Basic implementation of a {@link PlantMarket}
 */
public abstract class AbstractPlantMarket implements PlantMarket {

  // Constants
  protected static final int
    STEPS_ONE_AND_TWO_CURRENT_MARKET_SIZE = 4,
    INITIAL_MARKET_SIZE = STEPS_ONE_AND_TWO_CURRENT_MARKET_SIZE * 2;

  protected static final Logger LOGGER = LoggerFactory.getLogger(PlantMarket.class);

  // Properties
  private final Deck deck;
  private final SortedSet<Plant> plants;

  private boolean inStepThree;

  /**
   * Constructor
   *
   * @param startingPlants the plants in the initial market (current and
   *   future); can't be <code>null</code>, must contain exactly
   *   {@link #INITIAL_MARKET_SIZE} plants
   * @param deck the other plants in the game; can't be <code>null</code>
   */
  public AbstractPlantMarket(final Set<Plant> startingPlants, final Deck deck) {
    Utils.checkNotNull(startingPlants, deck);
    if (startingPlants.size() != INITIAL_MARKET_SIZE) {
      throw new IllegalArgumentException(
          "Invalid starting plants " + startingPlants);
    }
    this.deck = deck;
    this.plants = new TreeSet<Plant>(new PriceComparator());
    this.plants.addAll(startingPlants);
  }

  public void buyPlant(final Plant plant) {
    Utils.checkNotNull(plant);
    final Collection<Plant> buyablePlants = Arrays.asList(getCurrentMarket());
    if (!buyablePlants.contains(plant)) {
      throw new IllegalArgumentException(
          "Plant not in the current market " + plant);
    }
    removePlant(plant, Phase.TWO);
  }

  /**
   * Removes the plant at the given index, then adds a new plant to the market
   * from the {@link Deck}. The market should automatically re-sort itself,
   * because it's implemented as a SortedSet.
   *
   * @param index
   * @param phase the current phase of the game turn
   * @return the removed plant (non-<code>null</code>)
   */
  private Plant removePlant(final int index, final Phase phase) {
    final Plant plant = getPlant(index);
    removePlant(plant, phase);
    return plant;
  }

  /**
   * Removes the given plant from the market, then adds a new plant from the
   * {@link Deck}. The market should automatically re-sort itself, because it's
   * implemented as a SortedSet.
   *
   * @param plant the plant to remove
   * @param phase the current phase of the turn
   */
  private void removePlant(final Plant plant, final Phase phase) {
    plants.remove(plant);
    Plant nextPlant = null;
    try {
      nextPlant = deck.getNextPlant();
    }
    catch (final StepThreeStartingException ex) {
      // Handling of step three starting depends on what phase we're in
      switch (phase) {
        case TWO:
          // TODO remove lowest plant AT THE END OF THE PHASE!
          break;
        case FOUR:
          // Don't draw another plant, just remove the lowest
          plants.remove(getPlant(0));
          break;
        case FIVE:
          // Don't draw another plant, just remove the lowest
          plants.remove(getPlant(0));
          break;
        default:
          throw new IllegalStateException(
              "Can't remove a plant in phase " + phase);
      }
    }

    if (nextPlant != null) {
      plants.add(nextPlant);
    }
  }

  /**
   * Returns the plant at the given index
   *
   * @param index zero-based
   * @return a non-<code>null</code> Plant
   */
  private Plant getPlant(final int index) {
    return plants.toArray(new Plant[plants.size()])[index];
  }

  public void endTurn(final Step step) {
    if (!plants.isEmpty()) {
      if (Step.THREE.equals(step)) {
        // Remove the lowest plant in the market (if any) from the game
        removePlant(0, Phase.FIVE);
      }
      else {
        // Put the highest plant in the market back under the deck for step 3
        final Plant highestPlant = removePlant(plants.size() - 1, Phase.FIVE);
        deck.assignToStepThree(highestPlant);
      }
    }
  }

  public Plant[] getCurrentMarket() {
    final List<Plant> currentMarket = new ArrayList<Plant>();
    for (final Plant plant : plants) {
      if (currentMarket.size() < getCurrentMarketSize()) {
        currentMarket.add(plant);
      }
      else {
        break;
      }
    }
    return currentMarket.toArray(new Plant[currentMarket.size()]); // a copy
  }

  public Plant[] getFutureMarket() {
    final List<Plant> futureMarket = new ArrayList<Plant>();
    // Convert the whole market to an array
    final Plant[] market = plants.toArray(new Plant[plants.size()]);
    // Copy in the plants not in the current market
    for (int i = getCurrentMarketSize(); i < market.length; i++) {
      futureMarket.add(market[i]);
    }
    return futureMarket.toArray(new Plant[futureMarket.size()]);
  }

  private int getCurrentMarketSize() {
    if (inStepThree) {
      return plants.size();
    }
    return STEPS_ONE_AND_TWO_CURRENT_MARKET_SIZE;
  }

  public void noPlantsBought() {
    if (!plants.isEmpty()) {
      // Remove the lowest-numbered plant from the game
      final Plant removedPlant = removePlant(0, Phase.TWO);
      LOGGER.debug("Removed plant " + removedPlant + " from the game");
    }
  }

  public Plant[] removeObsoletePlants(final int citiesConnected) {
    final List<Plant> removedPlants = new ArrayList<Plant>();
    Plant plant = getPlant(0);
    while (plant.getNumber() <= citiesConnected) {
      removedPlants.add(removePlant(0, Phase.FOUR));
      plant = getPlant(0);
    }
    return removedPlants.toArray(new Plant[removedPlants.size()]);
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
  }
}

/**
 * Compares two {@link Plant}s by price. It's {@link Serializable} so that it
 * can be stored along with the set that it sorts.
 */
class PriceComparator implements Comparator<Plant>, Serializable {

  // Constants
  private static final long serialVersionUID = 5943648789531153414L;

  public int compare(final Plant plant1, final Plant plant2) {
    return plant1.getMinimumPrice() - plant2.getMinimumPrice();
  }
}