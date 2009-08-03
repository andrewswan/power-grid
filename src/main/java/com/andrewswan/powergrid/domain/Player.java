/*
 * Created on 07/02/2008
 */
package com.andrewswan.powergrid.domain;

/**
 * A player in the game of Power Grid
 */
public interface Player {

  /**
   * The possible player colours
   */
  public enum Colour {
    BLACK,
    BLUE,
    GREEN,
    PURPLE,
    RED,
    YELLOW
  }

  /**
   * Returns the name of this player
   *
   * @return a non-blank name
   */
  String getName();

  /**
   * Returns the colour of this player
   *
   * @return a non-<code>null</code> colour
   */
  Colour getColour();

  /**
   * Returns the plants owned by this player
   *
   * @return a non-<code>null</code> array (may be empty)
   */
  Plant[] getPlants();

  /**
   * Returns the highest plant number owned by this player
   *
   * @return <code>null</code> if they own no plants
   */
  Integer getHighestPlantNumber();

  /**
   * Invites the player to bid on the given Plant at auction
   *
   * @param plant the plant being auctioned; can't be <code>null</code>
   * @param minimumBid the minimum bid this player can make
   * @param canPass whether the player is allowed to pass
   * @return <code>null</code> if the player decides to pass, otherwise the
   *   value of their bid
   */
  Integer bidOnPlant(Plant plant, int minimumBid, boolean canPass);

  /**
   * Indicates to this player that they have bought the given Plant at auction
   *
   * @param plant the purchased plant; can't be <code>null</code>
   * @param price the price to be paid by the player
   */
  void buyPlant(Plant plant, int price);

  /**
   * Invites the player to buy resources from the resource market
   */
  void buyResources();

  /**
   * Invites the player to connect new cities
   */
  void connectCities();

  /**
   * Returns the number of elektros (dollars) held by the given player (this may
   * be required to work out the winner)
   *
   * @return zero or more
   */
  int getElektros();

  /**
   * Invites the player to power their desired number of cities (out of the
   * number they can actually power)
   *
   * @return the number of cities actually powered
   */
  int powerCities();

  /**
   * Invites the player to select a plant to be auctioned
   *
   * @param mandatory <code>true</code> if the player has to select a plant;
   *   <code>false</code> if the player can choose not to
   * @return <code>null</code> if the player chooses not to
   */
  Plant selectPlantForAuction(boolean mandatory);
}
