/*
 * Created on 15/02/2008
 */
package com.andrewswan.powergrid;

import junit.framework.TestCase;

/**
 * Unit test of the {@link Utils} class
 */
public class UtilsTest extends TestCase {

    public void testNonProhibitedCallerIsAllowed() {
        // Set up
        final Child child = new Child();

        // Invoke
        child.methodSecuredAgainstParent(); // shouldn't throw an exception
    }

    public void testProhibitedCallerIsNotAllowed() {
        // Set up a calling instance that implements the prohibited interface
        final Parent parent = new ParentImpl();

        // Invoke
        try {
            parent.callChild();
            fail("Shouldn't have been able to call the child from the parent");
        }
        catch (final SecurityException expected) {
            // Success
        }
    }

    public void testClassSecuredAgainstItself() {
        new SelfSecured().methodSecuredAgainstOwnClass(); // should be allowed
    }
    
    interface Parent {
        void callChild();
    }
    
    class ParentImpl implements Parent {
        
        public void callChild() {
            new Child().methodSecuredAgainstParent(); // should throw an exception
        }
    }
    
    class Child {
        
        void methodSecuredAgainstParent() {
            Utils.checkNotInCallStack(Parent.class);
        }
    }
    
    class SelfSecured {
        
        void methodSecuredAgainstOwnClass() {
            Utils.checkNotInCallStack(SelfSecured.class);
        }
    }
}
