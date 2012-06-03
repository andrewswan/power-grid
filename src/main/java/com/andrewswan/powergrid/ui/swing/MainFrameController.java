package com.andrewswan.powergrid.ui.swing;

import java.awt.Component;

import org.apache.commons.lang.Validate;

/**
 * Handles events coming from the {@link MainFrame} and its subordinate
 * {@link Component}s.
 *
 * @author Andrew Swan
 */
public class MainFrameController {
    
    // Fields
    private MainFrame mainFrame;
    
    /**
     * Constructor.
     */
    public MainFrameController() {
        // Empty
    }

    public void setMainFrame(final MainFrame mainFrame) {
        Validate.notNull(mainFrame);
        this.mainFrame = mainFrame;
    }
    
    public void exitApplication() {
        System.exit(0);
    }
    
    public void showApplicationDetails() {
        new AboutDialog(mainFrame);
    }

    public void startNewGame() {
        System.out.println("Starting new game");
        // TODO Auto-generated method stub
    }
}
