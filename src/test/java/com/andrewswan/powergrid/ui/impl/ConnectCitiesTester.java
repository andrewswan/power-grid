/*
 * Created on 29/03/2008
 */
package com.andrewswan.powergrid.ui.impl;

import java.util.Arrays;

import com.andrewswan.powergrid.domain.Player.Colour;
import com.andrewswan.powergrid.ui.InputDevice;

/**
 * A non-JUnit test that allows the developer to manually test the
 * {@link StandardInput}, because it's not unit-testable (uses the static fields
 * System.in and System.out).
 */
public class ConnectCitiesTester {

  /**
   * The entry point for this tester
   *
   * @param args not used
   */
  public static void main(final String[] args) {
    // Set up
    final InputDevice inputDevice = new StandardInput(new StandardOutput());

    // Invoke
    final String[] cities = inputDevice.getCitiesToConnect(Colour.BLUE);

    // Output
    System.out.println("Cities = " + Arrays.toString(cities));
  }
}
