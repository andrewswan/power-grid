/*
 * Created on 07/02/2008
 */
package com.andrewswan.powergrid.domain;

import edu.uci.ics.jung.graph.Edge;

/**
 * A link on the map from the owning {@link City} to another, which may or may
 * not be in the same {@link Area} of the {@link Board}.
 */
public interface Link extends Edge {

  /**
   * Returns the cost of building across this link
   * 
   * @return zero or more
   */
  int getCost();
}
