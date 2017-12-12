package edu.illinois.finalproject;

/**
 * Created by Alan Hu on 12/9/2017.
 */

public class NumberFormatter {
  
  private static final String[] SUFFIX_ARRAY = {"", "K", "M", "B", "T"};
  // I highly doubt anyone will get a quadrillion points.
  // Stored as array for easier access
  
  private static final int SUFFIX_RATIO = 1000;
  private static final double EPSILON = 0.0001;
  
  /**
   * Reformats the number to a more concise format, especially for larger numbers. Cuts down on
   * decimal points that may occur. Converts final result to a string.
   *
   * Wrapper method: Converts the long into a double before calling the double version of the
   * same method.
   *
   * @param value The score to be formatted.
   * @return The resulting string.
   */
  public static String formatNumber(long value) {
    double valueDouble = (double) value;
    return formatNumber(valueDouble);
  }
  
  /**
   * Reformats the number to a more concise format, especially for larger numbers. Cuts down on
   * decimal points that may occur. Converts final result to a string.
   *
   * For doubles, converts into a long
   *
   * @param value The score to be formatted.
   * @return The resulting string.
   */
  public static String formatNumber(double value) {
    int suffixCounter = 0;
    if (value < SUFFIX_RATIO && value - Math.floor(value) < EPSILON) {
//      int roundedValue
      return String.valueOf(value);
    }
    return String.valueOf(value);
  
//    while (value > SUFFIX_RATIO) {
//      suffixCounter ++;
//      value /= SUFFIX_RATIO;
//    }
  }
  
  
  /**
   * Reformats the time, given in seconds, into a readable format, e.g. 25 min., and returns it as
   * a string.
   *
   * @param time the
   * @return a String displaying the time in minutes
   */
  public static String formatTime(long time) {
    return null;
  }
  
}
