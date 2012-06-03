/**
 * 
 */
package com.andrewswan.powergrid.ui.swing;

import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.KeyStroke;

/**
 * The modal dialog initially displayed when the application starts
 * 
 * @author Andrew Swan
 */
public class WelcomeDialog extends JDialog {

    // Constants
    private static final int BORDER_SIZE = 5;
    private static final String TITLE = "Welcome";
    
    // Fields
    private final WelcomeDialogController controller;

    /**
     * Constructor
     * 
     * @param owner can't be <code>null</code>
     */
    public WelcomeDialog(final WelcomeDialogController controller) {
        super((Frame) null, TITLE, true);
        this.controller = controller;
        controller.setView(this);
        addWindowListener(controller);
        setLayout(new GridLayout(0, 2));
        addAction(new NewGameAction(), KeyEvent.VK_F2);
        addAction(new ExitAction(), KeyEvent.VK_ESCAPE);
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private void addAction(final Action action, final Integer shortcutKeyCode) {
        final WelcomeButton button = new WelcomeButton(action);
        if (shortcutKeyCode != null) {
            final String actionKey = action.toString();
            final InputMap inputMap = button.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
            inputMap.put(KeyStroke.getKeyStroke(shortcutKeyCode, 0), actionKey);
            button.getActionMap().put(actionKey, action);
        }
        add(button);
    }

    private class WelcomeButton extends JButton {
        
        private WelcomeButton(final Action action) {
            super(action);
            setBorder(SwingUtils.getEmptyBorder(BORDER_SIZE));
        }
    }
    
    private class NewGameAction extends AbstractAction {

        NewGameAction() {
            super("New Game");
        }

        @Override
        public void actionPerformed(final ActionEvent e) {
            controller.startNewGame();
        }
    }
    
    private class ExitAction extends AbstractAction {

        ExitAction() {
            super("Exit");
        }
        
        @Override
        public void actionPerformed(final ActionEvent e) {
            controller.exit();
        }
    }
}
