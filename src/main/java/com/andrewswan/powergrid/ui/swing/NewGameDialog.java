package com.andrewswan.powergrid.ui.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.andrewswan.powergrid.domain.Player.Colour;

/**
 * Prompts the user for the details of a new game.
 *
 * @author Andrew Swan
 */
public final class NewGameDialog extends JDialog {

    /**
     * Constructor.
     *
     * @param owner
     */
    public NewGameDialog(final Frame owner) {
        super(owner, true);
        setTitle("New Game");
        setLayout(new BorderLayout());
        add(getPromptPanel(), BorderLayout.NORTH);
        add(getInputPanel(), BorderLayout.CENTER);
        add(getButtonPanel(), BorderLayout.SOUTH);
        SwingUtils.centre(this);
        setVisible(true);
        // TODO Auto-generated constructor stub
    }
    
    private JLabel getPromptPanel() {
        final JLabel prompt = new JLabel("Enter the details of the new game:");
        prompt.setBorder(SwingUtils.getEmptyBorder(5));
        return prompt;
    }

    private Component getInputPanel() {
        final JPanel inputPanel = new JPanel();
        inputPanel.add(getPlayersTable());
        // TODO Auto-generated method stub
        return inputPanel;
    }
    
    private Component getPlayersTable() {
        final JPanel playersTable = new JPanel();
        playersTable.setBorder(BorderFactory.createTitledBorder("Players"));
        final Colour[] colours = Colour.values();
        playersTable.setLayout(new GridLayout(colours.length + 1, 2));
        // Title row
        playersTable.add(new JLabel("Colour"));
        playersTable.add(new JLabel("Player"));
        // Colour rows
        for (final Colour colour : colours) {
            playersTable.add(new JLabel(colour.name()));
            playersTable.add(new JLabel("(radio buttons)"));
        }
        return playersTable;
    }

    private Component getButtonPanel() {
        final JPanel buttonPanel = new JPanel();
        buttonPanel.add(new JButton("OK"));
        buttonPanel.add(new JButton("Cancel"));
        // TODO create and wire actions
        return buttonPanel;
    }
}
