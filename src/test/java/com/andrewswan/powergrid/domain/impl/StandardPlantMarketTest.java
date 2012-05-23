/*
 * Created on 17/02/2008
 */
package com.andrewswan.powergrid.domain.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.andrewswan.powergrid.domain.Plant;
import com.andrewswan.powergrid.domain.PlantMarket;
import com.andrewswan.powergrid.domain.impl.deck.StandardDeck;

/**
 * Tests the standard {@link PlantMarket}
 */
public class StandardPlantMarketTest {

  // Fixture
  private StandardPlantMarket market;

  @Test
  public void testStartingCurrentMarket() {
    // Set up
    market = new StandardPlantMarket(2);  // number of players irrelevant here

    // Invoke
    final Plant[] startingCurrentMarket = market.getCurrentMarket();

    // Check
    assertNotNull(startingCurrentMarket);
    assertEquals(4, startingCurrentMarket.length);
    for (int i = 0; i < startingCurrentMarket.length; i++) {
      assertEquals(i + 3, startingCurrentMarket[i].getNumber());
    }
  }

  @Test
  public void testStartingFutureMarket() {
    // Set up
    market = new StandardPlantMarket(2);  // number of players irrelevant here

    // Invoke
    final Plant[] startingFutureMarket = market.getFutureMarket();

    // Check
    assertNotNull(startingFutureMarket);
    assertEquals(4, startingFutureMarket.length);
    for (int i = 0; i < startingFutureMarket.length; i++) {
      assertEquals(i + 7, startingFutureMarket[i].getNumber());
    }
  }

  @Test
  public void testThirteenPlantIsNext() {
    // Set up
    market = new StandardPlantMarket(2);  // number of players irrelevant here
    final Plant cheapestPlant = market.getCurrentMarket()[0];

    // Invoke
    market.buyPlant(cheapestPlant);

    // Check
    assertEquals(4, market.getCurrentMarket().length);
    assertEquals(4, market.getFutureMarket().length);
    final Plant highestPlant = market.getFutureMarket()[3];
    assertEquals(StandardDeck.PLANT_ON_TOP, highestPlant.getNumber());
  }

  @Test
  public void testRemoveObsoletePlants() {
    // Set up
    market = new StandardPlantMarket(2);  // number of players irrelevant here

    // Invoke
    final Plant[] removedPlants = market.removeObsoletePlants(4);

    // Check
    assertEquals(2, removedPlants.length);
    assertEquals(3, removedPlants[0].getNumber());
    assertEquals(4, removedPlants[1].getNumber());
  }

  @Test
  public void testCannotBuyPlantInFutureMarket() {
    // Set up
    market = new StandardPlantMarket(2);  // number of players irrelevant here

    // Invoke and Check
    assertCannotBuyPlant(market.getFutureMarket()[0]);
  }

  @Test
  public void testCannotBuyNullPlant() {
    assertCannotBuyPlant(null);
  }

  /**
   * Checks that in a new market, you can't buy the plant at the given index
   *
   * @param index
   */
  private void assertCannotBuyPlant(final Plant plant) {
    // Set up
    market = new StandardPlantMarket(2);  // number of players irrelevant here
    // Invoke
    try {
      market.buyPlant(plant);
      fail("Shouldn't have been able to buy the plant " + plant);
    }
    catch (final IllegalArgumentException expected) {
      // Success
    }
  }
}
