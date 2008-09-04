/*
 * Created on 01/04/2008
 */
package com.andrewswan.powergrid.ui.impl;

import java.io.PrintStream;

import org.apache.commons.io.IOUtils;

import com.andrewswan.powergrid.Utils;
import com.andrewswan.powergrid.ui.OutputDevice;

/**
 * An {@link OutputDevice} that sends its output as a character stream, for
 * example to the console or a text file
 */
public class TextOutputDevice implements OutputDevice {

  // Properties
  private final PrintStream printStream;

  /**
   * Constructor
   * 
   * @param printStream the destination to which the text output should be sent;
   *   can't be <code>null</code>
   */
  public TextOutputDevice(PrintStream printStream) {
    Utils.checkNotNull(printStream);
    this.printStream = printStream;
  }
  
  public void prompt(String prompt) {
    printStream.println(prompt);
  }

  public void showError(String message) {
    printStream.println(message);
  }

  public void close() {
    IOUtils.closeQuietly(printStream);
  }
}
