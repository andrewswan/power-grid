/*
 * Created on 07/02/2008
 */
package com.andrewswan.powergrid.domain.impl;

import java.util.Comparator;

import com.andrewswan.powergrid.Utils;
import com.andrewswan.powergrid.domain.Board;
import com.andrewswan.powergrid.domain.Player;


/**
 * Sorts players into the correct turn order
 */
public class TurnOrderComparator implements Comparator<Player> {

  // Properties
  private final Board board;
  
  /**
   * Constructor
   * 
   * @param board can't be <code>null</code>
   */
  public TurnOrderComparator(Board board) {
    this.board = board;
  }

  public int compare(Player player1, Player player2) {
    Utils.checkNotNull(player1, player2);
    // First check number of cities connected
    int citiesDifference = board.getConnectedCities(player2).size() -
        board.getConnectedCities(player1).size();
    if (citiesDifference != 0) {
      return citiesDifference;
    }
    
    // Otherwise go by highest plant number
    if (player1.getHighestPlantNumber() == null
        || player2.getHighestPlantNumber() == null)
    {
      throw new IllegalStateException(
          "Can only compare players if both own plants");
    }
    return player2.getHighestPlantNumber() - player1.getHighestPlantNumber();
  }
}
