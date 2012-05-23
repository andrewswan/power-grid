/*
 * Created on 29/03/2008
 */
package com.andrewswan.powergrid.ui.impl;

import static com.andrewswan.powergrid.domain.ResourceMarket.Resource.COAL;
import static com.andrewswan.powergrid.domain.ResourceMarket.Resource.GARBAGE;
import static com.andrewswan.powergrid.domain.ResourceMarket.Resource.OIL;
import static com.andrewswan.powergrid.domain.ResourceMarket.Resource.URANIUM;
import static com.andrewswan.powergrid.ui.impl.TextInputDevice.LINE_SEPARATOR;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.InputStream;
import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.andrewswan.powergrid.domain.Plant;
import com.andrewswan.powergrid.domain.Player.Colour;
import com.andrewswan.powergrid.domain.ResourcePool;
import com.andrewswan.powergrid.domain.impl.ResourcePoolImpl;
import com.andrewswan.powergrid.ui.OutputDevice;

/**
 * Unit test of the text {@link TextInputDevice}
 */
public class TextInputDeviceTest {

  // Constants
  protected static final Logger LOGGER =
      LoggerFactory.getLogger(TextInputDeviceTest.class);

  private static final int MINIMUM_BID = 10;
  private static final int PLANT_NUMBER = 3;
  private static final Colour COLOUR = Colour.PURPLE;

