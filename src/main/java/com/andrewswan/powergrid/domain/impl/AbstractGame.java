/*
 * Created on 19/01/2008
 */
package com.andrewswan.powergrid.domain.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.andrewswan.powergrid.Utils;
import com.andrewswan.powergrid.domain.Board;
import com.andrewswan.powergrid.domain.Game;
import com.andrewswan.powergrid.domain.IncomeChart;
import com.andrewswan.powergrid.domain.Plant;
import com.andrewswan.powergrid.domain.PlantMarket;
import com.andrewswan.powergrid.domain.Player;
import com.andrewswan.powergrid.domain.ResourceMarket;

/**
 * A generic game of Power Grid using pluggable components
 */
public abstract class AbstractGame implements Game {
  
  // Constants
  public static final int MIN_PLAYERS = 2;
  public static final int MAX_PLAYERS = 6;
  
  protected static final Log LOGGER = LogFactory.getLog(Game.class); 
  
  // Required for serialization
  private static final long serialVersionUID = 2248398619578004763L;
  
  // Properties
  protected final List<Player> players;
  private Board board;
  private IncomeChart incomeChart;
  private PlantMarket plantMarket;
  private ResourceMarket resourceMarket;
  private TurnOrderComparator turnOrderComparator;
  
  private int turn;
  private Step step;
  
  /**
   * No-arg constructor required for serialization
   */
  protected AbstractGame() {
    this.players = new ArrayList<Player>();
  }

  /**
   * Constructor for a game using the given components.
   * 
   * @param board can't be <code>null</code>
   * @param incomeChart can't be <code>null</code>
   * @param plantMarket can't be <code>null</code>
   * @param resourceMarket can't be <code>null</code>
   * @param players must contain between {@link #MIN_PLAYERS} and
   *   {@link #MAX_PLAYERS}
   */
  protected AbstractGame(Board board, IncomeChart incomeChart,
      PlantMarket plantMarket, ResourceMarket resourceMarket)
  {
    this();
    Utils.checkNotNull(board, incomeChart, plantMarket, resourceMarket);
    this.board = board;
    this.incomeChart = incomeChart;
    this.plantMarket = plantMarket;
    this.resourceMarket = resourceMarket;
    this.step = Step.ONE;
    this.turn = 0;
    this.turnOrderComparator = new TurnOrderComparator(board);
  }
  
  public List<Player> play(Set<Player> thePlayers) {
    setPlayers(thePlayers);
    boolean gameOver = false;
    do {
      gameOver = oneTurn();
    } while (!gameOver);
    Collections.sort(players, new FinalPlacingsComparator(board));
    return new ArrayList<Player>(players);
  }

  /**
   * Sets the players taking part in this game
   * 
   * @param thePlayers can't be <code>null</code>, must contain between
   *   {@link #MIN_PLAYERS} and {@link #MAX_PLAYERS}
   */
  void setPlayers(Set<Player> thePlayers) {
    Utils.checkNotNull(thePlayers);
    if (thePlayers.size() < MIN_PLAYERS || thePlayers.size() > MAX_PLAYERS) {
      throw new IllegalArgumentException("Invalid players " + thePlayers);
    }
    LOGGER.debug("Starting the game with players " + thePlayers);
    players.addAll(thePlayers);
  }

  /**
   * Executes one turn of the game
   * 
   * @return <code>true</code> if the game is over
   */
  private boolean oneTurn() {
    turn++;
    LOGGER.debug("Starting turn " + turn);
    adjustTurnOrder();  // Phase 1
    auctionPlants();    // Phase 2
    buyResources();     // Phase 3
    connectCities();    // Phase 4
    if (gameOver()) {
      return true;
    }
    bureaucracy();      // Phase 5
    return false;
  }

  /**
   * Adjusts the turn order (Phase 1)
   */
  private void adjustTurnOrder() {
    LOGGER.debug("> Starting phase 1 (Adjust Turn Order) of turn " + turn);
    if (turn == 1) {
      // No plants or connections yet, so it's random
      Collections.shuffle(players);
    }
    else {
      Collections.sort(players, turnOrderComparator);
    }
    LOGGER.debug("Turn order is now " + players);
  }
  
  /**
   * Executes the phase in which plants are auctioned (Phase 2)
   */
  private void auctionPlants() {
    LOGGER.debug("> Starting phase 2 (Auction) of turn " + turn);
    // Keep track of which players have bought plants
    final Set<Player> playersWhoHaveBoughtPlants = new HashSet<Player>();
    // Keep track of which players have declined to put up a plant for auction
    final Set<Player> playersWhoHaveOptedOut = new HashSet<Player>();
    
    // Go through the players in turn order
    for (Player player : players) {
      if (!playersWhoHaveBoughtPlants.contains(player)
          && !playersWhoHaveOptedOut.contains(player))
      {
        // Give the player a chance to put up a plant for auction
        boolean mandatory = isMandatoryToSelectPlantForAuction();
        Plant plant = player.selectPlantForAuction(mandatory);
        if (plant == null) {
          if (mandatory) {
            throw new IllegalStateException("Players must select a plant");
          }
          playersWhoHaveOptedOut.add(player);
        }
        else {
          // Auction the selected plant
          auction(plant, playersWhoHaveBoughtPlants, playersWhoHaveOptedOut,
              player);
        }
      }
    }
    
    // End-of-auction-phase housekeeping
    if (turn == 1) {
      if (playersWhoHaveBoughtPlants.size() != players.size()) {
        players.removeAll(playersWhoHaveBoughtPlants);
        throw new IllegalStateException(
            "These players did not buy a plant: " + players);
      }
      Collections.sort(players, turnOrderComparator);
    }
    else if (playersWhoHaveBoughtPlants.isEmpty()) {
      plantMarket.noPlantsBought();
    }
  }

