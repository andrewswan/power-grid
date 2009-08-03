/*
 * Created on 09/02/2008
 */
package com.andrewswan.powergrid.domain.impl.player;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.andrewswan.powergrid.Utils;
import com.andrewswan.powergrid.domain.Board;
import com.andrewswan.powergrid.domain.CostedResourcePool;
import com.andrewswan.powergrid.domain.Game;
import com.andrewswan.powergrid.domain.Plant;
import com.andrewswan.powergrid.domain.Player;
import com.andrewswan.powergrid.domain.PlayerStrategy;
import com.andrewswan.powergrid.domain.ResourcePool;
import com.andrewswan.powergrid.domain.Game.Step;

/**
 * Implementation of a {@link Player}
 */
public class PlayerImpl implements Player {

  // Constants
  protected static final Log LOGGER = LogFactory.getLog(Player.class);

  private static final int DEFAULT_MAXIMUM_NUMBER_OF_PLANTS = 3;
  private static final int MAXIMUM_NUMBER_OF_PLANTS_FOR_TWO_PLAYERS = 4;
  private static final int STARTING_ELEKTROS = 50;

  // Properties
  private final Colour colour;
  private final Plant[] plants;
  private final PlayerStrategy strategy;    // the GoF "Strategy" pattern
  private final String name;
  private final Game game;

  private int elektros;

  /**
   * Constructor
   *
   * @param name can't be blank
   * @param colour can't be <code>null</code>
   * @param players the number of players in the game
   * @param strategy this player's strategy; can't be <code>null</code>
   */
  protected PlayerImpl(final String name, final Colour colour, final int players,
      final PlayerStrategy strategy, final Game game)
  {
    if (StringUtils.isBlank(name)) {
      throw new IllegalArgumentException("Invalid name '" + name + "'");
    }
    Utils.checkNotNull(colour, strategy, game);
    this.colour = colour;
    this.elektros = STARTING_ELEKTROS;
    this.game = game;
    this.name = name;
    int maxPlants = DEFAULT_MAXIMUM_NUMBER_OF_PLANTS;
    if (players == 2) {
      maxPlants = MAXIMUM_NUMBER_OF_PLANTS_FOR_TWO_PLAYERS;
    }
    this.plants = new Plant[maxPlants];
    this.strategy = strategy;
  }

  public void buyPlant(final Plant newPlant, final int price) {
    this.elektros -= price;
    for (int i = 0; i < plants.length; i++) {
      if (plants[i] == null) {
        // Found an empty slot
        plants[i] = newPlant;
        return;
      }
    }
    // If we get here, the player aleady has the maximum number of plants
    final int plantIndexToReplace = getPlantIndexToReplace(plants.clone(), newPlant);
    final Plant replacedPlant = plants[plantIndexToReplace];
    // Allow the player to salvage unused resources from this plant
    redistributeResources(plants, replacedPlant);
    plants[plantIndexToReplace] = newPlant;
  }

  /**
   * Selects which of the given plants will be replaced by the given newly
   * bought plant. This implementation replaces the lowest-numbered plant.
   * Subclasses can override this method for a more sophisticated algorithm.
   *
   * @param currentplants the currently owned plants, one of which is to be
   *   replaced (this is a defensive copy to prevent subclasses illegally
   *   modifying the owned plants); can't be <code>null</code> or contain
   *   <code>null</code> elements
   * @param newPlant the newly bought plant; can't be <code>null</code>
   * @return the index of the plant to replace (not the plant number)
   */
  protected int getPlantIndexToReplace(final Plant[] currentplants, final Plant newPlant) {
    Utils.checkNotNull(currentplants, newPlant);
    int lowestPlantIndex = 0;
    int lowestPlantNumber = currentplants[0].getNumber();
    for (int i = 1; i < currentplants.length; i++) {
      final Plant plant = currentplants[i];
      if (plant == null) {
        throw new IllegalStateException(
            "This method should only be called when the plants array is full");
      }
      if (plant.getNumber() < lowestPlantNumber) {
        lowestPlantNumber = plant.getNumber();
        lowestPlantIndex = i;
      }
    }
    return lowestPlantIndex;
  }

  public int getElektros() {
    // Secures this method against other players
    Utils.checkNotInCallStack(Player.class);
    return elektros;
  }

  public Integer getHighestPlantNumber() {
    Integer highestPlantNumber = null;
    for (final Plant plant : plants) {
      if (plant != null) {
        if (highestPlantNumber == null
            || plant.getNumber() > highestPlantNumber)
        {
          highestPlantNumber = plant.getNumber();
        }
      }
    }
    return highestPlantNumber;
  }

  public String getName() {
    return name;
  }

  public Plant[] getPlants() {
    return plants.clone();  // Defensive copy
  }

  @Override
  public boolean equals(final Object object) {
    if (object == this) {
      return true;
    }
    if (!(object instanceof Player)) {
      return false;
    }
    final Player otherPlayer = (Player) object;
    return colour.equals(otherPlayer.getColour());
  }

  @Override
  public int hashCode() {
    return colour.hashCode();
  }

  @Override
  public String toString() {
    return colour.toString();
  }

  public Colour getColour() {
    return colour;
  }

  public Integer bidOnPlant(final Plant plant, final int minimumBid, final boolean canPass) {
    return strategy.bidOnPlant(plant, minimumBid, canPass);
  }

  public void buyResources() {
    final ResourcePool resourcesToBuy = strategy.getResourcesToBuy();
    final CostedResourcePool resourcesBought =
        game.getResourceMarket().buy(resourcesToBuy, elektros);
    elektros -= resourcesBought.getCost();
  }

  public void connectCities() {
    // Get the cities this player wants to connect
    final String[] citiesToConnect = strategy.getCitiesToConnect();

    if (citiesToConnect != null && citiesToConnect.length > 0) {
      // Connect as many of them as the player has money for
      final Board board = game.getBoard();
      // Get the step once, as it can't change within a phase
      final Step step = game.getStep();
      for (final String cityName : citiesToConnect) {
        final Integer cost = board.getConnectionCost(cityName, this, step);
        if (cost != null && cost <= elektros) {
          // The connection is available and the player can afford it
          elektros -= cost;
          board.connectCity(cityName, this, step);
        }
      }
    }
  }

  public int powerCities() {
    final int[] plantsToOperate = strategy.getPlantsToOperate();
    final int citiesPowered = 0;
    if (plantsToOperate != null) {
      for (final int plantNumber : plantsToOperate) {
        // TODO operate plant if owned and fuelled, increment citiesPowered,
        // put burnt fuel back into market
      }
    }
    // Get Elektros
    elektros += game.getIncomeChart().getIncome(citiesPowered);
    return citiesPowered;
  }

  public void redistributeResources(
      final Plant[] currentPlants, final Plant plantBeingReplaced)
  {
    strategy.redistributeResources(currentPlants, plantBeingReplaced);
  }

  private int getNumberOfPowerableCities() {
    redistributeResources(plants, null);
    int powerableCities = 0;
    for (final Plant plant : plants) {
      if (plant != null) {
        powerableCities += plant.getCurrentlyPoweredCities();
      }
    }
    return powerableCities;
  }

  public Plant selectPlantForAuction(final boolean mandatory) {
    return strategy.selectPlantForAuction(mandatory);
  }
}
