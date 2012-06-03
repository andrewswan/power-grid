package com.andrewswan.powergrid.ui.swing;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.awt.Window;

import org.junit.Test;

/**
 * Unit test of {@link SwingUtils}.
 *
 * @author Andrew Swan
 */
public class SwingUtilsTest {

    @Test
    public void testCentreWindow() {
        // Set up
        final Window mockWindow = mock(Window.class);
        
        // Invoke
        SwingUtils.centre(mockWindow);
        
        // Check
        verify(mockWindow).pack();
        verify(mockWindow).setLocationRelativeTo(null);
    }
    
    @Test
    public void testCentreNullWindow() {
        SwingUtils.centre(null);
    }
}
