/**
 * 
 */
package com.andrewswan.powergrid.ui.swing;

import javax.swing.JFrame;

/**
 * The main {@link JFrame} for this Swing view
 * 
 * @author Admin
 */
public class MainFrame extends JFrame {

    // Constants
    private static final String TITLE = "Power Grid";
    
    // Properties
    private final WelcomeDialog welcomeDialog;
    
    /**
     * Constructor
     */
    public MainFrame() {
        super(TITLE);
        setDefaultCloseOperation(EXIT_ON_CLOSE);    // kill the JVM when closed
        
        // TODO Auto-generated constructor stub
        
        pack(); // size to fit contents
        setLocationRelativeTo(null);    // centre on screen
        this.welcomeDialog = new WelcomeDialog(this);
    }
}
