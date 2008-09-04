/*
 * Created on 29/03/2008
 */
package com.andrewswan.powergrid.ui.impl;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createStrictMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

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
  public static void main(String[] args) {
    // Set up
    StandardInput console = new StandardInput(new StandardOutput());
    Plant mockPlant = createStrictMock(Plant.class);
    expect(mockPlant.getNumber()).andStubReturn(PLANT_NUMBER);
    replay(mockPlant);
    
    // Invoke
    Integer bid = console.bidOnPlant(Colour.BLUE, mockPlant, MINIMUM_BID, true);
    
    // Output
    verify(mockPlant);
    System.out.println("Bid = " + bid);
  }
}
