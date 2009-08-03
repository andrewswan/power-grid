/*
 * Created on 02/03/2008
 */
package com.andrewswan.powergrid.domain;

import java.util.Set;

import com.andrewswan.powergrid.domain.Player.Colour;

/**
 * Creates {@link Player} instances. Based on the GoF "AbstractFactory" pattern.
 */
public interface PlayerFactory {

  /**
   * Returns the given number of players, each of which is to have a unique name
   * and {@link Colour}.
   *
   * @param numberOfPlayers the number of players to create
   * @param game the game in which they are playing; can't be <code>null</code>
   * @return the requested number of players
   */
  Set<Player> getPlayers(int numberOfPlayers, Game game);
}
