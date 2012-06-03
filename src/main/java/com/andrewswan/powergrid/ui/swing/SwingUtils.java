package com.andrewswan.powergrid.ui.swing;

import java.awt.Window;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

/**
 * Swing-related utility methods.
 *
 * @author Andrew Swan
 */
public final class SwingUtils {

    /**
     * Centres the given {@link Window} on the screen.
     * 
     * @param window the window to centre (ignored if <code>null</code>)
     */
    public static void centre(final Window window) {
        if (window != null) {
            window.pack();
            window.setLocationRelativeTo(null);
        }
    }
    
    /**
     * Creates an empty border the given number of pixels wide.
     *
     * @param borderSize
     * @return a non-<code>null</code> border
     */
    public static Border getEmptyBorder(final int borderSize) {
        return BorderFactory.createEmptyBorder(borderSize, borderSize, borderSize, borderSize);
    }
    
    /**
     * Constructor is private to prevent instantiation.
     */
    private SwingUtils() {}
}
