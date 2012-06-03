package com.andrewswan.powergrid.ui.swing;

import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

/**
 * An {@link Action} that can be invoked by a {@link KeyStroke}.
 * 
 * @author Andrew Swan
 */
public abstract class KeyableAction extends AbstractAction {

    // Fields
    private final KeyStroke hotKey;

    /**
     * Constructor for a hotkey with a modifier.
     *
     * @param name the action's name
     * @param keyCode the numeric code of the hot key (see {@link KeyEvent})
     * @param modifier the key modifier, see {@link KeyStroke#getKeyStroke(int, int)}
     */
    public KeyableAction(final String name, final int keyCode, final int modifier) {
        super(name);
        this.hotKey = KeyStroke.getKeyStroke(keyCode, modifier);
    }
    
    /**
     * Constructor for a hotkey with no modifier.
     *
     * @param name the action's name
     * @param the numeric code of the hot key (see {@link KeyEvent})
     */
    public KeyableAction(final String name, final int keyCode) {
        this(name, keyCode, 0);
    }

    /**
     * Returns the hot key for this action.
     *
     * @return
     */
    public KeyStroke getHotKey() {
        return hotKey;
    }
}
