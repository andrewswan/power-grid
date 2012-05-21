/*
 * Created on 07/02/2008
 */
package com.andrewswan.powergrid.domain.impl;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.andrewswan.powergrid.domain.Board;
import com.andrewswan.powergrid.domain.City;
import com.andrewswan.powergrid.domain.Player;

/**
 * Unit test of the {@link TurnOrderComparator}
 */
public class TurnOrderComparatorTest extends TestCase {

  // Fixture
  @Mock private Board mockBoard;
  @Mock private Player mockPlayer1;
  @Mock private Player mockPlayer2;
  @Mock private Set<City> mockCitySet1;
  @Mock private Set<City> mockCitySet2;
  private TurnOrderComparator comparator;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    MockitoAnnotations.initMocks(this);
    comparator = new TurnOrderComparator(mockBoard);
  }

  public void testCompareWithNullFirstPlayer() {
    // Invoke
    try {
      comparator.compare(null, mockPlayer2);
      fail("Shouldn't accept a null first player");
    }
    catch (final IllegalArgumentException expected) {
      // Success
    }
  }

  public void testCompareWithNullSecondPlayer() {
    // Invoke
    try {
      comparator.compare(mockPlayer1, null);
      fail("Shouldn't accept a null second player");
    }
    catch (final IllegalArgumentException expected) {
      // Success
    }
  }

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
      final Integer player1HighestPlant, final Integer player2HighestPlant)
  {
    when(mockBoard.getConnectedCities(mockPlayer1))
        .thenReturn(new HashSet<City>());
    when(mockBoard.getConnectedCities(mockPlayer2))
        .thenReturn(new HashSet<City>());
    // -- Set up plant numbers
    when(mockPlayer1.getHighestPlantNumber())
        .thenReturn(player1HighestPlant);
    when(mockPlayer2.getHighestPlantNumber())
        .thenReturn(player2HighestPlant);
  }

  /**
   * Uses the comparator to sort an actual list of players
   */
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

  public void testFirstPlayerMustOwnAPlant() {
      // Set up
    setUpHighestPlantsAndSameNumberOfCities(null, 10);

    // Invoke
    try {
      comparator.compare(mockPlayer1, mockPlayer2);
      fail(
          "Shouldn't be able to compare players if either doesn't own a plant");
    }
    catch (final IllegalStateException expected) {
      // Success
    }
  }

  public void testSecondPlayerMustOwnAPlant() {
    setUpHighestPlantsAndSameNumberOfCities(11, null);

    // Invoke
    try {
      comparator.compare(mockPlayer1, mockPlayer2);
      fail(
          "Shouldn't be able to compare players if either doesn't own a plant");
    }
    catch (final IllegalStateException expected) {
      // Success
    }
  }
}
