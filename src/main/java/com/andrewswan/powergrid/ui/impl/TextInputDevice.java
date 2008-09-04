/*
 * Created on 29/03/2008
 */
package com.andrewswan.powergrid.ui.impl;

import static com.andrewswan.powergrid.domain.ResourceMarket.Resource.COAL;
import static com.andrewswan.powergrid.domain.ResourceMarket.Resource.GARBAGE;
import static com.andrewswan.powergrid.domain.ResourceMarket.Resource.OIL;
import static com.andrewswan.powergrid.domain.ResourceMarket.Resource.URANIUM;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.andrewswan.powergrid.Utils;
import com.andrewswan.powergrid.domain.Plant;
import com.andrewswan.powergrid.domain.ResourcePool;
import com.andrewswan.powergrid.domain.Player.Colour;
import com.andrewswan.powergrid.domain.ResourceMarket.Resource;
import com.andrewswan.powergrid.domain.impl.ResourcePoolImpl;
import com.andrewswan.powergrid.ui.InputDevice;
import com.andrewswan.powergrid.ui.OutputDevice;

/**
 * A text-based {@link InputDevice}
 */
public class TextInputDevice implements InputDevice {

  // Constants
  private static final Log LOGGER = LogFactory.getLog(TextInputDevice.class);
  
  private static final String
    BID_PROMPT = "%s: how much do you bid on plant %d (minimum = %d%s): ",
    CITIES_PROMPT =
      "%s: which cities do you want to connect (one internal name per" +
      " line, case-insensitive, with a blank line to finish): ",
    INVALID_BID_MESSAGE =
      "Enter a bid of at least %d elektros (or zero to pass): ",
    INVALID_RESOURCE_MESSAGE = "Invalid resource token '%s'",
    OPTIONAL_PLANT = " (0 means none)",
    PLANTS_PROMPT =
      "%s: which plant numbers do you want to operate" +
      " (separated by spaces or commas): ",
    RESOURCES_PROMPT = "%s: what resources do you want to buy?\n" +
      "(format is [n][c|o|g|u] per resource, e.g. '1c 2o' means 1 coal and" +
      " 2 oil): ",
    SELECT_PLANT_PROMPT = "%s: which plant number do you want to auction: ";
  
  private static final char
    COAL_CHARACTER = 'c',
    GARBAGE_CHARACTER = 'g',
    OIL_CHARACTER = 'o',
    URANIUM_CHARACTER = 'u';
  
  private static final Locale LOCALE = Locale.getDefault();
  
  // The platform-specific line separator (can also get from IOUtils)
  static final String LINE_SEPARATOR = System.getProperty("line.separator");
  
  // Properties
  protected final InputStream inputStream;
  protected final OutputDevice outputDevice;
  
  /**
   * Constructor
   * 
   * @param inputStream the stream from which to read the user's input; can't be
   *   <code>null</code>
   * @param outputDevice the device for sending output to the user, such as
   *   input prompts and error messages; can't be <code>null</code>. The caller
   *   is responsible for closing it.
   */
  public TextInputDevice(InputStream inputStream, OutputDevice outputDevice) {
    Utils.checkNotNull(inputStream, outputDevice);
    this.inputStream = inputStream;
    this.outputDevice = outputDevice;
  }

  public Integer bidOnPlant(
      Colour colour, Plant plant, int minimumBid, boolean canPass)
  {
    String passOption = "";
    if (canPass) {
      passOption = ", pass = 0";
    }
    String prompt = String.format(
        BID_PROMPT, colour, plant.getNumber(), minimumBid, passOption);
    int bid = readInt(prompt);
    while (!isValidBid(bid, minimumBid, canPass)) {
      bid = readInt(String.format(INVALID_BID_MESSAGE, minimumBid));
    }
    if (bid <= 0) {
      return null;
    }
    return bid;
  }
  
