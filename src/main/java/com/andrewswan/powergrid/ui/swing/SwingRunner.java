/**
 * 
 */
package com.andrewswan.powergrid.ui.swing;

import javax.swing.SwingUtilities;

/**
 * Runs this application using a Swing view
 * 
 * @author Andrew Swan
 */
public final class SwingRunner {

    /**
     * Starts this application using Swing
     * 
     * @param args ignored
     */
    public static void main(final String[] args) {
        final MainFrameController mainFrameController = new MainFrameController();

        // Always create Swing components in the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                // Start the GUI and await user input
                new MainFrame(mainFrameController);
            }
        });
    }

    private SwingRunner() {}
}
