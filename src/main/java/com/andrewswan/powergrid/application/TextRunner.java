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
package com.andrewswan.powergrid.application;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.andrewswan.powergrid.domain.Game;
import com.andrewswan.powergrid.domain.Plant;
import com.andrewswan.powergrid.domain.PlayerFactory;
import com.andrewswan.powergrid.domain.impl.StandardGame;
import com.andrewswan.powergrid.domain.impl.board.maps.USABoard;
import com.andrewswan.powergrid.domain.impl.player.PlayerFactoryImpl;
import com.andrewswan.powergrid.ui.InputDevice;
import com.andrewswan.powergrid.ui.OutputDevice;
import com.andrewswan.powergrid.ui.impl.StandardInput;
import com.andrewswan.powergrid.ui.impl.StandardOutput;

/**
 * <p>Description: runs the Power Grid simulator</p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Business Information Services</p>
 * 
 * @author andrews
 */
public class TextRunner {
  
  // Constants
  private static final int GAMES = 1;
  private static final Log LOGGER = LogFactory.getLog(TextRunner.class);

  /**
   * Runs the Power Grid simulator
   * 
   * @param args
   */
  public static void main(String[] args) {
    new TextRunner();
  }
  
  public TextRunner() {
    OutputDevice outputDevice = null;
    InputDevice inputDevice = null;
    try {
      outputDevice = new StandardOutput();
      inputDevice = new StandardInput(outputDevice);
      PlayerFactory playerFactory = new PlayerFactoryImpl(inputDevice);
      for (int gameNumber = 1; gameNumber <= GAMES; gameNumber++) {
        LOGGER.debug(">>>>>>> Game " + gameNumber + ":");
        int players = 3;
        Game game = new StandardGame(new USABoard(players), players);
        LOGGER.debug("Start market: ");
        print(game.getCurrentMarket());
        game.play(playerFactory.getPlayers(players, game));
      }
    }
    finally {
      if (inputDevice != null) {
        inputDevice.close();
      }
      if (outputDevice != null) {
        outputDevice.close();
      }
    }
  }
  
  /**
   * Prints out the numbers of the given array of {@link Plant}s
   * 
   * @param plants can't be <code>null</code>
   */
  private void print(Plant[] plants) {
    for (int i = 0; i < plants.length; i++) {
      System.out.print(plants[i].getNumber());
      if (i == plants.length - 1) {
        // We just printed the last plant
        System.out.println();
      }
      else {
        // More plants to come
        System.out.print(", ");
      }
    }
  }
}