  // Fixture
  private TextInputDevice inputDevice;
  @Mock private InputStream mockInputStream;
  @Mock private OutputDevice mockOutputDevice;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    inputDevice = new TextInputDevice(mockInputStream, mockOutputDevice);
  }

  /**
   * Sets up the mock input stream to return the given string of characters
   *
   * @param text the characters to return; can't be <code>null</code>
   * @throws Exception
   */
  private void setUpInputStream(final String text) throws Exception {
      if (StringUtils.isEmpty(text)) {
          return;
      }
      final char[] charArray = text.toCharArray();
      final Integer firstChar = Integer.valueOf(charArray[0]);
      final Integer[] remainingChars = new Integer[text.length() - 1];
      for (int i = 1; i < charArray.length; i++) {
          remainingChars[i - 1] = Integer.valueOf(charArray[i]);
      }
      when(mockInputStream.read()).thenReturn(firstChar, remainingChars);
  }

  @Test
  public void testMandatoryBidOnPlant() throws Exception {
    // Set up
    final Plant mockPlant = mock(Plant.class);
    when(mockPlant.getNumber()).thenReturn(PLANT_NUMBER);
    setUpInputStream("13" + LINE_SEPARATOR);

    // Invoke
    final Integer bid = inputDevice.bidOnPlant(COLOUR, mockPlant, MINIMUM_BID, false);

    // Check
    verify(mockOutputDevice).prompt("PURPLE: how much do you bid on plant 3" +
            " (minimum = 10): ");
    assertNotNull(bid);
    assertEquals(13, bid.intValue());
  }

  @Test
  public void testGetNoCitiesToConnect() throws Exception {
    assertCitiesToConnect(LINE_SEPARATOR, new String[0]);
  }

  @Test
  public void testGetOneCityToConnect() throws Exception {
    final String city = "Paris";
    assertCitiesToConnect(
        city + LINE_SEPARATOR + LINE_SEPARATOR, new String[] {city});
  }

  @Test
  public void testGetTwoCitiesToConnect() throws Exception {
    final String london = "London";
    final String paris = "Paris";
    assertCitiesToConnect(
        london + LINE_SEPARATOR + paris + LINE_SEPARATOR + LINE_SEPARATOR,
        new String[] {london, paris});
  }

  /**
   * Checks that the Console reads the given input stream as the given cities
   *
   * @param inputStream the input stream to read; can't be <code>null</code>,
   *   must end in a {@link TextInputDevice#LINE_SEPARATOR} on its own line
   * @param expectedCities the expected array of city names
   * @throws Exception
   */
  private void assertCitiesToConnect(
      final String inputStream, final String[] expectedCities)
    throws Exception
  {
    // Set up
    setUpInputStream(inputStream);

    // Invoke
    final String[] actualCities = inputDevice.getCitiesToConnect(COLOUR);

    // Check
    verify(mockOutputDevice).prompt("PURPLE: which cities do you want to connect" +
            " (one internal name per line, case-insensitive, with a blank line to" +
            " finish): ");
    assertTrue(Arrays.equals(expectedCities, actualCities));
  }

  @Test
  public void testGetNoPlantNumbers() throws Exception {
    assertGetPlantsToOperate(LINE_SEPARATOR, new int[0]);
  }

  @Test
  public void testGetOnePlantNumber() throws Exception {
    assertGetPlantsToOperate("5" + LINE_SEPARATOR, new int[] {5});
  }

  @Test
  public void testGetFourPlantNumbers() throws Exception {
    // Here we test all combinations of separators
    assertGetPlantsToOperate(
        "5 8,9, 10" + LINE_SEPARATOR, new int[] {5, 8, 9, 10});
  }

  /**
   * Checks that the Console reads the given input stream as the given array of
   * plant numbers
   *
   * @param inputStream the input stream to read; can't be <code>null</code>,
   *   must end in a {@link TextInputDevice#LINE_SEPARATOR} on its own line
   * @param expectedPlantNumbers the expected array of plant numbers
   * @throws Exception
   */
  private void assertGetPlantsToOperate(
      final String inputStream, final int[] expectedPlantNumbers)
    throws Exception
  {
    // Set up
    setUpInputStream(inputStream);

    // Invoke
    final int[] actualPlantNumbers = inputDevice.getPlantsToOperate(COLOUR);

    // Check
    verify(mockOutputDevice).prompt("PURPLE: which plant numbers do you want to" +
            " operate (separated by spaces or commas): ");
    assertTrue(Arrays.equals(expectedPlantNumbers, actualPlantNumbers));
  }

  @Test
  public void testBuyNoResources() throws Exception {
    assertGetResourcesToBuy(LINE_SEPARATOR, new ResourcePoolImpl());
  }

  @Test
  public void testBuyOneCoal() throws Exception {
    final ResourcePool expectedResources = new ResourcePoolImpl();
    expectedResources.addResource(COAL, 1);
    assertGetResourcesToBuy("1c" + LINE_SEPARATOR, expectedResources);
  }

  @Test
  public void testBuySomeOfEachResource() throws Exception {
    final ResourcePool expectedResources = new ResourcePoolImpl();
    expectedResources.addResource(COAL, 10);
    expectedResources.addResource(OIL, 11);
    expectedResources.addResource(GARBAGE, 12);
    expectedResources.addResource(URANIUM, 13);
    assertGetResourcesToBuy(
        "10c 11o 12g 13u" + LINE_SEPARATOR, expectedResources);
  }

  /**
   * Checks that the Console reads the given input stream as the given pool of
   * resources to buy
   *
   * @param inputStream the input stream to read; can't be <code>null</code>,
   *   must end in a {@link TextInputDevice#LINE_SEPARATOR} on its own line
   * @param expectedResources the expected resources to buy
   * @throws Exception
   */
  private void assertGetResourcesToBuy(
      final String inputStream, final ResourcePool expectedResources)
    throws Exception
  {
    // Set up
    setUpInputStream(inputStream);

    // Invoke
    final ResourcePool actualResources = inputDevice.getResourcesToBuy(COLOUR);

    // Check
    verify(mockOutputDevice).prompt("PURPLE: what resources do you want to buy?\n" +
            "(format is [n][c|o|g|u] per resource, e.g. '1c 2o' means 1 coal and" +
            " 2 oil): ");
    assertEquals(expectedResources, actualResources);
  }

  @Test
  public void testSelectNoPlantForAuction() throws Exception {
    // Set up
    final Plant[] currentMarket = getMockPlants(PLANT_NUMBER, PLANT_NUMBER + 1);
    LOGGER.debug("Current market contains plants x " + currentMarket.length);
    setUpInputStream("0" + LINE_SEPARATOR);

    // Invoke
    final Integer plantNumber =
        inputDevice.selectPlantForAuction(COLOUR, currentMarket, false);

    // Check
    verify(mockOutputDevice).prompt(
            "PURPLE: which plant number do you want to auction (0 means none): ");
    assertNull(plantNumber);
  }

  @Test
  public void testSelectPlantForAuction() throws Exception {
    // Set up
    final Plant[] currentMarket = getMockPlants(PLANT_NUMBER);
    setUpInputStream(PLANT_NUMBER + LINE_SEPARATOR);

    // Invoke
    final Integer plantNumber =
        inputDevice.selectPlantForAuction(COLOUR, currentMarket, false);

    // Check
    verify(mockOutputDevice).prompt("PURPLE: which plant number do you want to auction" +
            " (0 means none): ");
    assertNotNull(plantNumber);
    assertEquals(PLANT_NUMBER, plantNumber.intValue());
  }

  /**
   * Returns an array of mock plants with the given (stub) plant numbers
   *
   * @param plantNumbers the plant numbers the mock plants are to have
   * @return a non-<code>null</code> array
   */
  private Plant[] getMockPlants(final int... plantNumbers) {
    final Plant[] mockPlants = new Plant[plantNumbers.length];
    for (int i = 0; i < plantNumbers.length; i++) {
      final Plant mockPlant = mock(Plant.class);
      when(mockPlant.getNumber()).thenReturn(plantNumbers[i]);
      mockPlants[i] = mockPlant;
    }
    return mockPlants;
  }
}
