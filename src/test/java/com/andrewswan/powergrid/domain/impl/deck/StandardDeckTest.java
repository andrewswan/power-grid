/*
 * Created on 08/02/2008
 */
package com.andrewswan.powergrid.domain.impl.deck;

import junit.framework.TestCase;

import com.andrewswan.powergrid.domain.Deck;
import com.andrewswan.powergrid.domain.Plant;

/**
 * Tests our implementation of the standard
 * {@link com.andrewswan.powergrid.domain.Deck}
 */
public class StandardDeckTest extends TestCase {

  public void testWithTwoPlayers() throws Exception {
    assertDeckSize(2, 26);
  }

  public void testWithThreePlayers() throws Exception {
    assertDeckSize(3, 26);
  }

  public void testWithFourPlayers() throws Exception {
    assertDeckSize(4, 30);
  }

  public void testWithFivePlayers() throws Exception {
    assertDeckSize(5, 34);
  }

  public void testWithSixPlayers() throws Exception {
    assertDeckSize(6, 34); // 11-40, 42, 44, 46, 50
  }

  private void assertDeckSize(final int players, final int expectedDeckSize)
    throws Exception
  {
    // Invoke
    final Deck deck = new StandardDeck(players);

    // Check
    int actualDeckSize = 0;
    Plant plant = deck.getNextPlant();
    assertNotNull(plant);
    assertEquals(StandardDeck.PLANT_ON_TOP, plant.getNumber());
    while (plant != null) {
      actualDeckSize++;
      plant = deck.getNextPlant();
    }
    assertEquals(expectedDeckSize, actualDeckSize);
  }
}
