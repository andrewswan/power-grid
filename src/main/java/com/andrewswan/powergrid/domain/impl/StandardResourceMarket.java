/*
 * Created on 10/02/2008
 */
package com.andrewswan.powergrid.domain.impl;

import static com.andrewswan.powergrid.domain.ResourceMarket.Resource.COAL;
import static com.andrewswan.powergrid.domain.ResourceMarket.Resource.GARBAGE;
import static com.andrewswan.powergrid.domain.ResourceMarket.Resource.OIL;
import static com.andrewswan.powergrid.domain.ResourceMarket.Resource.URANIUM;

import java.util.HashMap;
import java.util.Map;

import com.andrewswan.powergrid.domain.Game.Step;
import com.andrewswan.powergrid.domain.ResourceMarket;
import com.andrewswan.powergrid.domain.ResourcePool;

/**
 * The standard {@link ResourceMarket}. Some maps (Italy and France?) use a
 * different resource market
 */
public class StandardResourceMarket extends AbstractResourceMarket {

  // Constants
  public static final int MAXIMUM_NON_URANIUM_PRICE = 8;
  public static final int TOTAL_COAL = 24;
  public static final int TOTAL_GARBAGE = 24;
  public static final int TOTAL_OIL = 24;
  public static final int TOTAL_URANIUM = 12;
  public static final int UNITS_PER_PRICE_BRACKET = 3;
  public static final int STARTING_COAL = 8 * UNITS_PER_PRICE_BRACKET;
  public static final int STARTING_GARBAGE = 2 * UNITS_PER_PRICE_BRACKET;
  public static final int STARTING_OIL = 6 * UNITS_PER_PRICE_BRACKET;
  public static final int STARTING_URANIUM = 2;

  private static final int[] URANIUM_PRICES =
      {16, 14, 12, 10, 8, 7, 6, 5, 4, 3, 2, 1};

  private static final ResourcePool STARTING_RESOURCES;
  static {
    // TODO might be better to push this stuff to the superclass
    STARTING_RESOURCES = new ResourcePoolImpl();
    STARTING_RESOURCES.addResource(Resource.COAL, STARTING_COAL);
    STARTING_RESOURCES.addResource(Resource.OIL, STARTING_OIL);
    STARTING_RESOURCES.addResource(Resource.GARBAGE, STARTING_GARBAGE);
    STARTING_RESOURCES.addResource(Resource.URANIUM, STARTING_URANIUM);
  }

  private static final ResourcePool STARTING_SPARE_RESOURCES;
  static {
    // TODO might be better to push this logic to the superclass (assumes all
    // ResourceMarkets have the same total number of resources)
    STARTING_SPARE_RESOURCES = new ResourcePoolImpl();
    STARTING_SPARE_RESOURCES.addResource(
        Resource.COAL, TOTAL_COAL - STARTING_COAL);
    STARTING_SPARE_RESOURCES.addResource(
        Resource.OIL, TOTAL_OIL - STARTING_OIL);
    STARTING_SPARE_RESOURCES.addResource(
        Resource.GARBAGE, TOTAL_GARBAGE - STARTING_GARBAGE);
    STARTING_SPARE_RESOURCES.addResource(
        Resource.URANIUM, TOTAL_URANIUM - STARTING_URANIUM);
  }

  private static final Map<Step, ResourcePool> TWO_PLAYER_RESTOCKS;
  static {
    final ResourcePool STEP_1 = new ResourcePoolImpl();
    STEP_1.addResource(COAL, 3);
    STEP_1.addResource(OIL, 2);
    STEP_1.addResource(GARBAGE, 1);
    STEP_1.addResource(URANIUM, 1);
    final ResourcePool STEP_2 = new ResourcePoolImpl();
    STEP_2.addResource(COAL, 4);
    STEP_2.addResource(OIL, 2);
    STEP_2.addResource(GARBAGE, 2);
    STEP_2.addResource(URANIUM, 1);
    final ResourcePool STEP_3 = new ResourcePoolImpl();
    STEP_3.addResource(COAL, 3);
    STEP_3.addResource(OIL, 4);
    STEP_3.addResource(GARBAGE, 3);
    STEP_3.addResource(URANIUM, 1);
    TWO_PLAYER_RESTOCKS = new HashMap<Step, ResourcePool>();
    TWO_PLAYER_RESTOCKS.put(Step.ONE, STEP_1);
    TWO_PLAYER_RESTOCKS.put(Step.TWO, STEP_2);
    TWO_PLAYER_RESTOCKS.put(Step.THREE, STEP_3);
  }

  private static final Map<Step, ResourcePool> THREE_PLAYER_RESTOCKS;
  static {
    final ResourcePool STEP_1 = new ResourcePoolImpl();
    STEP_1.addResource(COAL, 4);
    STEP_1.addResource(OIL, 2);
    STEP_1.addResource(GARBAGE, 1);
    STEP_1.addResource(URANIUM, 1);
    final ResourcePool STEP_2 = new ResourcePoolImpl();
    STEP_2.addResource(COAL, 5);
    STEP_2.addResource(OIL, 3);
    STEP_2.addResource(GARBAGE, 2);
    STEP_2.addResource(URANIUM, 1);
    final ResourcePool STEP_3 = new ResourcePoolImpl();
    STEP_3.addResource(COAL, 3);
    STEP_3.addResource(OIL, 4);
    STEP_3.addResource(GARBAGE, 3);
    STEP_3.addResource(URANIUM, 1);
    THREE_PLAYER_RESTOCKS = new HashMap<Step, ResourcePool>();
    THREE_PLAYER_RESTOCKS.put(Step.ONE, STEP_1);
    THREE_PLAYER_RESTOCKS.put(Step.TWO, STEP_2);
    THREE_PLAYER_RESTOCKS.put(Step.THREE, STEP_3);
  }

