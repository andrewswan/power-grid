/*
 * Created on 21/02/2008
 */
package com.andrewswan.powergrid.domain.impl.board.maps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Set;

import org.junit.Test;

import com.andrewswan.powergrid.domain.Area;
import com.andrewswan.powergrid.domain.Board;

/**
 * Unit test of the U.S.A. board
 */
public class USABoardTest {

    @Test
  public void testAreaAdjacencies() {
    assertAdjacentAreas("Purple", 3);
    assertAdjacentAreas("Dark Green", 2);
    assertAdjacentAreas("Yellow", 4);
    assertAdjacentAreas("Red", 4);
    assertAdjacentAreas("Brown", 2);
    assertAdjacentAreas("Light Green", 3);
  }

  /**
   * Checks that the named area has the given number of adjacent areas
   *
   * @param areaName
   * @param expectedNeighbours
   */
  private void assertAdjacentAreas(final String areaName, final int expectedNeighbours) {
    final AbstractBoard board = new USABoard(2);
    board.setAreaAdjacencies();
    final Area area = board.getArea(areaName);
    assertEquals(expectedNeighbours, area.getAdjacentAreas().size());
  }

  @Test
  public void testVariousLinkCounts() {
    assertLinkCountEquals("Kansas City", 7);
    assertLinkCountEquals("Minneapolis", 6);
    assertLinkCountEquals("Boston", 1);
    assertLinkCountEquals("Santa Fe", 8);
  }

  /**
   * Checks that the city with the given internal name has the given number of
   * links to other cities
   *
   * @param cityInternalName
   * @param expectedLinkCount
   */
  private void assertLinkCountEquals(
      final String cityInternalName, final int expectedLinkCount)
  {
    final AbstractBoard board = new USABoard(2);
    final int linkCount = board.getCity(cityInternalName).numNeighbors();
    assertEquals("Wrong link count for " + cityInternalName,
        expectedLinkCount, linkCount);
  }

  @Test
  public void testAreasInPlay() {
    assertNumberOfAreasInPlay(2, 3);
    assertNumberOfAreasInPlay(3, 3);
    assertNumberOfAreasInPlay(4, 4);
    assertNumberOfAreasInPlay(5, 5);
    assertNumberOfAreasInPlay(6, 5);
  }

  /**
   * Checks that for the given number of players, the given number of areas are
   * in play
   *
   * @param players
   * @param expectedAreas
   */
  private void assertNumberOfAreasInPlay(final int players, final int expectedAreas) {
    // Set up
    final Board board = new USABoard(players);

    // Invoke
    final Set<Area> areasInPlay = board.getAreasInPlay();

    // Check
    assertNotNull(areasInPlay);
    assertEquals(expectedAreas, areasInPlay.size());
  }
}
