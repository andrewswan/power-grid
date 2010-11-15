/**
 * 
 */
package com.andrewswan.powergrid.domain;

import com.andrewswan.powergrid.Utils;

/**
 * The MVC controller for this application
 * 
 * @author Admin
 */
public class Controller {

    // Properties
    private final GameModel game;

    /**
     * Constructor
     *
     * @param game the MVC model for the game being controlled; can't be
     *   <code>null</code>
     */
    public Controller(final GameModel game) {
        Utils.checkNotNull(game);
        this.game = game;
    }
    
    // TODO add methods for executing each possible user action
}
