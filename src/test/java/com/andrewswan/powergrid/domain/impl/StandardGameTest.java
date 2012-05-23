/*
 * Created on 17/02/2008
 */
package com.andrewswan.powergrid.domain.impl;

import static com.andrewswan.powergrid.domain.impl.StandardGame.CITIES_TO_END_2_PLAYER_GAME;
import static com.andrewswan.powergrid.domain.impl.StandardGame.CITIES_TO_END_3_PLAYER_GAME;
import static com.andrewswan.powergrid.domain.impl.StandardGame.CITIES_TO_END_4_PLAYER_GAME;
import static com.andrewswan.powergrid.domain.impl.StandardGame.CITIES_TO_END_5_PLAYER_GAME;
import static com.andrewswan.powergrid.domain.impl.StandardGame.CITIES_TO_END_6_PLAYER_GAME;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.andrewswan.powergrid.domain.Board;
import com.andrewswan.powergrid.domain.Game;
import com.andrewswan.powergrid.domain.Player;

/**
 * Unit test for the standard {@link Game}
 */
public class StandardGameTest {

  // Fixture
  private StandardGame game;
  @Mock private Board mockBoard;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testCitiesToEndTwoPlayerGame() {
    assertEndCitiesForNumberOfPlayers(CITIES_TO_END_2_PLAYER_GAME, 2);
  }

  @Test
  public void testCitiesToEndThreePlayerGame() {
    assertEndCitiesForNumberOfPlayers(CITIES_TO_END_3_PLAYER_GAME, 3);
  }

  @Test
  public void testCitiesToEndFourPlayerGame() {
    assertEndCitiesForNumberOfPlayers(CITIES_TO_END_4_PLAYER_GAME, 4);
  }

  @Test
  public void testCitiesToEndFivePlayerGame() {
    assertEndCitiesForNumberOfPlayers(CITIES_TO_END_5_PLAYER_GAME, 5);
  }

  @Test
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
      final int expectedNumberOfCities, final int numberOfPlayers)
  {
    // Set up
    game = new StandardGame(mockBoard, numberOfPlayers);
    final Set<Player> players = new HashSet<Player>();
    for (int i = 0; i < numberOfPlayers; i++) {
      players.add(mock(Player.class));
    }
    game.setPlayers(players);

    // Invoke and check
    final int numberOfCitiesToEndGame = game.getNumberOfCitiesToEndGame();

    // Check
    assertEquals(expectedNumberOfCities, numberOfCitiesToEndGame);
  }
}
