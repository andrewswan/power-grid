/*
 * Created on 01/04/2008
 */
package com.andrewswan.powergrid.ui.impl;

import com.andrewswan.powergrid.ui.OutputDevice;

/**
 * An {@link OutputDevice} that sends its output to the system console
 */
public class StandardOutput extends TextOutputDevice {

  /**
   * Constructor
   */
  public StandardOutput() {
    super(System.out);
  }

  @Override
  public void close() {
    // The System.out stream should never be closed
  }
}