  /**
   * Indicates whether the given bid is valid, based on the minimum allowed bid
   * and whether the player can pass.
   * 
   * @param bid
   * @param minimumBid
   * @param canPass
   * @return see above
   */
  private boolean isValidBid(int bid, int minimumBid, boolean canPass) {
    return (bid <= 0 && canPass) || bid >= minimumBid;
  }

  /**
   * Prompts the user for an integer, and keeps re-prompting them until they
   * enter a valid one. 
   * 
   * @return the entered number
   */
  private int readInt(String prompt) {
    outputDevice.prompt(prompt);
    StringBuilder inputBuffer = new StringBuilder();
    try {
      inputBuffer.append((char) inputStream.read());
      while (inputBuffer.indexOf(LINE_SEPARATOR) == -1) {
        inputBuffer.append((char) inputStream.read());
      }
    }
    catch (IOException ex) {
      handle(ex);
    }
    String input = StringUtils.trim(inputBuffer.toString());
    LOGGER.debug("Integer text (trimmed) = '" + input + "'");
    try {
      return Integer.parseInt(input);
    }
    catch (NumberFormatException ex) {
      // Invalid input; ask the user to try again
      outputDevice.showError("Not a valid number; please try again.");
      return readInt(prompt);
    }
  }

  public String[] getCitiesToConnect(Colour colour) {
    outputDevice.prompt(String.format(CITIES_PROMPT, colour));
    List<String> citiesToConnect = new ArrayList<String>();
    String cityName = readLine();
    while (!StringUtils.isBlank(cityName)) {
      citiesToConnect.add(cityName);
      cityName = readLine();
    }
    return citiesToConnect.toArray(new String[citiesToConnect.size()]);
  }

  /**
   * Reads a line from the input stream
   * 
   * @return a non-<code>null</code> line
   */
  private String readLine() {
    StringBuilder line = new StringBuilder();
    try {
      line.append((char) inputStream.read());
      while (line.indexOf(LINE_SEPARATOR) == -1) {
        line.append((char) inputStream.read());
      }
    }
    catch (IOException ex) {
      handle(ex);
    }
    return StringUtils.trim(line.toString());
  }

  public int[] getPlantsToOperate(Colour colour) {
    return readIntArray(String.format(PLANTS_PROMPT, colour));
  }

  /**
   * Reads an array of ints from one line of the input stream
   * 
   * @param prompt the text with which to prompt the user
   * @return a non-<code>null</code> array of ints
   */
  private int[] readIntArray(String prompt) {
    outputDevice.prompt(prompt);
    List<Integer> plantNumbers = new ArrayList<Integer>();
    for (String plantNumberString : StringUtils.split(readLine(), " ,")) {
      try {
        plantNumbers.add(Integer.valueOf(plantNumberString));
      }
      catch (NumberFormatException ex) {
        outputDevice.showError("Invalid number '" + plantNumberString + "'");
        return readIntArray(prompt);
      }
    }
    // Convert the collected Integers to ints
    int[] plantNumberArray = new int[plantNumbers.size()];
    for (int i = 0; i < plantNumbers.size(); i++) {
      plantNumberArray[i] = plantNumbers.get(i);
    }
    return plantNumberArray;
  }

  public ResourcePool getResourcesToBuy(Colour colour) {
    outputDevice.prompt(String.format(RESOURCES_PROMPT, colour));
    ResourcePool resources = new ResourcePoolImpl();
    for (String token : StringUtils.split(readLine(), ' ')) {
      // The token should be a number followed by a character indicating the
      // resource type
      try {
        Resource resource = getResourceType(token);
        int quantity = getResourceQuantity(token);
        // If we get here, the type and quantity are valid
        resources.addResource(resource, quantity);
      }
      catch (InvalidResourceTokenException ex) {
        // Show an error message
        outputDevice.showError(String.format(INVALID_RESOURCE_MESSAGE, token));
        // Re-prompt the user for their input
        return getResourcesToBuy(colour);
      }
    }
    return resources;
  }
  
