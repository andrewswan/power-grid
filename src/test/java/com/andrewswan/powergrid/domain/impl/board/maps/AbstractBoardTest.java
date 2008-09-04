/*
 * Created on 21/02/2008
 */
package com.andrewswan.powergrid.domain.impl.board.maps;

import junit.framework.TestCase;

import com.andrewswan.powergrid.domain.Area;
import com.andrewswan.powergrid.domain.Board;
import com.andrewswan.powergrid.domain.City;

/**
 * Unit test of our basic {@link Board} implementation, using a dummy subclass
 */
public class AbstractBoardTest extends TestCase {

  public void testAddAreasAndCitiesAndGetDistances() {
    // Set up
    TestBoard board = new TestBoard(2);
    
    // Invoke
    int cost = board.getTotalLinkCost(board.cityA, board.cityC);
    
    // Check
    assertEquals(4 + 3, cost);
  }
  
  /**
   * A test {@link Board}
   */
  private static class TestBoard extends AbstractBoard {
    
    // Properties
    private final City cityA;
    private final City cityB;
    private final City cityC;

    /**
     * Constructor
     * 
     * @param players
     */
    protected TestBoard(int players) {
      super("Nowhere", players);
      Area north = addArea("North");
      cityA = addCity("A", north); 
      cityB = addCity("B", north); 
      Area south = addArea("South");
      cityC = addCity("C", south);
      addLink(cityA, cityB, 4);
      addLink(cityA, cityC, 10);
      addLink(cityB, cityC, 3);
    }
  }
}
