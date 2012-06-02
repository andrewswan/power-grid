/**
 * 
 */
package com.andrewswan.powergrid.ui.swing;

import java.awt.Dimension;

import javax.swing.JFrame;

/**
 * The main {@link JFrame} for this Swing view
 * 
 * @author Andrew Swan
 */
public class MainFrame extends JFrame {

    // Constants
    private static final int PREFERRED_HEIGHT = 400;
    private static final int PREFERRED_WIDTH = 600;
    private static final String TITLE = "Power Grid";
    
    // Fields
    private final MainFrameController mainFrameController;

    /**
     * Constructor
     * @param mainFrameController 
     */
    public MainFrame(final MainFrameController mainFrameController) {
        super(TITLE);
        this.mainFrameController = mainFrameController;
        setDefaultCloseOperation(EXIT_ON_CLOSE); // kill the JVM when closed
        setPreferredSize(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));
        // TODO Auto-generated constructor stub

        pack(); // size to fit contents
        setLocationRelativeTo(null); // centre on screen
        setVisible(true);
    }
}
