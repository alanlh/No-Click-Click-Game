package edu.illinois.finalproject;

/**
 * Created by Alan Hu on 12/9/2017.
 */

public class NumberFormatter {
  
  private static final int LARGE_NUMBER_LENGTH = 5; // For numbers over 1 million
  private static final int SMALL_NUMBER_LENGTH = 8; // For numbers less than 1 million
  private static final String[] SUFFIX_ARRAY = {"", "K", "M", "B", "T"};
  // I highly doubt anyone will get a quadrillion points.
  // Stored as array for easier access
  
  private static final String MINUTE_SUFFIX = " more minutes";
  private static final long MILLISEC_PER_MINUTE = 60000;
  
  private static final int SUFFIX_RATIO = 1000;
  private static final double EPSILON = 0.0001;
  
  /**
   * Reformats the number to a more concise format, especially for larger numbers. Cuts down on
   * decimal points that may occur. Converts final result to a string.
   * <p>
   * Wrapper method: Converts the long into a double before calling the double version of the
   * same method.
   *
   * @param value The score to be formatted.
   * @return The resulting string.
   */
  static String formatNumber(long value) {
    double valueDouble = (double) value;
    return formatNumber(valueDouble);
  }
  
  /**
   * Reformats the number to a more concise format, especially for larger numbers. Cuts down on
   * decimal points that may occur. Converts final result to a string.
   *
   * @param value The score to be formatted.
   * @return The resulting string.
   */
  static String formatNumber(double value) {
    int suffixCounter = 0;
    
    // Different formats for numbers less than and greater than 1 million
    if (value < SUFFIX_RATIO * SUFFIX_RATIO) {
      // Since value is a double, tests if value is an integer, in which case it rounds.
      if (Math.abs(value - Math.round(value)) < EPSILON) {
        return String.valueOf(Math.round(value));
      }
      return subString(String.valueOf(value), SMALL_NUMBER_LENGTH);
    }
    
    while (value > SUFFIX_RATIO) {
      suffixCounter++;
      value /= SUFFIX_RATIO;
    }
    String valueString = String.valueOf(value);
    return subString(valueString, LARGE_NUMBER_LENGTH) + SUFFIX_ARRAY[suffixCounter];
  }
  
  /**
   * Reformats the time, given in seconds, into a readable format, e.g. 25 min., and returns it as
   * a string.
   *
   * @param time the time to be evaluated, in milliseconds
   * @return a String displaying the time in minutes
   */
  static String formatTimeMinutes(long time) {
    String minutesLeft = String.valueOf((int)Math.ceil(
      (double) time / (double) MILLISEC_PER_MINUTE));
    return minutesLeft + MINUTE_SUFFIX;
  }
  
  /**
   * Returns a substring of a original string, starting from the beginning of the string, and
   * extending to the specified length.
   * <p>
   * I know someone has already written this before, but it requires importing stuff and that
   * seems like more work.
   *
   * @param originalString The original string
   * @param length         The length of the desired substring. If length is longer than the
   *                       original string, then just returns original string.
   */
  static String subString(String originalString, int length) {
    if (originalString.length() < length) {
      return originalString;
    }
    
    String result = "";
    for (int i = 0; i < length; i++) {
      result += originalString.charAt(i);
    }
    return result;
  }
}
