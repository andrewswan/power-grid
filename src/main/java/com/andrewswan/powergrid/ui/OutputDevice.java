/*
 * Created on 29/03/2008
 */
package com.andrewswan.powergrid.ui;

/**
 * A component that provides output to one or more humans involved in the game
 */
public interface OutputDevice {

  /**
   * Prompts the user for input with the given text
   * 
   * @param prompt the prompt to show
   */
  void prompt(String prompt);

  /**
   * Displays the given error message to the user
   * 
   * @param message the message to show
   */
  void showError(String message);
  
  /**
   * Closes this output device
   */
  void close();
}
