/*
 * Created on 04/03/2008
 */
package com.andrewswan.powergrid.domain.impl.player.strategy;

import static org.mockito.Mockito.when;
import junit.framework.TestCase;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.andrewswan.powergrid.domain.Game;
import com.andrewswan.powergrid.domain.Plant;
import com.andrewswan.powergrid.domain.PlayerStrategy;
import com.andrewswan.powergrid.domain.ResourcePool;

/**
 * Unit test of the {@link DoNothingStrategy}
 */
public class DoNothingStrategyTest extends TestCase {

  // Constants
  private static final int MINIMUM_BID = 3; // arbitrary

  // Fixture
  private PlayerStrategy strategy;
  @Mock private Game mockGame;
  @Mock private Plant mockPlant1;
  @Mock private Plant mockPlant2;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    MockitoAnnotations.initMocks(this);
    strategy = new DoNothingStrategy(mockGame);
  }

  public void testMandatoryBidOnPlant() {
    // Invoke
    final Integer bid = strategy.bidOnPlant(mockPlant1, MINIMUM_BID, false);

    // Check
    assertNotNull(bid);
    assertEquals(MINIMUM_BID, bid.intValue());
  }

  public void testOptionalBidOnPlant() {
    // Invoke
    final Integer bid = strategy.bidOnPlant(mockPlant1, MINIMUM_BID, true);

    // Check
    assertNull(bid);
  }

  public void testBuyResources() {
    // Invoke
    final ResourcePool resourcesToBuy = strategy.getResourcesToBuy();
    
    // Check
    assertNull(resourcesToBuy);
  }

  public void testConnectCities() {
    // Invoke
    final String[] citiesToConnect = strategy.getCitiesToConnect();

    // Check
    assertNotNull(citiesToConnect);
    assertEquals(0, citiesToConnect.length);
  }

  public void testPowerCities() {
    // Invoke
    final int[] plantsToOperate = strategy.getPlantsToOperate();

    // Check
    assertNotNull(plantsToOperate);
    assertEquals(0, plantsToOperate.length);
  }

  public void testSelectOptionalPlantForAuction() {
    // Invoke
    final Plant plant = strategy.selectPlantForAuction(false);

    // Check
    assertNull(plant);
  }

  public void testSelectMandatoryPlantForAuction() {
    // Set up
    when(mockGame.getCurrentMarket())
        .thenReturn(new Plant[] {mockPlant1, mockPlant2});

    // Invoke
    final Plant plant = strategy.selectPlantForAuction(true);

    // Check
    assertNotNull(plant);
    assertSame(mockPlant1, plant);
  }
}
