/*
 * Created on 09/03/2008
 */
package com.andrewswan.powergrid.domain.exceptions;

/**
 * Indicates that an attempt to draw the next
 * {@link com.andrewswan.powergrid.domain.Plant} from the
 * {@link com.andrewswan.powergrid.domain.Deck} has triggered
 * {@link com.andrewswan.powergrid.domain.Game.Step#THREE}.
 */
public class StepThreeStartingException extends Exception {

  // Constants
  private static final long serialVersionUID = -8375539101862109093L;

  /**
   * Constructor
   */
  public StepThreeStartingException() {
    // Empty
  }
}