  /**
   * Indicates whether it's mandatory that each player selects a plant for
   * auction. This implementation returns <code>true</code> if it's the first
   * turn, otherwise <code>false</code>.
   * 
   * @return see above
   */
  protected boolean isMandatoryToSelectPlantForAuction() {
    return turn == 1;
  }

  /**
   * Auctions off the given plant to the players who have not yet bought a plant
   * or opted out of the auction phase
   * 
   * @param plant the plant to auction; can't be <code>null</code>
   * @param playersWhoHaveOptedOut players who have turned down the chance to
   *   put up a plant for auction; can't be <code>null</code>
   * @param playersWhoHaveBoughtPlants players who have bought plants this turn;
   *   can't be <code>null</code>
   * @param firstBidder the player to bid first
   */
  private void auction(Plant plant, Set<Player> playersWhoHaveBoughtPlants,
      Set<Player> playersWhoHaveOptedOut, Player firstBidder)
  {
    Utils.checkNotNull(
        plant, playersWhoHaveBoughtPlants, playersWhoHaveOptedOut);
    Integer highestBid =
        firstBidder.bidOnPlant(plant, plant.getMinimumPrice(), false);
    if (highestBid == null) {
      throw new IllegalStateException("First bid can't be a pass");
    }
    LOGGER.debug(
        firstBidder + " has put up the " + plant + " plant for " + highestBid);
    List<Player> bidders = new ArrayList<Player>(players);
    bidders.removeAll(playersWhoHaveBoughtPlants);
    bidders.removeAll(playersWhoHaveOptedOut);
    int bidderIndex = bidders.indexOf(firstBidder);
    while (bidders.size() > 1) {
      bidderIndex += 1;
      if (bidderIndex >= bidders.size()) {
        bidderIndex = 0;
      }
      Player bidder = bidders.get(bidderIndex);
      int minimumBid = highestBid + 1;
      Integer bid = bidder.bidOnPlant(plant, minimumBid, true);
      if (bid == null) {
        // This bidder passed
        LOGGER.debug(bidder + " passed");
        bidders.remove(bidder);
        bidderIndex -= 1;
      }
      else if (bid <= highestBid) {
        throw new IllegalStateException("Must bid at least " + minimumBid);
      }
      else {
        // It's a higher bid
        LOGGER.debug(bidder + " bids " + bid);
        highestBid = bid;
      }
    }
    Player highestBidder = bidders.get(0);
    highestBidder.buyPlant(plant, highestBid);
    plantMarket.buyPlant(plant);
    LOGGER.debug(highestBidder + " bought it for " + highestBid);
    playersWhoHaveBoughtPlants.add(highestBidder);
  }

  /**
   * Executes the phase in which players buy resources (Phase 3)
   */
  private void buyResources() {
    LOGGER.debug("> Starting phase 3 (Buy Resources) of turn " + turn);
    // Reverse turn order
    for (int i = players.size() - 1; i >= 0; i--) {
      players.get(i).buyResources();
    }
  }
  
  /**
   * Executes the phase in which players connect cities (Phase 4)
   */
  private void connectCities() {
    LOGGER.debug("> Starting phase 4 (Connect Cities) of turn " + turn);
    /*
     * Get the largest number of cities connected by one player, so we can
     * remove any obolsete plants from the market after everyone has finished
     * building. We should actually be doing this after each new connection is
     * made, so that players can see the new state of the market after each
     * plant is removed (esp. knowing that Step 3 is about to start), but that's
     * a nicety we can add later. 
     */
    int maxCityCount = 0;
    for (int i = players.size() - 1; i >= 0; i--) {
      Player player = players.get(i);
      // Invite the player to connect new cities
      player.connectCities();
      // See if this player now has more connections than anyone else 
      int totalCitiesConnected = board.getConnectedCities(player).size();
      if (totalCitiesConnected > maxCityCount) {
        maxCityCount = totalCitiesConnected;
      }
    }
    plantMarket.removeObsoletePlants(maxCityCount);
  }
  
  /**
   * Indicates whether the game-ending condition(s) have been met. This
   * implementation uses the standard rule about how many cities have been
   * connected by any one player.
   * 
   * @return <code>true</code> if the game is over
   */
  protected boolean gameOver() {
    int citiesToEndGame = getNumberOfCitiesToEndGame();
    for (Player player : players) {
      if (board.getConnectedCities(player).size() >= citiesToEndGame) {
        return true;
      }
    }
    return false;
  }
  
  /**
   * Returns the number of cities built by any player that will cause the game
   * to end
   * 
   * @return a number greater than zero
   */
  protected abstract int getNumberOfCitiesToEndGame();

  /**
   * Carries out the end-of-turn housekeeping (Phase 5)
   */
  private void bureaucracy() {
    LOGGER.debug("> Starting phase 5 (Bureaucracy) of turn " + turn);
    // Power up
    for (Player player : players) {
      player.powerCities(); // here we don't care how many they power
    }

    // Restock resource market
    resourceMarket.restock(step);

    // Discard lowest/highest plant
    plantMarket.endTurn(step);
  }
  
  public Plant[] getCurrentMarket() {
    return plantMarket.getCurrentMarket();
  }
  
  public Step getStep() {
    return step;
  }
  
  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
  }

  public ResourceMarket getResourceMarket() {
    return resourceMarket;
  }

  public Board getBoard() {
    return board;
  }

  public IncomeChart getIncomeChart() {
    return incomeChart;
  }
}
