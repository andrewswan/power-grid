/*
 * Created on 07/02/2008
 */
package com.andrewswan.powergrid.domain.impl.board;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.andrewswan.powergrid.domain.City;
import com.andrewswan.powergrid.domain.Link;

import edu.uci.ics.jung.graph.impl.UndirectedSparseEdge;

/**
 * The implementation of a {@link Link}
 */
public class LinkImpl extends UndirectedSparseEdge implements Link {

  // Properties
  private final int cost;

  /**
   * Constructor
   *
   * @param from
   * @param to
   * @param cost
   */
  public LinkImpl(final City from, final City to, final int cost) {
    super(from, to);
    this.cost = cost;
  }

  public int getCost() {
    return cost;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
  }
}
