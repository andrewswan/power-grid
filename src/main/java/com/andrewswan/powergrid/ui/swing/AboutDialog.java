package com.andrewswan.powergrid.ui.swing;

import java.awt.Component;
import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JTextArea;

/**
 * A {@link JDialog} that displays program information.
 *
 * @author Andrew Swan
 */
public class AboutDialog extends JDialog {

    private static final int BORDER_SIZE = 10;

    public AboutDialog(final Frame owner) {
        super(owner, "About", true);
        add(getTextArea());
        SwingUtils.centre(this);
        setVisible(true);
    }

    private Component getTextArea() {
        final JTextArea textArea = new JTextArea("Written by Andrew Swan");
        textArea.setBorder(SwingUtils.getEmptyBorder(BORDER_SIZE));
        return textArea;
    }
}
