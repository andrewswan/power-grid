/*
 * Created on 08/02/2008
 */
package com.andrewswan.powergrid.domain.impl.deck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.andrewswan.powergrid.domain.Deck;
import com.andrewswan.powergrid.domain.Plant;
import com.andrewswan.powergrid.domain.exceptions.StepThreeStartingException;

/**
 * Tests our implementation of the standard
 * {@link com.andrewswan.powergrid.domain.Deck}
 */
public class StandardDeckTest {

    @Test
    public void testWithTwoPlayers() throws Exception {
        assertDeckSize(2, 26);
    }

    @Test
    public void testWithThreePlayers() throws Exception {
        assertDeckSize(3, 26);
    }

    @Test
    public void testWithFourPlayers() throws Exception {
        assertDeckSize(4, 30);
    }

    @Test
    public void testWithFivePlayers() throws Exception {
        assertDeckSize(5, 34);
    }

    @Test
    public void testWithSixPlayers() throws Exception {
        assertDeckSize(6, 34); // 11-40, 42, 44, 46, 50
    }

    private void assertDeckSize(final int players, final int expectedDeckSize)
            throws StepThreeStartingException {
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
