/*
 * Created on 02/03/2008
 */
package com.andrewswan.powergrid.domain.impl.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.andrewswan.powergrid.Utils;
import com.andrewswan.powergrid.domain.Game;
import com.andrewswan.powergrid.domain.Player;
import com.andrewswan.powergrid.domain.PlayerFactory;
import com.andrewswan.powergrid.domain.PlayerStrategy;
import com.andrewswan.powergrid.domain.Player.Colour;
import com.andrewswan.powergrid.domain.impl.player.strategy.DoNothingStrategy;
import com.andrewswan.powergrid.domain.impl.player.strategy.HumanPlayerStrategy;
import com.andrewswan.powergrid.ui.InputDevice;

/**
 * A naive implementation of the {@link PlayerFactory}. Internally this factory
 * uses the GoF "Template Method" pattern. Subclasses of this factory can
 * implement more sophisticated algorithms for obtaining the details of each
 * player by overriding one or more of the protected methods.
 */
public class PlayerFactoryImpl implements PlayerFactory {
  
  // Properties
  private final InputDevice inputDevice;
  
  /**
   * Constructor for this factory itself
   * 
   * @param inputDevice
   */
  public PlayerFactoryImpl(InputDevice inputDevice) {
    this.inputDevice = inputDevice;
  }

  public Set<Player> getPlayers(int numberOfPlayers, Game game) {
    // Obtain a mutable list of all the possible player colours
    final List<Colour> unusedColours =
        new ArrayList<Colour>(Arrays.asList(Colour.values()));
    
    // Keep track of which names have been chosen
    final Collection<String> playerNames = new ArrayList<String>(); 
    
    // Create the players one by one
    final Set<Player> players = new HashSet<Player>();
    for (int i = 0; i < numberOfPlayers; i++) {
      // Pass defensive copies of our collections to the overridable methods
      
      // Get the new player's name
      String name = getName(i, new ArrayList<String>(playerNames));
      if (StringUtils.isBlank(name) || playerNames.contains(name)) {
        throw new IllegalStateException("Unavailable name '" + name + "'");
      }
      
      // Get the new player's colour
      Colour colour = getColour(i, new ArrayList<Colour>(unusedColours));
      if (!unusedColours.remove(colour)) {
        throw new IllegalStateException("Unavailable colour " + colour);
      }
      
      // Get the new player's strategy
      PlayerStrategy strategy = getStrategy(colour, game);
      if (strategy == null) {
        throw new IllegalStateException("No strategy for player " + i);
      }
      
      // Create the player and add them to our array
      players.add(createPlayer(numberOfPlayers, game, name, colour, strategy));
    }
    return players;
  }

  /**
   * Creates a player using the given properties. Subclasses can override this
   * method to instantiate a different Player class.
   * 
   * @param numberOfPlayers
   * @param game can't be <code>null</code>
   * @param name can't be <code>null</code>
   * @param colour can't be <code>null</code>
   * @param strategy can't be <code>null</code>
   * @return the created player
   */
  protected PlayerImpl createPlayer(int numberOfPlayers, Game game, String name,
      Colour colour, PlayerStrategy strategy)
  {
    return new PlayerImpl(name, colour, numberOfPlayers, strategy, game);
  }

  /**
   * Returns the name of the player with the given player number (turn order is
   * randomised before the game starts, so it doesn't matter what order players
   * are in at this point). This implementation simply returns a name like
   * "Player 1". Subclasses can override this method to implement a more
   * sophisticated naming approach (e.g. prompting the user via a UI).
   * 
   * @param playerNumber the player number, zero-indexed
   * @param chosenNames the names that have already been chosen; can't be
   *   <code>null</code>
   * @return a non-blank name that's not already in the given collection
   */
  protected String getName(int playerNumber, Collection<String> chosenNames) {
    if (playerNumber < 0) {
      throw new IllegalArgumentException(
          "Invalid player number " + playerNumber);
    }
    Utils.checkNotNull(chosenNames);
    return String.format("Player %d", playerNumber + 1);
  }

  /**
   * Selects the colour for the given player, out of the given list of unused
   * player colours. This implementation simply choose the first available
   * colour. Subclasses can override this method to implement a more
   * sophisticated algorithm (e.g. allow choice via a UI). 
   * 
   * @param playerNumber the player number, zero-indexed
   * @param unusedColours the colours that are still available to be chosen;
   *   can't be <code>null</code> or empty
   * @return one of the colours in the given list
   */
  protected Colour getColour(int playerNumber, List<Colour> unusedColours) {
    if (playerNumber < 0) {
      throw new IllegalArgumentException(
          "Invalid player number " + playerNumber);
    }
    if (unusedColours == null || unusedColours.isEmpty()) {
      throw new IllegalArgumentException("Invalid colours " + unusedColours);
    }
    // Use the first one
    return unusedColours.get(0);
  }

  /**
   * Returns the strategy to be used by the given player. This implementation
   * returns the {@link DoNothingStrategy}. Subclasses can override this method
   * to provide sensible strategies for each player.
   * 
   * @param colour the player's colour; can't be <code>null</code>
   * @param game the game being played; can't be <code>null</code>
   * @return a non-<code>null</code> strategy
   */
  protected PlayerStrategy getStrategy(Colour colour, Game game) {
    Utils.checkNotNull(colour, game);
    return new HumanPlayerStrategy(colour, game, inputDevice);
//    return new DoNothingStrategy(game);
  }
}
