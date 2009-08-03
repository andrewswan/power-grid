/*
 * Created on 07/02/2008
 */
package com.andrewswan.powergrid.domain.impl.board;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.andrewswan.powergrid.domain.Area;
import com.andrewswan.powergrid.domain.Board;
import com.andrewswan.powergrid.domain.City;

/**
 * Basic implementation of an {@link Area} of the {@link Board}
 */
public class AreaImpl extends HashSet<City> implements Area {

  /**
   * Required for serialisation
   */
  private static final long serialVersionUID = -578968504862405486L;

  // Properties
  private final Set<Area> adjacentAreas;
  private final String name;

  /**
   * Constructor
   *
   * @param name the name of the area; can't be blank
   */
  public AreaImpl(final String name) {
    if (StringUtils.isBlank(name)) {
      throw new IllegalArgumentException("Invalid name '" + name + "'");
    }
    this.adjacentAreas = new HashSet<Area>();
    this.name = name;
  }

  public String getName() {
    return name;
  }

  @Override
  public boolean equals(final Object object) {
    if (object == this) {
      return true;
    }
    if (!(object instanceof Area)) {
      return false;
    }
    final Area otherArea = (Area) object;
    return name.equalsIgnoreCase(otherArea.getName());
  }

  @Override
  public int hashCode() {
    return name.toLowerCase(Locale.getDefault()).hashCode();
  }

  public void addAdjacentArea(final Area area) {
    if (area == null || area == this) {
      throw new IllegalArgumentException("Invalid area " + area);
    }
    adjacentAreas.add(area);
  }

  public Set<Area> getAdjacentAreas() {
    return new HashSet<Area>(adjacentAreas);   // defensive copy
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
  }
}