  /**
   * Returns the quantity of resources indicated by the given token
   * 
   * @param token the token to parse; can't be <code>null</code>
   * @return see above
   * @throws InvalidResourceTokenException if the given token doesn't contain a
   *   valid quantity 
   */
  private int getResourceQuantity(String token)
    throws InvalidResourceTokenException
  {
    // Remove the resource indicator from the end of the token & convert to int
    try {
      return Integer.parseInt(StringUtils.chop(token));
    }
    catch (NumberFormatException ex) {
      throw new InvalidResourceTokenException();
    }
  }

  /**
   * Returns the type of resource indicated by the given input string
   * 
   * @param string can't be <code>null</code>
   * @return a non-<code>null</code> resource
   * @throws InvalidResourceException if the string doesn't contain a valid
   *   resource type indicator
   */
  private Resource getResourceType(String string)
    throws InvalidResourceTokenException
  {
    // The resource is indicated by the last character in the given string
    char resourceIndicator =
        string.toLowerCase(LOCALE).charAt(string.length() - 1);
    switch (resourceIndicator) {
      case COAL_CHARACTER:
        return COAL;
      case OIL_CHARACTER:
        return OIL;
      case GARBAGE_CHARACTER:
        return GARBAGE;
      case URANIUM_CHARACTER:
        return URANIUM;
      default:
        throw new InvalidResourceTokenException();
    }
  }

  public Integer selectPlantForAuction(
      Colour colour, Plant[] currentMarket, boolean mandatory)
  {
    Collection<Integer> validPlantNumbers = Utils.getPlantNumbers(currentMarket);
    if (validPlantNumbers.isEmpty()) {
      return null;  // No point prompting the user
    }
    String passOption = OPTIONAL_PLANT;
    if (mandatory) {
      passOption = "";
    }
    String prompt = String.format(SELECT_PLANT_PROMPT, colour, passOption);
    return readPlantNumber(prompt, validPlantNumbers, mandatory);
  }



  /**
   * Prompts the user for a plant number and keeps doing so until they either
   * enter a valid one or (if not mandatory) enter zero or less.
   * 
   * @param prompt the text with which to prompt the user
   * @param validPlantNumbers the plant numbers from which the user can choose
   * @param mandatory whether the user is required to enter a valid plant number
   * @return <code>null</code> if they opt not to enter a plant number and
   *   mandatory is <code>false</code>
   */
  private Integer readPlantNumber(
      String prompt, Collection<Integer> validPlantNumbers, boolean mandatory)
  {
    int plantNumber = readInt(prompt);
    while (!isValidPlantNumber(plantNumber, validPlantNumbers, mandatory)) {
      outputDevice.showError("You can't choose plant " + plantNumber);
      plantNumber = readInt(prompt);
    }
    if (plantNumber <= 0) {
      return null;
    }
    return plantNumber;
  }

  /**
   * Indicates whether the given plant number is a valid selection, given an
   * array of valid selections and whether the user has to choose a plant
   * 
   * @param plantNumber the plant number whose validity is being checked
   * @param validPlantNumbers a list of the valid plant numbers that can be
   *   chosen from; can't be <code>null</code>
   * @param mandatory whether it's mandatory for the user to choose a valid
   *   plant
   * @return see above
   */
  private boolean isValidPlantNumber(
      int plantNumber, Collection<Integer> validPlantNumbers, boolean mandatory)
  {
    return (plantNumber <= 0 && !mandatory)
        || validPlantNumbers.contains(plantNumber);
  }
  
  /**
   * Handles an I/O exception in the input or output streams. This
   * implementation closes those streams. Subclasses can override this method to
   * provide alternative handling.
   * 
   * @param e the exception to be handled; can't be <code>null</code>
   */
  protected void handle(IOException e) {
    try {
      throw new RuntimeException(e);
    }
    finally {
      close();
    }
  }
  
  public void close() {
    IOUtils.closeQuietly(inputStream);
  }
}

/**
 * Indicates an invalid resource token being entered by the user
 */
class InvalidResourceTokenException extends Exception {

  // Required for serliaization
  private static final long serialVersionUID = -5312491393348936612L;
}