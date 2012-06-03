package com.andrewswan.powergrid.ui.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
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
        setLayout(new BorderLayout());
        add(getTextArea(), BorderLayout.CENTER);
        add(getButtonPanel(), BorderLayout.SOUTH);
        SwingUtils.centre(this);
        setVisible(true);
    }

    private Component getButtonPanel() {
        final Container buttonPanel = new JPanel();
        buttonPanel.add(new JButton(new OkAction()));
        return buttonPanel;
    }

    private Component getTextArea() {
        final JTextArea textArea = new JTextArea("Written by Andrew Swan");
        textArea.setBorder(SwingUtils.getEmptyBorder(BORDER_SIZE));
        return textArea;
    }
    
    class OkAction extends AbstractAction {
        
        OkAction() {
            super("Great!");
        }

        @Override
        public void actionPerformed(final ActionEvent e) {
            dispose();
        }
    }
}
