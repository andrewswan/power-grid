/*
 * Created on 17/02/2008
 */
package com.andrewswan.powergrid.domain.impl;

import static com.andrewswan.powergrid.domain.impl.StandardResourceMarket.STARTING_COAL;
import static com.andrewswan.powergrid.domain.impl.StandardResourceMarket.STARTING_GARBAGE;
import static com.andrewswan.powergrid.domain.impl.StandardResourceMarket.STARTING_OIL;
import static com.andrewswan.powergrid.domain.impl.StandardResourceMarket.STARTING_URANIUM;
import junit.framework.TestCase;

import com.andrewswan.powergrid.domain.CostedResourcePool;
import com.andrewswan.powergrid.domain.ResourceMarket;
import com.andrewswan.powergrid.domain.ResourceMarket.Resource;
import com.andrewswan.powergrid.domain.ResourcePool;

/**
 * Unit test of the standard {@link ResourceMarket}
 */
public class StandardResourceMarketTest extends TestCase {

  // Fixture
  private StandardResourceMarket market;

  public void testStartingResources() {
    // Invoke
    market = new StandardResourceMarket(2); // number of players irrelevant here

    // Check
    assertEquals(STARTING_COAL, market.getStock(Resource.COAL));
    assertEquals(STARTING_OIL, market.getStock(Resource.OIL));
    assertEquals(STARTING_GARBAGE, market.getStock(Resource.GARBAGE));
    assertEquals(STARTING_URANIUM, market.getStock(Resource.URANIUM));
  }

  public void testStartingPrices() {
    // Invoke
    market = new StandardResourceMarket(2); // number of players irrelevant here

    // Check (none of these prices should be null)
    assertEquals(1, market.getPrice(Resource.COAL).intValue());
    assertEquals(3, market.getPrice(Resource.OIL).intValue());
    assertEquals(7, market.getPrice(Resource.GARBAGE).intValue());
    assertEquals(14, market.getPrice(Resource.URANIUM).intValue());
  }

  /**
   * Checks that buying the given resources costs the given number of elektros
   *
   * @param resources the resources to buy; can be <code>null</code>
   * @param expectedCost
   */
  private void assertResourceCost(final ResourcePool resources, final int expectedCost) {
    // Set up
    market = new StandardResourceMarket(2); // number of players irrelevant here

    // Invoke
    final CostedResourcePool resourcesBought =
        market.buy(resources, Integer.MAX_VALUE);

    // Check
    assertNotNull(resourcesBought);
    assertEquals(expectedCost, resourcesBought.getCost());
  }

  public void testBuyingNullResourcesCostsZero() {
    assertResourceCost(null, 0);
  }

  public void testBuyingNoResourcesCostsZero() {
    assertResourceCost(new ResourcePoolImpl(), 0);
  }

  public void testBuyingOneCoalCostsOneElektro() {
    final ResourcePool resources = new ResourcePoolImpl();
    resources.addResource(Resource.COAL, 1);
    assertResourceCost(resources, 1);
  }

  public void testBuyingOneOilCostsThreeElektro() {
    final ResourcePool resources = new ResourcePoolImpl();
    resources.addResource(Resource.OIL, 1);
    assertResourceCost(resources, 3);
  }

  public void testBuyingOneGarbageCostsSevenElektro() {
    final ResourcePool resources = new ResourcePoolImpl();
    resources.addResource(Resource.GARBAGE, 1);
    assertResourceCost(resources, 7);
  }

  public void testBuyingOneUraniumCostsFourteenElektro() {
    final ResourcePool resources = new ResourcePoolImpl();
    resources.addResource(Resource.URANIUM, 1);
    assertResourceCost(resources, 14);
  }

  public void testBuyingFourCoalCostsFiveElektro() {
    final ResourcePool resources = new ResourcePoolImpl();
    resources.addResource(Resource.COAL, 4);
    assertResourceCost(resources, 5);
  }

  public void testPriceOfCoalForAllStockLevels() {
    // Set up
    market = new StandardResourceMarket(2); // number of players irrelevant here

    // Invoke and check
    assertEquals(1, market.getPrice(Resource.COAL, 24));
    assertEquals(1, market.getPrice(Resource.COAL, 23));
    assertEquals(1, market.getPrice(Resource.COAL, 22));
    assertEquals(2, market.getPrice(Resource.COAL, 21));
    assertEquals(2, market.getPrice(Resource.COAL, 20));
    assertEquals(2, market.getPrice(Resource.COAL, 19));
    assertEquals(3, market.getPrice(Resource.COAL, 18));
    assertEquals(3, market.getPrice(Resource.COAL, 17));
    assertEquals(3, market.getPrice(Resource.COAL, 16));
    assertEquals(4, market.getPrice(Resource.COAL, 15));
    assertEquals(4, market.getPrice(Resource.COAL, 14));
    assertEquals(4, market.getPrice(Resource.COAL, 13));
    assertEquals(5, market.getPrice(Resource.COAL, 12));
    assertEquals(5, market.getPrice(Resource.COAL, 11));
    assertEquals(5, market.getPrice(Resource.COAL, 10));
    assertEquals(6, market.getPrice(Resource.COAL, 9));
    assertEquals(6, market.getPrice(Resource.COAL, 8));
    assertEquals(6, market.getPrice(Resource.COAL, 7));
    assertEquals(7, market.getPrice(Resource.COAL, 6));
    assertEquals(7, market.getPrice(Resource.COAL, 5));
    assertEquals(7, market.getPrice(Resource.COAL, 4));
    assertEquals(8, market.getPrice(Resource.COAL, 3));
    assertEquals(8, market.getPrice(Resource.COAL, 2));
    assertEquals(8, market.getPrice(Resource.COAL, 1));
  }
}
