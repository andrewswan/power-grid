/*
 * Created on 07/02/2008
 */
package com.andrewswan.powergrid.domain.impl.board;

import junit.framework.TestCase;

import com.andrewswan.powergrid.domain.Area;
import com.andrewswan.powergrid.domain.City;

/**
 * Tests our implementation of a {@link City}
 */
public class CityImplTest extends TestCase {

  // Constants
  private static final String DISPLAY_NAME = "Paris";
  private static final String INTERNAL_NAME = "ParisNE";
  
  // Fixture
  private Area area;
  private City city;
  
  @Override
  protected void setUp() throws Exception {
    super.setUp();
    area = new AreaImpl("West");
  }
  
  public void testCityWithDifferentInternalAndDisplayNames() {
    // Invoke
    city = new CityImpl(area, DISPLAY_NAME, INTERNAL_NAME);
    
    // Check
    assertEquals(DISPLAY_NAME, city.getDisplayName());
    assertEquals(INTERNAL_NAME, city.getInternalName());
    assertEquals(area, city.getArea());
  }
  
  public void testCityWithSameInternalAndDisplayNames() {
    // Invoke
    city = new CityImpl(area, DISPLAY_NAME);
    
    // Check
    assertEquals(DISPLAY_NAME, city.getDisplayName());
    assertEquals(DISPLAY_NAME, city.getInternalName());
    assertEquals(area, city.getArea());
  }
}
