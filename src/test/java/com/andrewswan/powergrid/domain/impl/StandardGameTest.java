/*
 * Created on 17/02/2008
 */
package com.andrewswan.powergrid.domain.impl;

import static com.andrewswan.powergrid.domain.impl.StandardGame.CITIES_TO_END_2_PLAYER_GAME;
import static com.andrewswan.powergrid.domain.impl.StandardGame.CITIES_TO_END_3_PLAYER_GAME;
import static com.andrewswan.powergrid.domain.impl.StandardGame.CITIES_TO_END_4_PLAYER_GAME;
import static com.andrewswan.powergrid.domain.impl.StandardGame.CITIES_TO_END_5_PLAYER_GAME;
import static com.andrewswan.powergrid.domain.impl.StandardGame.CITIES_TO_END_6_PLAYER_GAME;

import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

import com.andrewswan.powergrid.EasyMockContainer;
import com.andrewswan.powergrid.domain.Board;
import com.andrewswan.powergrid.domain.Game;
import com.andrewswan.powergrid.domain.Player;

/**
 * Unit test for the standard {@link Game}
 */
public class StandardGameTest extends TestCase {

  // Fixture
  private EasyMockContainer mocks;
  private StandardGame game;
  private Board mockBoard;
  
  @Override
  protected void setUp() throws Exception {
    super.setUp();
    mocks = new EasyMockContainer();
    mockBoard = mocks.createStrictMock(Board.class);
  }
  
  public void testCitiesToEndTwoPlayerGame() {
    assertEndCitiesForNumberOfPlayers(CITIES_TO_END_2_PLAYER_GAME, 2);
  }

  public void testCitiesToEndThreePlayerGame() {
    assertEndCitiesForNumberOfPlayers(CITIES_TO_END_3_PLAYER_GAME, 3);
  }

  public void testCitiesToEndFourPlayerGame() {
    assertEndCitiesForNumberOfPlayers(CITIES_TO_END_4_PLAYER_GAME, 4);
  }

  public void testCitiesToEndFivePlayerGame() {
    assertEndCitiesForNumberOfPlayers(CITIES_TO_END_5_PLAYER_GAME, 5);
  }

  public void testCitiesToEndSixPlayerGame() {
    assertEndCitiesForNumberOfPlayers(CITIES_TO_END_6_PLAYER_GAME, 6);
  }

  /**
   * Checks that building the given number of cities will end the game when the
   * given number of people are playing
   * 
   * @param expectedNumberOfCities
   * @param numberOfPlayers
   */
  private void assertEndCitiesForNumberOfPlayers(
      int expectedNumberOfCities, int numberOfPlayers)
  {
    // Set up
    game = new StandardGame(mockBoard, numberOfPlayers);
    Set<Player> players = new HashSet<Player>();
    for (int i = 0; i < numberOfPlayers; i++) {
      players.add(mocks.createStrictMock(Player.class));
    }
    game.setPlayers(players);
    mocks.replay();
    
    // Invoke and check
    int numberOfCitiesToEndGame = game.getNumberOfCitiesToEndGame();
    
    // Check
    mocks.verify();
    assertEquals(expectedNumberOfCities, numberOfCitiesToEndGame);
  }
}
