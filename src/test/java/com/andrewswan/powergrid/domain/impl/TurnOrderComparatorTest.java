/*
 * Created on 07/02/2008
 */
package com.andrewswan.powergrid.domain.impl;

import static org.easymock.EasyMock.expect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import com.andrewswan.powergrid.EasyMockContainer;
import com.andrewswan.powergrid.domain.Board;
import com.andrewswan.powergrid.domain.City;
import com.andrewswan.powergrid.domain.Player;

/**
 * Unit test of the {@link TurnOrderComparator}
 */
public class TurnOrderComparatorTest extends TestCase {
  
  // Fixture
  private EasyMockContainer mocks;
  private Board mockBoard;
  private Player mockPlayer1;
  private Player mockPlayer2;
  private Set<City> mockCitySet1;
  private Set<City> mockCitySet2;
  private TurnOrderComparator comparator;
  
  @SuppressWarnings("unchecked")
  @Override
  protected void setUp() throws Exception {
    super.setUp();
    mocks = new EasyMockContainer(); 
    mockBoard = mocks.createStrictMock(Board.class);
    comparator = new TurnOrderComparator(mockBoard);
    mockCitySet1 = mocks.createStrictMock(Set.class);
    mockCitySet2 = mocks.createStrictMock(Set.class);
    mockPlayer1 = mocks.createStrictMock(Player.class);
    mockPlayer2 = mocks.createStrictMock(Player.class);
  }

  public void testCompareWithNullFirstPlayer() {
    // Invoke
    try {
      comparator.compare(null, mockPlayer2);
      fail("Shouldn't accept a null first player");
    }
    catch (IllegalArgumentException expected) {
      // Success
    }
  }
  
  public void testCompareWithNullSecondPlayer() {
    // Invoke
    try {
      comparator.compare(mockPlayer1, null);
      fail("Shouldn't accept a null second player");
    }
    catch (IllegalArgumentException expected) {
      // Success
    }
  }
  
  public void testCompareWithDifferentNumbersOfCitiesConnected() {
    // Set up
    expect(mockBoard.getConnectedCities(mockPlayer1))
        .andStubReturn(mockCitySet1);
    expect(mockCitySet1.size()).andStubReturn(3);
    expect(mockBoard.getConnectedCities(mockPlayer2))
        .andStubReturn(mockCitySet2);
    expect(mockCitySet2.size()).andStubReturn(2);
    mocks.replay();
    
    // Invoke
    int result = comparator.compare(mockPlayer1, mockPlayer2);
    
    // Check
    mocks.verify();
    assertTrue(result < 0); // i.e. the player with more cities is "lesser"
  }
  
  public void testCompareWithSameNumbersOfCitiesConnected() {
    // Set up
    // -- Neither player has connected any cities
    setUpHighestPlantsAndSameNumberOfCities(20, 19);
    mocks.replay();
    
    // Invoke
    int result = comparator.compare(mockPlayer1, mockPlayer2);
    
    // Check
    mocks.verify();
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
      Integer player1HighestPlant, Integer player2HighestPlant)
  {
    expect(mockBoard.getConnectedCities(mockPlayer1))
        .andStubReturn(new HashSet<City>());
    expect(mockBoard.getConnectedCities(mockPlayer2))
        .andStubReturn(new HashSet<City>());
    // -- Set up plant numbers
    expect(mockPlayer1.getHighestPlantNumber())
        .andStubReturn(player1HighestPlant);
    expect(mockPlayer2.getHighestPlantNumber())
        .andStubReturn(player2HighestPlant);
  }
  
  /**
   * Uses the comparator to sort an actual list of players
   */
  public void testSortListOfPlayersHavingDifferentNumbersOfCities() {
    // Set up
    expect(mockBoard.getConnectedCities(mockPlayer1))
        .andStubReturn(mockCitySet1);
    expect(mockCitySet1.size()).andStubReturn(3);
    expect(mockBoard.getConnectedCities(mockPlayer2))
        .andStubReturn(mockCitySet2);
    expect(mockCitySet2.size()).andStubReturn(2);
    
    // -- Create the list with the players in the wrong order
    List<Player> players = new ArrayList<Player>();
    players.add(mockPlayer2);
    players.add(mockPlayer1);
    mocks.replay();
    
    // Invoke
    Collections.sort(players, comparator);
    
    // Check
    mocks.verify();
    // -- The players should now be around the right way
    assertSame(mockPlayer1, players.get(0));
    assertSame(mockPlayer2, players.get(1));
  }
  
  public void testFirstPlayerMustOwnAPlant() {
    setUpHighestPlantsAndSameNumberOfCities(null, 10);
    mocks.replay();
    
    // Invoke
    try {
      comparator.compare(mockPlayer1, mockPlayer2);
      fail(
          "Shouldn't be able to compare players if either doesn't own a plant");
    }
    catch (IllegalStateException expected) {
      // Success
      mocks.verify();
    }
  }
  
  public void testSecondPlayerMustOwnAPlant() {
    setUpHighestPlantsAndSameNumberOfCities(11, null);
    mocks.replay();
    
    // Invoke
    try {
      comparator.compare(mockPlayer1, mockPlayer2);
      fail(
          "Shouldn't be able to compare players if either doesn't own a plant");
    }
    catch (IllegalStateException expected) {
      // Success
      mocks.verify();
    }
  }
}
