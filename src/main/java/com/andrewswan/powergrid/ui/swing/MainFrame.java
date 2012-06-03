/**
 * 
 */
package com.andrewswan.powergrid.ui.swing;

import java.awt.Dimension;

import javax.swing.JFrame;

import org.apache.commons.lang.Validate;

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
     * 
     * @param mainFrameController 
     */
    public MainFrame(final MainFrameController mainFrameController) {
        super(TITLE);
        Validate.notNull(mainFrameController);
        this.mainFrameController = mainFrameController;
        mainFrameController.setMainFrame(this);
        setDefaultCloseOperation(EXIT_ON_CLOSE); // kill the JVM when closed
        setJMenuBar(new MainMenuBar(mainFrameController));
        setPreferredSize(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));
        SwingUtils.centre(this);
        setVisible(true);
    }
}
