/*
 * Copyright (c) 2007 Business Information Services.
 * Sydney, Australia.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of Business
 * Information Services ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Business Information Services.
 */
package com.andrewswan.powergrid.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * <p>Description: a game of Power Grid</p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Business Information Services</p>
 * 
 * @author andrews
 */
public interface Game extends Serializable {
  
  /**
   * The various steps of the game (each taking some number of turns)
   */
  public enum Step {ONE, TWO, THREE}
  
  /**
   * The various phases of within a turn
   */
  public enum Phase {ONE, TWO, THREE, FOUR, FIVE};
  
  /**
   * Returns the current state of the game map
   * 
   * @return a non-null board
   */
  Board getBoard();

  /**
   * Returns the plants currently available for purchase
   * 
   * @return a non-<code>null</code> array; size will depend mainly on which
   *   "step" the game is in
   */
  Plant[] getCurrentMarket();
  
  /**
   * Plays this game until it's finished
   *  
   * @param players the players in the game; can't be <code>null</code>
   * @return the finishing order of the players; a non-empty list with the first
   *   element being the winner
   */
  List<Player> play(Set<Player> players);
  
  /**
   * Returns the chart showing how much income is earned for powering a given
   * number of cities
   * 
   * @return a non-<code>null</code> chart
   */
  IncomeChart getIncomeChart();
  
  /**
   * Returns the resource market
   * 
   * @return a non-<code>null</code> resource market
   */
  ResourceMarket getResourceMarket();
  
  /**
   * Returns the step that the game is in
   * 
   * @return one of the defined {@link Step}s
   */
  Step getStep();
}
