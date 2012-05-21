/*
 * Created on 10/02/2008
 */
package com.andrewswan.powergrid;

import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.andrewswan.powergrid.domain.Plant;

public class Utils {

  // Constants
  protected static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);

  /*
   * The index of the StackTraceElement array that contains the first indirect
   * caller, i.e. the first one on the stack before the one that called Utils.
   * The first ones on the stack are:
   * 0: java.lang.Thread
   * 1: com.andrewswan.powergrid.Utils
   * 2: the class that called Utils
   */
  private static final int FIRST_INDIRECT_CALLER = 3;

/**
   * Checks that none of the given objects are null
   *
   * @param objects if <code>null</code>, this method does nothing
   * @throws IllegalArgumentException if any are <code>null</code>
   */
  public static void checkNotNull(final Object... objects) {
  	// We use a loop counter so we know which index had the null value
    for (int i = 0; i < objects.length; i++) {
      if (objects[i] == null) {
        throw new IllegalArgumentException("Null object at index " + i);
      }
    }
  }
  
  /**
   * Closes the given object
   *
   * @param closeable if <code>null</code>, this method does nothing
   */
  public static void closeQuietly(final Closeable closeable) {
  	if (closeable != null) {
  		try {
				closeable.close();
			}
			catch (IOException e) {
				throw new RuntimeException(e);
			}
  	}
  }
  
  /**
   * Checks that none of the given strings are blank
   *
   * @param strings
   * @throws IllegalArgumentException if any of them are blank
   */
  public static void checkNotBlank(final String... strings) {
  	// We use a loop counter so we know which string was blank
    for (int i = 0; i < strings.length; i++) {
			if (StringUtils.isBlank(strings[i])) {
        throw new IllegalArgumentException("Blank string at index " + i);
      }
    }
  }
  
  /**
   * Converts the given {@link Integer} to an int, treating <code>null</code> as
   * zero.
   *
   * @param integer
   * @return see above
   */
  public static int nullToZero(final Integer integer) {
    if (integer == null) {
      return 0;
    }
    return integer;
  }

  /**
   * Checks the call stack doesn't include the given class.
   *
   * @param prohibitedClass the class being screened for
   * @throws SecurityException if it does
   */
  public static void checkNotInCallStack(final Class<?> prohibitedClass) {
    final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
    LOGGER.debug("Checking callers x " + stackTrace.length);
    for (int i = FIRST_INDIRECT_CALLER; i < stackTrace.length; i++) {
      final String callerClassName = stackTrace[i].getClassName();
      try {
        final Class<?> callerClass = Class.forName(callerClassName);
        LOGGER.debug("Checking caller " + i + " of type " + callerClassName);
        if (prohibitedClass.isAssignableFrom(callerClass)) {
          throw new SecurityException(
              "This method cannot be called from a " + callerClass);
        }
      }
      catch (final ClassNotFoundException ex) {
        // If the calling class isn't on the classpath, we can safely ignore it
        LOGGER.debug("Ignoring caller " + i + " of type " + callerClassName);
      }
    }
  }

  /**
   * Converts the given array of {@link Plant}s to an array of their plant
   * numbers.
   *
   * @param plants the array of plants to convert; can be <code>null</code>; any
   *   <code>null</code> elements are ignored
   * @return a non-<code>null</code> set in the same order
   */
  public static Set<Integer> getPlantNumbers(final Plant[] plants) {
    LOGGER.debug(
        "Getting plant numbers for plant array " + Arrays.toString(plants));
    final Set<Integer> plantNumbers = new LinkedHashSet<Integer>(); // preserve order
    if (plants != null) {
      for (final Plant plant : plants) {
        if (plant != null) {
          plantNumbers.add(plant.getNumber());
        }
      }
    }
    return plantNumbers;
  }
  
  /**
   * Checks that the given condition is <code>true</code>
   * 
   * @param condition the condition to check
   * @param message the exception message if it's <code>false</code>
   * @throws IllegalArgumentException if it's <code>false</code>
   */
  public static void checkTrue(final boolean condition, final String message) {
      if (!condition) {
          throw new IllegalArgumentException(message);
      }
  }

  /**
   * Constructor is private to prevent instantiation
   */
  private Utils() {
    // Empty
  }
}
