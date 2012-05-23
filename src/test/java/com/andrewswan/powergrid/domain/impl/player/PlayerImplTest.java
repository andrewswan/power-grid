/*
 * Created on 28/03/2008
 */
package com.andrewswan.powergrid.domain.impl.player;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.andrewswan.powergrid.domain.Board;
import com.andrewswan.powergrid.domain.Game;
import com.andrewswan.powergrid.domain.Game.Step;
import com.andrewswan.powergrid.domain.Player;
import com.andrewswan.powergrid.domain.Player.Colour;
import com.andrewswan.powergrid.domain.PlayerStrategy;

/**
 * Unit test of the {@link Player} implementation
 */
public class PlayerImplTest {

  // Constants
  private static final int CONNECTION_COST = 27;
  private static final Step STEP = Step.ONE;
  private static final String PLAYER_NAME = "Bob";

  // Fixture
  private PlayerImpl player;
  @Mock private Game mockGame;
  @Mock private PlayerStrategy mockStrategy;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    player =
        new PlayerImpl(PLAYER_NAME, Colour.BLACK, 3, mockStrategy, mockGame);
  }

  @Test
  public void testGetElektros() {
    player.getElektros();
  }

  @Test
  public void testConnectNullArrayOfCities() {
    // Set up
    when(mockStrategy.getCitiesToConnect()).thenReturn(null);

    // Invoke
    player.connectCities();
  }

  @Test
  public void testConnectEmptyArrayOfCities() {
    // Set up
    when(mockStrategy.getCitiesToConnect()).thenReturn(new String[0]);

    // Invoke
    player.connectCities();
  }

  @Test
  public void testConnectOneCity() {
    // Set up
    final String cityName = "London";
    final int startingElektros = player.getElektros();
    when(mockStrategy.getCitiesToConnect())
        .thenReturn(new String[] {cityName});
    final Board mockBoard = mock(Board.class);
    when(mockGame.getBoard()).thenReturn(mockBoard);
    when(mockGame.getStep()).thenReturn(STEP);
    when(mockBoard.getConnectionCost(cityName, player, STEP))
        .thenReturn(CONNECTION_COST);
    mockBoard.connectCity(cityName, player, STEP);

    // Invoke
    player.connectCities();

    // Check
    assertEquals(startingElektros - CONNECTION_COST, player.getElektros());
  }
}
