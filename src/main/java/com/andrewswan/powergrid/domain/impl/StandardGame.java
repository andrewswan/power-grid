/*
 * Created on 19/01/2008
 */
package com.andrewswan.powergrid.domain.impl;

import com.andrewswan.powergrid.domain.Board;

/**
 * A game of Power Grid using the standard rules
 */
public class StandardGame extends AbstractGame {

  // Required for serialization
  private static final long serialVersionUID = -3220758203063551851L;

  /**
   * The number of cities that need to be build to end the game, when a given
   * number of people are playing.
   */
  public static final int CITIES_TO_END_2_PLAYER_GAME = 21;
  public static final int CITIES_TO_END_3_PLAYER_GAME = 17;
  public static final int CITIES_TO_END_4_PLAYER_GAME = 17;
  public static final int CITIES_TO_END_5_PLAYER_GAME = 15;
  public static final int CITIES_TO_END_6_PLAYER_GAME = 14;

  /**
   * Constructor for a standard game on the given board
   *
   * @param board the board on which to play; can't be <code>null</code>
   * @param numberOfPlayers must be between {@link #MIN_PLAYERS} and
   *   {@link #MAX_PLAYERS}
   */
  public StandardGame(final Board board, final int numberOfPlayers) {
    super(board, new StandardIncomeChart(),
        new StandardPlantMarket(numberOfPlayers),
        new StandardResourceMarket(numberOfPlayers));
  }

  @Override
  protected int getNumberOfCitiesToEndGame() {
    switch (players.size()) {
      case 2:
        return CITIES_TO_END_2_PLAYER_GAME;
      case 3:
        return CITIES_TO_END_3_PLAYER_GAME;
      case 4:
        return CITIES_TO_END_4_PLAYER_GAME;
      case 5:
        return CITIES_TO_END_5_PLAYER_GAME;
      case 6:
        return CITIES_TO_END_6_PLAYER_GAME;
      default:
        throw new IllegalArgumentException(
            "Invalid number of players " + players);
    }
  }
}
