/*
 * Created on 29/03/2008
 */
package com.andrewswan.powergrid.ui.impl;

import com.andrewswan.powergrid.ui.InputDevice;
import com.andrewswan.powergrid.ui.OutputDevice;

/**
 * The {@link System} console, an {@link InputDevice} in a game of Power Grid
 */
public class StandardInput extends TextInputDevice {

  /**
   * Constructor
   *
   * @param outputDevice the device to which output should be sent; can't be
   *   <code>null</code>
   */
  public StandardInput(final OutputDevice outputDevice) {
    super(System.in, outputDevice);
  }

  @Override
  public void close() {
    // The System.in stream should never be closed
  }
}
