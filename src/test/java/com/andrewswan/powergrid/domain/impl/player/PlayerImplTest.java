/*
 * Created on 28/03/2008
 */
package com.andrewswan.powergrid.domain.impl.player;

import static org.easymock.EasyMock.expect;
import junit.framework.TestCase;

import com.andrewswan.powergrid.EasyMockContainer;
import com.andrewswan.powergrid.domain.Board;
import com.andrewswan.powergrid.domain.Game;
import com.andrewswan.powergrid.domain.Game.Step;
import com.andrewswan.powergrid.domain.Player;
import com.andrewswan.powergrid.domain.Player.Colour;
import com.andrewswan.powergrid.domain.PlayerStrategy;

/**
 * Unit test of the {@link Player} implementation
 */
public class PlayerImplTest extends TestCase {

  // Constants
  private static final int CONNECTION_COST = 27;
  private static final Step STEP = Step.ONE;
  private static final String PLAYER_NAME = "Bob";

  // Fixture
  private EasyMockContainer mocks;
  private Game mockGame;
  private PlayerImpl player;
  private PlayerStrategy mockStrategy;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    mocks = new EasyMockContainer();
    mockGame = mocks.createStrictMock(Game.class);
    mockStrategy = mocks.createStrictMock(PlayerStrategy.class);
    player =
        new PlayerImpl(PLAYER_NAME, Colour.BLACK, 3, mockStrategy, mockGame);
  }

  public void testGetElektros() {
    player.getElektros();
  }

  public void testConnectNullArrayOfCities() {
    // Set up
    expect(mockStrategy.getCitiesToConnect()).andReturn(null);
    mocks.replay();

    // Invoke
    player.connectCities();

    // Check
    mocks.verify();
  }

  public void testConnectEmptyArrayOfCities() {
    // Set up
    expect(mockStrategy.getCitiesToConnect()).andReturn(new String[0]);
    mocks.replay();

    // Invoke
    player.connectCities();

    // Check
    mocks.verify();
  }

  public void testConnectOneCity() {
    // Set up
    final String cityName = "London";
    final int startingElektros = player.getElektros();
    expect(mockStrategy.getCitiesToConnect())
        .andStubReturn(new String[] {cityName});
    final Board mockBoard = mocks.createStrictMock(Board.class);
    expect(mockGame.getBoard()).andStubReturn(mockBoard);
    expect(mockGame.getStep()).andStubReturn(STEP);
    expect(mockBoard.getConnectionCost(cityName, player, STEP))
        .andReturn(CONNECTION_COST);
    mockBoard.connectCity(cityName, player, STEP);
    mocks.replay();

    // Invoke
    player.connectCities();

    // Check
    mocks.verify();
    assertEquals(startingElektros - CONNECTION_COST, player.getElektros());
  }
}
