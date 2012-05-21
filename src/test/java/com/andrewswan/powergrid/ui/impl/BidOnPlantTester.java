/*
 * Created on 29/03/2008
 */
package com.andrewswan.powergrid.ui.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.andrewswan.powergrid.domain.Plant;
import com.andrewswan.powergrid.domain.Player.Colour;

/**
 * A non-JUnit test that allows the developer to manually test the
 * {@link StandardInput}, because it's not unit-testable (uses the static fields
 * System.in and System.out).
 */
public class BidOnPlantTester {

  // Constants
  private static final int MINIMUM_BID = 10;
  private static final int PLANT_NUMBER = 3;

  /**
   * The entry point for this tester
   *
   * @param args not used
   */
  public static void main(final String[] args) {
    // Set up
    final StandardInput console = new StandardInput(new StandardOutput());
    final Plant mockPlant = mock(Plant.class);
    when(mockPlant.getNumber()).thenReturn(PLANT_NUMBER);

    // Invoke
    final Integer bid = console.bidOnPlant(Colour.BLUE, mockPlant, MINIMUM_BID, true);

    // Output
    System.out.println("Bid = " + bid);
  }
}
