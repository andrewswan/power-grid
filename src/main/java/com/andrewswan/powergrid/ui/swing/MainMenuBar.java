package com.andrewswan.powergrid.ui.swing;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;

import org.apache.commons.lang.Validate;

/**
 * The main menu bar for the application.
 *
 * @author Andrew Swan
 */
public final class MainMenuBar extends JMenuBar {
    
    // Fields
    private final MainFrameController mainFrameController;
    
    /**
     * Constructor.
     *
     * @param mainFrameController
     */
    public MainMenuBar(final MainFrameController mainFrameController) {
        Validate.notNull(mainFrameController);
        this.mainFrameController = mainFrameController;
        add(getFileMenu());
        add(getHelpMenu());
    }
    
    private JMenu getFileMenu() {
        return new MenuBuilder("File")
            .addActions(new FileNewGameAction())
            .addSeparator()
            .addActions(new FileQuitAction())
            .build();
    }
    
    public JMenu getHelpMenu() {
        return new MenuBuilder("Help")
            .addActions(new HelpAboutAction())
            .build();
    }

    class FileNewGameAction extends KeyableAction {
        
        FileNewGameAction() {
            super("New Game", KeyEvent.VK_F2);
        }
        
        @Override
        public void actionPerformed(final ActionEvent event) {
            mainFrameController.startNewGame();
        }
    }
    
    class FileQuitAction extends KeyableAction {
        
        FileQuitAction() {
            super("Quit", KeyEvent.VK_Q, KeyEvent.META_DOWN_MASK);
        }

        @Override
        public void actionPerformed(final ActionEvent event) {
            mainFrameController.exitApplication();
        }
    }
    
    class HelpAboutAction extends KeyableAction {
        
        HelpAboutAction() {
            super("About", KeyEvent.VK_F1);
        }

        @Override
        public void actionPerformed(final ActionEvent event) {
            mainFrameController.showApplicationDetails();
        }
    }
}
