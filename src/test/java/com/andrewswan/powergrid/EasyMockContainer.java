/*
 * Created on 31/03/2008
 */
package com.andrewswan.powergrid;

import java.util.ArrayList;
import java.util.List;

import org.easymock.classextension.EasyMock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Description: simplifies the creation and usage of multiple EasyMocks.
 *   Supports mocks of both interfaces and classes. Not tied to JUnit.</p>
 * <p>Based on code posted to easymock@yahoogroups.com by Jeremy Hare (
 *   jeremyhare@googlemail.com). Uses the "Aggregate" pattern.</p>
 */
public class EasyMockContainer {

  // Constants
  protected static final Logger LOGGER = LoggerFactory.getLogger(EasyMockContainer.class);

  // Properties
  private final List<Object> mocks;

  /**
   * Constructor for an empty container
   */
  public EasyMockContainer() {
    mocks = new ArrayList<Object>();
  }

  /**
   * Creates a "nice" mock of the given type, adds it to this container, and
   * returns it
   *
   * @param <T> the type of mock to create
   * @param toMock the class or interface to mock
   * @return the created mock
   */
  public <T> T createNiceMock(final Class<T> toMock) {
    final T mock = EasyMock.createNiceMock(toMock);
    mocks.add(mock);
    return mock;
  }

  /**
   * Creates a strict mock of the given type, adds it to this container, and
   * returns it
   *
   * @param <T> the type of mock to create
   * @param toMock the class or interface to mock
   * @return the created mock
   */
  public <T> T createStrictMock(final Class<T> toMock) {
    final T mock = EasyMock.createStrictMock(toMock);
    mocks.add(mock);
    return mock;
  }

  /**
   * Replays the mocks in this container
   *
   * @see EasyMock#replay(Object[])
   */
  public void replay() {
    /*
     * Here we'd like to be able to simply do this:
     *
     *   EasyMock.replay(mocks.toArray());
     *
     * However if there's an error replaying a mock (e.g. expectations not set
     * correctly), the exception thrown by EasyMock doesn't tell us which mock
     * had the problem. We therefore replay the mocks one at a time. This takes
     * slightly longer, but milliseconds aren't a big issue when running tests.
     */
    for (final Object mock : mocks) {
      LOGGER.debug("Replaying " + mock);
      try {
        // This line throws an error with EasyMock CE 2.2 => use 2.2.2 or later
        EasyMock.replay(mock);
      }
      catch (final RuntimeException e) {
        // The default message doesn't tell us which mock it was
        final String message = getInformativeMessage(mock, e);
        throw new IllegalStateException(message);
      }
    }
  }

  /**
   * Returns an informative message about the given {@link Throwable}. This is
   * necessary because the exception messages thrown by {@link EasyMock} don't
   * say which mock had the error.
   *
   * @param mock can't be <code>null</code>
   * @param throwable can't be <code>null</code>
   * @return a non-<code>null</code> message
   */
  private String getInformativeMessage(final Object mock, final Throwable throwable) {
    final StringBuilder stringBuilder = new StringBuilder("Error in ");
    stringBuilder.append(mock);
    stringBuilder.append(": ");
    stringBuilder.append(throwable.getMessage());
    return stringBuilder.toString();
  }

  /**
   * Resets the mocks in this container
   *
   * @see EasyMock#reset(Object[])
   */
  public void reset() {
    // See the big comment in the replay() method
    for (final Object mock : mocks) {
      LOGGER.debug("Resetting " + mock);
      try {
        EasyMock.reset(mock);
      }
      catch (final RuntimeException e) {
        // Sadly the default message doesn't tell us which mock it was
        final String message = getInformativeMessage(mock, e);
        throw new IllegalStateException(message);
      }
    }
  }

  /**
   * Verifies the mocks in this container
   *
   * @see EasyMock#verify(Object[])
   */
  public void verify() {
    // See the big comment in the replay() method
    for (final Object mock : mocks) {
      LOGGER.debug("Verifying " + mock);
      try {
        EasyMock.verify(mock);
      }
      catch (final RuntimeException e) {
        // Sadly the default message doesn't tell us which mock it was
        final String message = getInformativeMessage(mock, e);
        throw new IllegalStateException(message);
      }
    }
  }

  /**
   * Clears the mocks from this container (equivalent to making a new container)
   */
  public void clear() {
    mocks.clear();
  }
}
