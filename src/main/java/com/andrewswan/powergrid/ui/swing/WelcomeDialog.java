/**
 * 
 */
package com.andrewswan.powergrid.ui.swing;

import java.awt.Frame;

import javax.swing.JDialog;


/**
 * The modal dialog initially displayed when the application starts
 * 
 * @author Admin
 */
public class WelcomeDialog extends JDialog {

    // Constants
    private static final String TITLE = "Welcome";

    /**
     * Constructor
     *
     * @param owner can't be <code>null</code>
     */
    public WelcomeDialog(final Frame owner) {
        super(owner, TITLE, true);
    }
}
