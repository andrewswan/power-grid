/**
 * 
 */
package com.andrewswan.powergrid.ui.swing;

import javax.swing.JFrame;

import com.andrewswan.powergrid.domain.Controller;
import com.andrewswan.powergrid.domain.GameModel;
import com.andrewswan.powergrid.ui.AbstractView;

/**
 * A Swing-based View (in MVC terms) for this application
 * 
 * @author Admin
 */
public class SwingView extends AbstractView {
    
    // Properties
    private final JFrame frame;

    /**
     * Constructor
     *
     * @param game the MVC model for the game being viewed; can't be
     *   <code>null</code>
     * @param controller the MVC controller to invoke upon user actions; can't
     *   be <code>null</code>
     */
    public SwingView(final GameModel game, final Controller controller) {
        super(game, controller);
        this.frame = new MainFrame();
        this.frame.setVisible(true);
    }

    @Override
    protected void update(final Object message) {
        LOGGER.debug("Updating with message = " + message);
        
        // TODO Auto-generated method stub
        
    }
}
