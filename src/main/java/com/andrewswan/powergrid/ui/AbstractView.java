/**
 * 
 */
package com.andrewswan.powergrid.ui;

import java.util.Observable;
import java.util.Observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.andrewswan.powergrid.Utils;
import com.andrewswan.powergrid.domain.Controller;
import com.andrewswan.powergrid.domain.GameModel;

/**
 * Superclass for for this application's MVC views 
 * 
 * @author Admin
 */
public abstract class AbstractView implements Observer {
    
    // Constants
    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractView.class);
    
    // Properties
    protected final Controller controller;
    protected final GameModel game;
    
    /**
     * Constructor
     *
     * @param game the MVC model for the game being viewed; can't be
     *   <code>null</code>
     * @param controller the MVC controller to invoke upon user actions; can't
     *   be <code>null</code>
     */
    protected AbstractView(final GameModel game, final Controller controller) {
        Utils.checkNotNull(controller, game);
        this.controller = controller;
        this.game = game;
        game.addObserver(this);
    }

    @Override
    public void update(final Observable observable, final Object message) {
        Utils.checkTrue(
                observable == game, "Unexpected Observable " + observable);
        update(message);
    }

    /**
     * Views should update themselves as necessary based on the given message
     * from the model and the state of the inherited <code>game</code> field
     * 
     * @param message the message from the model; can be <code>null</code>
     */
    protected abstract void update(final Object message);
}
