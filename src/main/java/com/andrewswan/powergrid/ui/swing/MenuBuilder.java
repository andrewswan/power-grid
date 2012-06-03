package com.andrewswan.powergrid.ui.swing;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.apache.commons.lang.Validate;

/**
 * Builds a {@link JMenu}.
 * 
 * @author aswan
 */
public class MenuBuilder {
    
    // Fields
    private final JMenu menu;

    /**
     * Constructor for a {@link JMenu} with the given label.
     * 
     * @param label the menu label (required)
     */
    public MenuBuilder(final String label) {
        Validate.notEmpty(label);
        this.menu = new JMenu(label);
    }
    
    public MenuBuilder addActions(final KeyableAction... actions) {
        for (final KeyableAction action : actions) {
            final JMenuItem menuItem = new JMenuItem(action);
            menuItem.setAccelerator(action.getHotKey());
            this.menu.add(menuItem);
        }
        return this;
    }
    
    public MenuBuilder addSeparator() {
        this.menu.addSeparator();
        return this;
    }
    
    public JMenu build() {
        return menu;
    }
}
