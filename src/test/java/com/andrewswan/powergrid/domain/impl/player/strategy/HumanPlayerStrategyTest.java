/*
 * Created on 29/03/2008
 */
package com.andrewswan.powergrid.domain.impl.player.strategy;

import static org.easymock.EasyMock.expect;
import junit.framework.TestCase;

import com.andrewswan.powergrid.EasyMockContainer;
import com.andrewswan.powergrid.domain.Game;
import com.andrewswan.powergrid.domain.Plant;
import com.andrewswan.powergrid.domain.ResourcePool;
import com.andrewswan.powergrid.domain.Player.Colour;
import com.andrewswan.powergrid.ui.InputDevice;

/**
 * Unit test of {@link HumanPlayerStrategy}
 */
public class HumanPlayerStrategyTest extends TestCase {

  // Constants (arbitrary)
  private static final boolean CAN_PASS = false;
  private static final int MINIMUM_BID = 10;
  private static final int PLANT_NUMBER = 30;
  private static final Colour COLOUR = Colour.BLUE;
  private static final Integer BID = 15;
  private static final String CITY_NAME = "Timbuktu";

  // Fixture
  private EasyMockContainer mocks;
  private Game mockGame;
  private HumanPlayerStrategy strategy;
  private InputDevice mockInputDevice;
  
  @Override
  protected void setUp() throws Exception {
    super.setUp();
    mocks = new EasyMockContainer();
    mockGame = mocks.createStrictMock(Game.class);
    mockInputDevice = mocks.createStrictMock(InputDevice.class);
    strategy = new HumanPlayerStrategy(COLOUR, mockGame, mockInputDevice);
  }
  
  public void testBidOnPlant() {
    // Set up
    Plant mockPlant = mocks.createStrictMock(Plant.class);
    expect(mockInputDevice.bidOnPlant(COLOUR, mockPlant, MINIMUM_BID, CAN_PASS))
        .andReturn(BID);
    mocks.replay();
    
    // Invoke
    Integer bid = strategy.bidOnPlant(mockPlant, MINIMUM_BID, CAN_PASS);
    
    // Check
    mocks.verify();
    assertEquals(BID, bid);
  }
  
  public void testGetPlantsToOperate() {
    // Set up
    int[] plants = new int[] {PLANT_NUMBER};
    expect(mockInputDevice.getPlantsToOperate(COLOUR)).andReturn(plants);
    mocks.replay();
    
    // Invoke
    int[] actualPlants = strategy.getPlantsToOperate();
    
    // Check
    mocks.verify();
    assertNotNull(actualPlants);
    assertEquals(plants.length, actualPlants.length);
    assertEquals(PLANT_NUMBER, actualPlants[0]);
  }
  
  public void testGetResourcesToBuy() {
    // Set up
    ResourcePool mockResources = mocks.createStrictMock(ResourcePool.class);
    expect(mockInputDevice.getResourcesToBuy(COLOUR)).andReturn(mockResources);
    mocks.replay();
    
    // Invoke
    ResourcePool resources = strategy.getResourcesToBuy();
    
    // Check
    mocks.verify();
    assertSame(mockResources, resources);
  }
  
  public void testGetCitiesToConnect() {
    // Set up
    String[] cityNames = new String[] {CITY_NAME};
    expect(mockInputDevice.getCitiesToConnect(COLOUR)).andReturn(cityNames);
    mocks.replay();
    
    // Invoke
    String[] actualCityNames = strategy.getCitiesToConnect();
    
    // Check
    mocks.verify();
    assertNotNull(actualCityNames);
    assertEquals(cityNames.length, actualCityNames.length);
    assertEquals(CITY_NAME, actualCityNames[0]);
  }
  
  public void testSelectValidPlantToAuction() {
    // Set up
    Plant mockPlantOne = mocks.createStrictMock(Plant.class);
    expect(mockPlantOne.getNumber()).andStubReturn(PLANT_NUMBER + 1);
    Plant mockPlantTwo = mocks.createStrictMock(Plant.class);
    expect(mockPlantTwo.getNumber()).andStubReturn(PLANT_NUMBER);
    Plant[] currentMarket = {mockPlantOne, mockPlantTwo};
    expect(mockGame.getCurrentMarket()).andStubReturn(currentMarket);
    expect(
        mockInputDevice.selectPlantForAuction(COLOUR, currentMarket, CAN_PASS))
            .andReturn(PLANT_NUMBER);
    mocks.replay();
    
    // Invoke
    Plant plant = strategy.selectPlantForAuction(CAN_PASS);
    
    // Check
    mocks.verify();
    assertSame(mockPlantTwo, plant);
  }
}
