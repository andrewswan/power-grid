/*
 * Created on 07/02/2008
 */
package com.andrewswan.powergrid.domain.impl;

import java.util.Comparator;

import com.andrewswan.powergrid.domain.Board;
import com.andrewswan.powergrid.domain.Player;


/**
 * Sorts players into their final placings, i.e. the finish order
 */
public class FinalPlacingsComparator implements Comparator<Player> {

  // Properties
  private final Board board;

  /**
   * Constructor
   *
   * @param board can't be <code>null</code>
   */
  public FinalPlacingsComparator(final Board board) {
    this.board = board;
  }

  public int compare(final Player player1, final Player player2) {
    // First check number of cities powered
    final int citiesPoweredDifference = player2.powerCities() - player1.powerCities();
    if (citiesPoweredDifference != 0) {
      return citiesPoweredDifference;
    }

    // Next check elektros remaining
    final int elektrosDifference = player2.getElektros() - player1.getElektros();
    if (elektrosDifference != 0) {
      return elektrosDifference;
    }

    // Otherwise, check total cities connected
    return board.getConnectedCities(player2).size() -
        board.getConnectedCities(player1).size();
  }
}

