/**
 * 
 */
package com.andrewswan.powergrid.ui.swing;

import javax.swing.SwingUtilities;

import com.andrewswan.powergrid.domain.Controller;
import com.andrewswan.powergrid.domain.GameModel;


/**
 * Runs this application using a Swing view
 * 
 * @author Admin
 */
public final class SwingRunner {

    /**
     * Starts this application using Swing
     * 
     * @param args ignored
     */
    public static void main(final String[] args) {
        final GameModel game = new GameModel();
        final Controller controller = new Controller(game);
        
        // Always create Swing components in the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(new Runnable() {
            
            @Override
            public void run() {
                // Start the GUI and await user input
                new SwingView(game, controller);
            }
        });
    }

}
