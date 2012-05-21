/*
 * Created on 29/03/2008
 */
package com.andrewswan.powergrid.domain.impl.player.strategy;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import junit.framework.TestCase;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.andrewswan.powergrid.domain.Game;
import com.andrewswan.powergrid.domain.Plant;
import com.andrewswan.powergrid.domain.Player.Colour;
import com.andrewswan.powergrid.domain.ResourcePool;
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
  private HumanPlayerStrategy strategy;
  @Mock private Game mockGame;
  @Mock private InputDevice mockInputDevice;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    MockitoAnnotations.initMocks(this);
    strategy = new HumanPlayerStrategy(COLOUR, mockGame, mockInputDevice);
  }

  public void testBidOnPlant() {
    // Set up
    final Plant mockPlant = mock(Plant.class);
    when(mockInputDevice.bidOnPlant(COLOUR, mockPlant, MINIMUM_BID, CAN_PASS))
        .thenReturn(BID);

    // Invoke
    final Integer bid = strategy.bidOnPlant(mockPlant, MINIMUM_BID, CAN_PASS);

    // Check
    assertEquals(BID, bid);
  }

  public void testGetPlantsToOperate() {
    // Set up
    final int[] plants = new int[] {PLANT_NUMBER};
    when(mockInputDevice.getPlantsToOperate(COLOUR)).thenReturn(plants);

    // Invoke
    final int[] actualPlants = strategy.getPlantsToOperate();

    // Check
    assertNotNull(actualPlants);
    assertEquals(plants.length, actualPlants.length);
    assertEquals(PLANT_NUMBER, actualPlants[0]);
  }

  public void testGetResourcesToBuy() {
    // Set up
    final ResourcePool mockResources = mock(ResourcePool.class);
    when(mockInputDevice.getResourcesToBuy(COLOUR)).thenReturn(mockResources);

    // Invoke
    final ResourcePool resources = strategy.getResourcesToBuy();

    // Check
    assertSame(mockResources, resources);
  }

  public void testGetCitiesToConnect() {
    // Set up
    final String[] cityNames = new String[] {CITY_NAME};
    when(mockInputDevice.getCitiesToConnect(COLOUR)).thenReturn(cityNames);

    // Invoke
    final String[] actualCityNames = strategy.getCitiesToConnect();

    // Check
    assertNotNull(actualCityNames);
    assertEquals(cityNames.length, actualCityNames.length);
    assertEquals(CITY_NAME, actualCityNames[0]);
  }

  public void testSelectValidPlantToAuction() {
    // Set up
    final Plant mockPlantOne = mock(Plant.class);
    when(mockPlantOne.getNumber()).thenReturn(PLANT_NUMBER + 1);
    final Plant mockPlantTwo = mock(Plant.class);
    when(mockPlantTwo.getNumber()).thenReturn(PLANT_NUMBER);
    final Plant[] currentMarket = {mockPlantOne, mockPlantTwo};
    when(mockGame.getCurrentMarket()).thenReturn(currentMarket);
    when(
        mockInputDevice.selectPlantForAuction(COLOUR, currentMarket, CAN_PASS))
            .thenReturn(PLANT_NUMBER);

    // Invoke
    final Plant plant = strategy.selectPlantForAuction(CAN_PASS);

    // Check
    assertSame(mockPlantTwo, plant);
  }
}
