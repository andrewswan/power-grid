/*
 * Created on 04/03/2008
 */
package com.andrewswan.powergrid.domain.impl.player.strategy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.andrewswan.powergrid.domain.Game;
import com.andrewswan.powergrid.domain.Plant;
import com.andrewswan.powergrid.domain.PlayerStrategy;
import com.andrewswan.powergrid.domain.ResourcePool;

/**
 * Unit test of the {@link DoNothingStrategy}
 */
public class DoNothingStrategyTest {

  // Constants
  private static final int MINIMUM_BID = 3; // arbitrary

  // Fixture
  private PlayerStrategy strategy;
  @Mock private Game mockGame;
  @Mock private Plant mockPlant1;
  @Mock private Plant mockPlant2;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    strategy = new DoNothingStrategy(mockGame);
  }

  @Test
  public void testMandatoryBidOnPlant() {
    // Invoke
    final Integer bid = strategy.bidOnPlant(mockPlant1, MINIMUM_BID, false);

    // Check
    assertNotNull(bid);
    assertEquals(MINIMUM_BID, bid.intValue());
  }

  @Test
  public void testOptionalBidOnPlant() {
    // Invoke
    final Integer bid = strategy.bidOnPlant(mockPlant1, MINIMUM_BID, true);

    // Check
    assertNull(bid);
  }

  @Test
  public void testBuyResources() {
    // Invoke
    final ResourcePool resourcesToBuy = strategy.getResourcesToBuy();
    
    // Check
    assertNull(resourcesToBuy);
  }

  @Test
  public void testConnectCities() {
    // Invoke
    final String[] citiesToConnect = strategy.getCitiesToConnect();

    // Check
    assertNotNull(citiesToConnect);
    assertEquals(0, citiesToConnect.length);
  }

  @Test
  public void testPowerCities() {
    // Invoke
    final int[] plantsToOperate = strategy.getPlantsToOperate();

    // Check
    assertNotNull(plantsToOperate);
    assertEquals(0, plantsToOperate.length);
  }

  @Test
  public void testSelectOptionalPlantForAuction() {
    // Invoke
    final Plant plant = strategy.selectPlantForAuction(false);

    // Check
    assertNull(plant);
  }

  @Test
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