  private static final Map<Step, ResourcePool> FOUR_PLAYER_RESTOCKS;
  static {
    final ResourcePool STEP_1 = new ResourcePoolImpl();
    STEP_1.addResource(COAL, 5);
    STEP_1.addResource(OIL, 3);
    STEP_1.addResource(GARBAGE, 2);
    STEP_1.addResource(URANIUM, 1);
    final ResourcePool STEP_2 = new ResourcePoolImpl();
    STEP_2.addResource(COAL, 6);
    STEP_2.addResource(OIL, 4);
    STEP_2.addResource(GARBAGE, 3);
    STEP_2.addResource(URANIUM, 2);
    final ResourcePool STEP_3 = new ResourcePoolImpl();
    STEP_3.addResource(COAL, 4);
    STEP_3.addResource(OIL, 5);
    STEP_3.addResource(GARBAGE, 4);
    STEP_3.addResource(URANIUM, 2);
    FOUR_PLAYER_RESTOCKS = new HashMap<Step, ResourcePool>();
    FOUR_PLAYER_RESTOCKS.put(Step.ONE, STEP_1);
    FOUR_PLAYER_RESTOCKS.put(Step.TWO, STEP_2);
    FOUR_PLAYER_RESTOCKS.put(Step.THREE, STEP_3);
  }

  private static final Map<Step, ResourcePool> FIVE_PLAYER_RESTOCKS;
  static {
    final ResourcePool STEP_1 = new ResourcePoolImpl();
    STEP_1.addResource(COAL, 5);
    STEP_1.addResource(OIL, 3);
    STEP_1.addResource(GARBAGE, 2);
    STEP_1.addResource(URANIUM, 1);
    final ResourcePool STEP_2 = new ResourcePoolImpl();
    STEP_2.addResource(COAL, 6);
    STEP_2.addResource(OIL, 4);
    STEP_2.addResource(GARBAGE, 3);
    STEP_2.addResource(URANIUM, 2);
    final ResourcePool STEP_3 = new ResourcePoolImpl();
    STEP_3.addResource(COAL, 4);
    STEP_3.addResource(OIL, 5);
    STEP_3.addResource(GARBAGE, 4);
    STEP_3.addResource(URANIUM, 2);
    FIVE_PLAYER_RESTOCKS = new HashMap<Step, ResourcePool>();
    FIVE_PLAYER_RESTOCKS.put(Step.ONE, STEP_1);
    FIVE_PLAYER_RESTOCKS.put(Step.TWO, STEP_2);
    FIVE_PLAYER_RESTOCKS.put(Step.THREE, STEP_3);
  }

  private static final Map<Step, ResourcePool> SIX_PLAYER_RESTOCKS;
  static {
    final ResourcePool STEP_1 = new ResourcePoolImpl();
    STEP_1.addResource(COAL, 7);
    STEP_1.addResource(OIL, 5);
    STEP_1.addResource(GARBAGE, 3);
    STEP_1.addResource(URANIUM, 2);
    final ResourcePool STEP_2 = new ResourcePoolImpl();
    STEP_2.addResource(COAL, 9);
    STEP_2.addResource(OIL, 6);
    STEP_2.addResource(GARBAGE, 5);
    STEP_2.addResource(URANIUM, 3);
    final ResourcePool STEP_3 = new ResourcePoolImpl();
    STEP_3.addResource(COAL, 6);
    STEP_3.addResource(OIL, 7);
    STEP_3.addResource(GARBAGE, 6);
    STEP_3.addResource(URANIUM, 3);
    SIX_PLAYER_RESTOCKS = new HashMap<Step, ResourcePool>();
    SIX_PLAYER_RESTOCKS.put(Step.ONE, STEP_1);
    SIX_PLAYER_RESTOCKS.put(Step.TWO, STEP_2);
    SIX_PLAYER_RESTOCKS.put(Step.THREE, STEP_3);
  }

  /**
   * Returns the restock rates for the given number of players
   *
   * @param players the number of players in the game; affects the rate at which
   *   resources are restocked
   * @return a non-<code>null</code> map
   */
  private static Map<Step, ResourcePool> getRestockRates(final int players) {
    switch (players) {
      case 2:
        return TWO_PLAYER_RESTOCKS;
      case 3:
        return THREE_PLAYER_RESTOCKS;
      case 4:
        return FOUR_PLAYER_RESTOCKS;
      case 5:
        return FIVE_PLAYER_RESTOCKS;
      case 6:
        return SIX_PLAYER_RESTOCKS;
      default:
        throw new IllegalArgumentException(
            "Invalid number of players: " + players);
    }
  }

  /**
   * Constructor
   *
   * @param players the number of players in the game
   */
  public StandardResourceMarket(final int players) {
    super(
        STARTING_RESOURCES, STARTING_SPARE_RESOURCES, getRestockRates(players));
  }

  @Override
  protected int getPrice(final Resource resource, final int stock) {
    if (Resource.URANIUM.equals(resource)) {
      return URANIUM_PRICES[stock - 1];
    }
    return MAXIMUM_NON_URANIUM_PRICE - ((stock - 1) / UNITS_PER_PRICE_BRACKET);
  }
}
