package com.andrewswan.powergrid.ui.swing;

import org.apache.commons.lang.Validate;

public class MainFrameController {
    
    private MainFrame mainFrame;

    public void showApplicationDetails() {
        new AboutDialog(mainFrame);
    }
    
    public void setMainFrame(final MainFrame mainFrame) {
        Validate.notNull(mainFrame);
        this.mainFrame = mainFrame;
    }
}
