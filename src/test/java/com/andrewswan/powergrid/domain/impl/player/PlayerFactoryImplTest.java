/*
 * Created on 02/03/2008
 */
package com.andrewswan.powergrid.domain.impl.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import com.andrewswan.powergrid.EasyMockContainer;
import com.andrewswan.powergrid.domain.Game;
import com.andrewswan.powergrid.domain.Player;
import com.andrewswan.powergrid.domain.PlayerFactory;
import com.andrewswan.powergrid.domain.PlayerStrategy;
import com.andrewswan.powergrid.domain.Player.Colour;
import com.andrewswan.powergrid.ui.InputDevice;

/**
 * Unit test of the basic {@link PlayerFactory} implementation
 */
public class PlayerFactoryImplTest extends TestCase {

  // Fixture
  private PlayerFactoryImpl factory;    // the factory being tested
  private EasyMockContainer mocks;
  private InputDevice mockInputDevice;
  
  @Override
  protected void setUp() throws Exception {
    super.setUp();
    mocks = new EasyMockContainer();
    mockInputDevice = mocks.createStrictMock(InputDevice.class);
    factory = new PlayerFactoryImpl(mockInputDevice);
  }

  public void testGetNameOfFirstPlayer() {
    // Set up
    mocks.replay();
    
    // Invoke
    String name = factory.getName(0, new ArrayList<String>());
    
    // Check
    mocks.verify();
    assertEquals("Player 1", name);
  }

  public void testGetColourOfFirstPlayer() {
    // Set up
    mocks.replay();
    
    // Invoke
    Colour colour = factory.getColour(0, Arrays.asList(Colour.values()));
    
    // Check
    mocks.verify();
    assertEquals(Colour.values()[0], colour);
  }
  
  public void testGetStrategyOfFirstPlayer() {
    // Set up
    Game mockGame = mocks.createStrictMock(Game.class);
    mocks.replay();
    
    // Invoke
    PlayerStrategy strategy = factory.getStrategy(Colour.BLACK, mockGame);
    
    // Check
    mocks.verify();
    assertNotNull(strategy);
  }
  
  public void testGetPlayers() {
    // Set up
    int numberOfPlayers = 3;
    Game mockGame = mocks.createStrictMock(Game.class);
    mocks.replay();
    
    // Invoke
    List<Player> players =
        new ArrayList<Player>(factory.getPlayers(numberOfPlayers, mockGame));
    
    // Check
    mocks.verify();
    assertNotNull(players);
    assertEquals(numberOfPlayers, players.size());
    Player player1 = players.get(0);
    Player player2 = players.get(1);
    Player player3 = players.get(2);
    // Ensure the colours are all different
    assertFalse(player1.getColour().equals(player2.getColour()));
    assertFalse(player1.getColour().equals(player3.getColour()));
    assertFalse(player2.getColour().equals(player3.getColour()));
    // Ensure the names are all different
    assertFalse(player1.getName().equals(player2.getName()));
    assertFalse(player1.getName().equals(player3.getName()));
    assertFalse(player2.getName().equals(player3.getName()));
  }
}
