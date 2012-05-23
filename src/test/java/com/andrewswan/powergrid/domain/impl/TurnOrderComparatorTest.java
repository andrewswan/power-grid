/*
 * Created on 07/02/2008
 */
package com.andrewswan.powergrid.domain.impl;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.andrewswan.powergrid.domain.Board;
import com.andrewswan.powergrid.domain.City;
import com.andrewswan.powergrid.domain.Player;

/**
 * Unit test of the {@link TurnOrderComparator}
 */
public class TurnOrderComparatorTest {

    // Fixture
    @Mock private Board mockBoard;
    @Mock private Player mockPlayer1;
    @Mock private Player mockPlayer2;
    @Mock private Set<City> mockCitySet1;
    @Mock private Set<City> mockCitySet2;
    private TurnOrderComparator comparator;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        comparator = new TurnOrderComparator(mockBoard);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompareWithNullFirstPlayer() {
        comparator.compare(null, mockPlayer2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompareWithNullSecondPlayer() {
        comparator.compare(mockPlayer1, null);
    }

    @Test
    public void testCompareWithDifferentNumbersOfCitiesConnected() {
        // Set up
        when(mockBoard.getConnectedCities(mockPlayer1))
                .thenReturn(mockCitySet1);
        when(mockCitySet1.size()).thenReturn(3);
        when(mockBoard.getConnectedCities(mockPlayer2))
                .thenReturn(mockCitySet2);
        when(mockCitySet2.size()).thenReturn(2);

        // Invoke
        final int result = comparator.compare(mockPlayer1, mockPlayer2);

        // Check
        assertTrue(result < 0); // i.e. the player with more cities is "lesser"
    }

    @Test
    public void testCompareWithSameNumbersOfCitiesConnected() {
        // Set up
        // -- Neither player has connected any cities
        setUpHighestPlantsAndSameNumberOfCities(20, 19);

        // Invoke
        final int result = comparator.compare(mockPlayer1, mockPlayer2);

        // Check
        assertTrue(result < 0); // i.e. the player with higher plant is "lesser"
    }

    /**
     * Sets up the two mock players to have the same numbers of cities and the
     * given highest plant numbers
     * 
     * @param player1HighestPlant can be <code>null</code> for none
     * @param player2HighestPlant can be <code>null</code> for none
     */
    private void setUpHighestPlantsAndSameNumberOfCities(
            final Integer player1HighestPlant, final Integer player2HighestPlant) {
        when(mockBoard.getConnectedCities(mockPlayer1)).thenReturn(
                new HashSet<City>());
        when(mockBoard.getConnectedCities(mockPlayer2)).thenReturn(
                new HashSet<City>());
        // -- Set up plant numbers
        when(mockPlayer1.getHighestPlantNumber()).thenReturn(
                player1HighestPlant);
        when(mockPlayer2.getHighestPlantNumber()).thenReturn(
                player2HighestPlant);
    }

    /**
     * Uses the comparator to sort an actual list of players
     */
    @Test
    public void testSortListOfPlayersHavingDifferentNumbersOfCities() {
        // Set up
        when(mockBoard.getConnectedCities(mockPlayer1))
                .thenReturn(mockCitySet1);
        when(mockCitySet1.size()).thenReturn(3);
        when(mockBoard.getConnectedCities(mockPlayer2))
                .thenReturn(mockCitySet2);
        when(mockCitySet2.size()).thenReturn(2);

        // -- Create the list with the players in the wrong order
        final List<Player> players = new ArrayList<Player>();
        players.add(mockPlayer2);
        players.add(mockPlayer1);

        // Invoke
        Collections.sort(players, comparator);

        // Check
        // -- The players should now be around the right way
        assertSame(mockPlayer1, players.get(0));
        assertSame(mockPlayer2, players.get(1));
    }

    @Test(expected = IllegalStateException.class)
    public void testFirstPlayerMustOwnAPlant() {
        // Set up
        setUpHighestPlantsAndSameNumberOfCities(null, 10);

        // Invoke
        comparator.compare(mockPlayer1, mockPlayer2);
    }

    @Test(expected = IllegalStateException.class)
    public void testSecondPlayerMustOwnAPlant() {
        // Set up
        setUpHighestPlantsAndSameNumberOfCities(11, null);

        // Invoke
        comparator.compare(mockPlayer1, mockPlayer2);
    }
}
