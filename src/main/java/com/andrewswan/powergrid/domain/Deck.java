/*
 * Created on 08/02/2008
 */
package com.andrewswan.powergrid.domain;

import com.andrewswan.powergrid.domain.exceptions.StepThreeStartingException;

/**
 * The deck from which new {@link Plant}s are drawn.
 */
public interface Deck {

  /**
   * Returns the next plant from the deck
   * 
   * @return <code>null</code> if the deck is empty (rare)
   * @throws StepThreeStartingException if the next plant was the "Step Three"
   *   card (not implemented as a card in this program)
   */
  Plant getNextPlant() throws StepThreeStartingException;
  
  /**
   * Puts the given Plant back "under" the deck for use in Step three
   * 
   * @param plant the plant to put under the deck; can't be <code>null</code>
   */
  void assignToStepThree(Plant plant);
}
