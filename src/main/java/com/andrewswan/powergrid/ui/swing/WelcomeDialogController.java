package com.andrewswan.powergrid.ui.swing;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Handles actions arising from the {@link WelcomeDialog}.
 *
 * @author Andrew Swan
 */
public class WelcomeDialogController extends WindowAdapter {
    
    private WelcomeDialog welcomeDialog;

    public void startNewGame() {
        System.out.println("Starting new game");
        final MainFrameController mainFrameController = new MainFrameController();
        this.welcomeDialog.dispose();
        new MainFrame(mainFrameController);
    }

    public void exit() {
        System.exit(0);
    }

    @Override
    public void windowClosing(WindowEvent e) {
        exit();
    }

    public void setView(final WelcomeDialog welcomeDialog) {
        this.welcomeDialog = welcomeDialog;
    }
}
