/*
 * Created on 29/03/2008
 */
package com.andrewswan.powergrid.services;

import com.andrewswan.powergrid.domain.Game;


/**
 * Provides non-domain services relating to {@link Game}s
 */
public interface GameService {

  /**
   * Loads the game that's saved in the specified file
   *
   * @param directory the directory containing the save file; must be an
   *   existing directory
   * @param fileName the name of the save file; must be a readable file
   * @return the loaded game
   */
  Game load(String directory, String fileName);

  /**
   * Saves the given game to the specified file
   *
   * @param game
   * @param directory
   * @param fileName
   */
  void save(Game game, String directory, String fileName);
}
