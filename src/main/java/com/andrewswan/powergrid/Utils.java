/*
 * Created on 10/02/2008
 */
package com.andrewswan.powergrid;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.andrewswan.powergrid.domain.Plant;

public class Utils {

  // Constants
  protected static final Log LOGGER = LogFactory.getLog(Utils.class);
  
  /*
   * The index of the StackTraceElement array that contains the first indirect
   * caller, i.e. the first one on the stack before the one that called Utils.
   * The first ones on the stack are:
   * 0: java.lang.Thread
   * 1: java.lang.Thread
   * 2: com.andrewswan.powergrid.Utils
   * 3: the class that called Utils
   */
  private static final int FIRST_INDIRECT_CALLER = 4;
  
/**
   * Checks that none of the given objects are null
   * 
   * @param objects if <code>null</code>, this method does nothing
   * @throws IllegalArgumentException if any are <code>null</code>
   */
  public static void checkNotNull(Object... objects) {
    if (objects != null) {
      for (int i = 0; i < objects.length; i++) {
        if (objects[i] == null) {
          throw new IllegalArgumentException("Null object at index " + i);
        }
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
  public static int nullToZero(Integer integer) {
    if (integer == null) {
      return 0;
    }
    return integer;
  }
  
  /**
   * Checks the call stack doesn't include the given class more than the given
   * allowed number of times
   * 
   * @param prohibitedClass the class being screened for
   * @param allowedTimes the number of times the screened class is 
   * @throws SecurityException if it does
   */
  public static void checkNotInCallStack(Class<?> prohibitedClass) {
    StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
    LOGGER.debug("Checking callers x " + stackTrace.length);
    for (int i = FIRST_INDIRECT_CALLER; i < stackTrace.length; i++) {
      String callerClassName = stackTrace[i].getClassName();
      try {
        Class<?> callerClass = Class.forName(callerClassName);
        LOGGER.debug("Checking caller " + i + " of type " + callerClassName);
        if (prohibitedClass.isAssignableFrom(callerClass)) {
          throw new SecurityException(
              "This method cannot be called from a " + callerClass);
        }
      }
      catch (ClassNotFoundException ex) {
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
  public static Set<Integer> getPlantNumbers(Plant[] plants) {
    LOGGER.debug(
        "Getting plant numbers for plant array " + Arrays.toString(plants));
    Set<Integer> plantNumbers = new LinkedHashSet<Integer>(); // preserve order
    if (plants != null) {
      for (Plant plant : plants) {
        if (plant != null) {
          plantNumbers.add(plant.getNumber());
        }
      }
    }
    return plantNumbers;
  }
  
  /**
   * Constructor is private to prevent instantiation
   */
  private Utils() {
    // Empty
  }
}
