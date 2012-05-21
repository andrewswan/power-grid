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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.andrewswan.powergrid.Utils;
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
 * <p>Description: runs the Power Grid simulator in text mode</p>
 * 
 * @author andrews
 */
public final class TextRunner {

	// Constants
	private static final Logger LOGGER = LoggerFactory.getLogger(TextRunner.class);

	private static final int
	    GAMES_TO_PLAY = 1,
	    PLAYERS = 3;

	/**
	 * Runs the Power Grid simulator
	 * 
	 * @param args
	 */
	public static void main(final String[] args) {
		new TextRunner();
	}

	/**
	 * Constructor
	 */
	private TextRunner() {
		OutputDevice outputDevice = null;
		InputDevice inputDevice = null;
		try {
			outputDevice = new StandardOutput();
			inputDevice = new StandardInput(outputDevice);
			final PlayerFactory playerFactory =
			    new PlayerFactoryImpl(inputDevice);
			for (int gameNumber = 1; gameNumber <= GAMES_TO_PLAY; gameNumber++)
			{
				LOGGER.debug(">>>>>>> Game " + gameNumber + ":");
				final Game game =
				    new StandardGame(new USABoard(PLAYERS), PLAYERS);
				LOGGER.debug("Start market: ");
				print(game.getCurrentMarket());
				game.play(playerFactory.getPlayers(PLAYERS, game));
			}
        }
        finally {
            Utils.closeQuietly(inputDevice);
            Utils.closeQuietly(outputDevice);
        }
	}

	/**
	 * Prints out the numbers of the given {@link Plant}s
	 * 
	 * @param plants can't be <code>null</code>
	 */
	private void print(final Plant... plants) {
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
