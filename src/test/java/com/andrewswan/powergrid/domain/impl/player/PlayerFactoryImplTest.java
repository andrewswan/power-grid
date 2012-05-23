/*
 * Created on 02/03/2008
 */
package com.andrewswan.powergrid.domain.impl.player;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.andrewswan.powergrid.domain.Game;
import com.andrewswan.powergrid.domain.Player;
import com.andrewswan.powergrid.domain.Player.Colour;
import com.andrewswan.powergrid.domain.PlayerFactory;
import com.andrewswan.powergrid.domain.PlayerStrategy;
import com.andrewswan.powergrid.ui.InputDevice;

/**
 * Unit test of the basic {@link PlayerFactory} implementation
 */
public class PlayerFactoryImplTest {

  // Fixture
  private PlayerFactoryImpl factory;    // the factory being tested
  @Mock private InputDevice mockInputDevice;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    factory = new PlayerFactoryImpl(mockInputDevice);
  }

  @Test
  public void testGetNameOfFirstPlayer() {
    // Invoke
    final String name = factory.getName(0, new ArrayList<String>());

    // Check
    assertEquals("Player 1", name);
  }

  @Test
  public void testGetColourOfFirstPlayer() {
    // Invoke
    final Colour colour = factory.getColour(0, Arrays.asList(Colour.values()));

    // Check
    assertEquals(Colour.values()[0], colour);
  }

  @Test
  public void testGetStrategyOfFirstPlayer() {
    // Set up
    final Game mockGame = mock(Game.class);

    // Invoke
    final PlayerStrategy strategy = factory.getStrategy(Colour.BLACK, mockGame);

    // Check
    assertNotNull(strategy);
  }

  @Test
  public void testGetPlayers() {
    // Set up
    final int numberOfPlayers = 3;
    final Game mockGame = mock(Game.class);

    // Invoke
    final List<Player> players =
        new ArrayList<Player>(factory.getPlayers(numberOfPlayers, mockGame));

    // Check
    assertNotNull(players);
    assertEquals(numberOfPlayers, players.size());
    final Player player1 = players.get(0);
    final Player player2 = players.get(1);
    final Player player3 = players.get(2);
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
