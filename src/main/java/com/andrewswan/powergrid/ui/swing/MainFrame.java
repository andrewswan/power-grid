/**
 * 
 */
package com.andrewswan.powergrid.ui.swing;

import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.apache.commons.lang.Validate;

/**
 * The main {@link JFrame} for this Swing view
 * 
 * @author Andrew Swan
 */
public class MainFrame extends JFrame {

    class AboutAction extends AbstractAction {
        
        AboutAction() {
            super("About");
        }

        @Override
        public void actionPerformed(final ActionEvent event) {
            mainFrameController.showApplicationDetails();
        }
    }

    // Constants
    private static final int PREFERRED_HEIGHT = 400;
    private static final int PREFERRED_WIDTH = 600;
    private static final String TITLE = "Power Grid";
    
    // Fields
    private final MainFrameController mainFrameController;

    /**
     * Constructor
     * 
     * @param mainFrameController 
     */
    public MainFrame(final MainFrameController mainFrameController) {
        super(TITLE);
        Validate.notNull(mainFrameController);
        this.mainFrameController = mainFrameController;
        mainFrameController.setMainFrame(this);
        setDefaultCloseOperation(EXIT_ON_CLOSE); // kill the JVM when closed
        setJMenuBar(createMenuBar());
        setPreferredSize(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));
        SwingUtils.centre(this);
        setVisible(true);
    }

    private JMenuBar createMenuBar() {
        final JMenuBar menuBar = new JMenuBar();
        menuBar.add(getHelpMenu());
        return menuBar;
    }

    private JMenu getHelpMenu() {
        final JMenu helpMenu = new JMenu("Help");
        helpMenu.add(getAboutMenuItem());
        return helpMenu;
    }

    private JMenuItem getAboutMenuItem() {
        return new JMenuItem(new AboutAction());
    }
}
