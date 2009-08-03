/*
 * Created on 04/03/2008
 */
package com.andrewswan.powergrid.domain.impl.player.strategy;

import static org.easymock.EasyMock.expect;
import junit.framework.TestCase;

import com.andrewswan.powergrid.EasyMockContainer;
import com.andrewswan.powergrid.domain.Game;
import com.andrewswan.powergrid.domain.Plant;
import com.andrewswan.powergrid.domain.PlayerStrategy;

/**
 * Unit test of the {@link DoNothingStrategy}
 */
public class DoNothingStrategyTest extends TestCase {

  // Constants
  private static final int MINIMUM_BID = 3; // arbitrary

  // Fixture
  private EasyMockContainer mocks;
  private PlayerStrategy strategy;
  private Game mockGame;
  private Plant mockPlant1;
  private Plant mockPlant2;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    mocks = new EasyMockContainer();
    mockGame = mocks.createStrictMock(Game.class);
    mockPlant1 = mocks.createStrictMock(Plant.class);
    mockPlant2 = mocks.createStrictMock(Plant.class);
    strategy = new DoNothingStrategy(mockGame);
  }

  public void testMandatoryBidOnPlant() {
    // Set up
    mocks.replay();

    // Invoke
    final Integer bid = strategy.bidOnPlant(mockPlant1, MINIMUM_BID, false);

    // Check
    mocks.verify();
    assertNotNull(bid);
    assertEquals(MINIMUM_BID, bid.intValue());
  }

  public void testOptionalBidOnPlant() {
    // Set up
    mocks.replay();

    // Invoke
    final Integer bid = strategy.bidOnPlant(mockPlant1, MINIMUM_BID, true);

    // Check
    mocks.verify();
    assertNull(bid);
  }

  public void testBuyResources() {
    // Set up
    mocks.replay();

    // Invoke
    strategy.getResourcesToBuy();

    // Check
    mocks.verify();
  }

  public void testConnectCities() {
    // Set up
    mocks.replay();

    // Invoke
    strategy.getCitiesToConnect();

    // Check
    mocks.verify();
  }

  public void testPowerCities() {
    // Set up
    mocks.replay();

    // Invoke
    final int[] plantsToOperate = strategy.getPlantsToOperate();

    // Check
    mocks.verify();
    assertNotNull(plantsToOperate);
    assertEquals(0, plantsToOperate.length);
  }

  public void testSelectOptionalPlantForAuction() {
    // Set up
    mocks.replay();

    // Invoke
    final Plant plant = strategy.selectPlantForAuction(false);

    // Check
    mocks.verify();
    assertNull(plant);
  }

  public void testSelectMandatoryPlantForAuction() {
    // Set up
    expect(mockGame.getCurrentMarket())
        .andStubReturn(new Plant[] {mockPlant1, mockPlant2});
    mocks.replay();

    // Invoke
    final Plant plant = strategy.selectPlantForAuction(true);

    // Check
    mocks.verify();
    assertNotNull(plant);
    assertSame(mockPlant1, plant);
  }
}
