/*
 * Created on 02/03/2008
 */
package com.andrewswan.powergrid.domain;

/**
 * An individual {@link Player}'s strategy for playing the game of Power Grid.
 * Contains every {@link Player} action that involves decision-making on their
 * part. The design of this interface assumes that implementations will have a
 * reference to the current {@link Game} instance in order to make decisions.
 * This means that singleton dependencies such as the {@link PlantMarket} and
 * the {@link ResourceMarket} do not need to be passed as method parameters.
 */
public interface PlayerStrategy {

  /**
   * Invites the player to select a plant to be auctioned
   *
   * @param mandatory <code>true</code> if the player has to select a plant;
   *   <code>false</code> if the player can choose not to
   * @return <code>null</code> if the player chooses not to
   */
  Plant selectPlantForAuction(boolean mandatory);

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
   * Invites the player to buy resources from the resource market
   *
   * @return the resources the player wants to buy; can be <code>null</code> or
   *   contain more than the quantities of resources actually available
   */
  ResourcePool getResourcesToBuy();

  /**
   * Invites the player to connect (build) new cities on the given game board
   *
   * @return the internal names of the connected cities, in the order they are
   *   to be connected. Any cities the player can't connect for any reason will
   *   be skipped. The returned array can be <code>null</code> for none.
   */
  String[] getCitiesToConnect();

  /**
   * Asks the player which plants they want to operate (in the Bureaucracy
   * phase). Any such plants that can't actually be operated (e.g. not owned or
   * not enough fuel) will be quietly ignored.
   *
   * @return the plant numbers of the plants to power; can be <code>null</code>
   *   or an empty array for none
   */
  int[] getPlantsToOperate();

  /**
   * Invites the player to redistribute resources among the given plants that
   * they own
   *
   * @param plants the plants owned by the player; can't be <code>null</code>
   * @param plantBeingReplaced the plant being replaced from which resources may
   *   be stripped; will be <code>null</code> if no plant is being replaced
   */
  void redistributeResources(Plant[] plants, Plant plantBeingReplaced);
}
