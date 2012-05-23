/*
 * Created on 15/02/2008
 */
package com.andrewswan.powergrid;

import org.junit.Test;

/**
 * Unit test of the {@link Utils} class
 */
public class UtilsTest {

    @Test
    public void testNonProhibitedCallerIsAllowed() {
        // Set up
        final Child child = new Child();

        // Invoke
        child.methodSecuredAgainstParent(); // shouldn't throw an exception
    }

    @Test(expected = SecurityException.class)
    public void testProhibitedCallerIsNotAllowed() {
        // Set up a calling instance that implements the prohibited interface
        final Parent parent = new ParentImpl();

        // Invoke
        parent.callChild();
    }

    @Test
    public void testClassSecuredAgainstItself() {
        new SelfSecured().methodSecuredAgainstOwnClass(); // should be allowed
    }

    private interface Parent {
        void callChild();
    }

    private static class ParentImpl implements Parent {

        public void callChild() {
            new Child().methodSecuredAgainstParent(); // should throw an
                                                      // exception
        }
    }

    private static class Child {

        void methodSecuredAgainstParent() {
            Utils.checkNotInCallStack(Parent.class);
        }
    }

    private static class SelfSecured {

        void methodSecuredAgainstOwnClass() {
            Utils.checkNotInCallStack(SelfSecured.class);
        }
    }
}
