package edu.illinois.finalproject;

/**
 * This class contains the keys of most SharedPreferences values and Firebase database locations.
 * A new class is used to keep track of strings more easily, avoid accidental duplicates, and
 * maintain consistency. Other widely used strings (namely, the app name) are stored here as well.
 * KEY refers to a SharedPreference key.
 * REF refers to a Firebase Reference key.
 */
public class AccessKeys {
  
  private static final String APP_NAME = "No-Click Click Game";
  
  // Keys used for SharedPreferences
  private static final String FIRST_RUN_KEY = "Obviously Not";
  private static final String USER_FIREBASE_KEY = "User Firebase Key";
  private static final String USERNAME_KEY = "Username Key";
  private static final String TOTAL_SCORE_KEY = "Total Score";
  private static final String CLICK_COUNT_KEY = "Click Count";
  private static final String LAST_CLICK_KEY = "Last Click";
  
  // Keys used for Firebase
  private static final String BUTTON_LIST_REF = "Button List";
  private static final String BUTTON_I_REF = "Button ";
  private static final String USER_LIST_REF = "User List";
  private static final String TOTAL_SCORE_REF = "totalPoints";
  private static final String CLICK_COUNT_REF = "clickCount";
  private static final String LAST_CLICK_REF = "lastClickTime";
  private static final String USERNAME_REF = "username";
  private static final String AVERAGE_SCORE_REF = "averagePoints";
  
  public static String getAppName() {
    return APP_NAME;
  }
  
  public static String getFirstRunKey() {
    return FIRST_RUN_KEY;
  }
  
  public static String getUserFirebaseKey() {
    return USER_FIREBASE_KEY;
  }
  
  public static String getUsernameKey() {
    return USERNAME_KEY;
  }
  
  public static String getTotalScoreKey() {
    return TOTAL_SCORE_KEY;
  }
  
  public static String getClickCountKey() {
    return CLICK_COUNT_KEY;
  }
  
  public static String getLastClickKey() {
    return LAST_CLICK_KEY;
  }
  
  public static String getButtonListRef() {
    return BUTTON_LIST_REF;
  }
  
  /**
   * @return The string key for the iTH button
   */
  public static String getButtonIRef() {
    return BUTTON_I_REF;
  }
  
  public static String getUserListRef() {
    return USER_LIST_REF;
  }
  
  public static String getTotalScoreRef() {
    return TOTAL_SCORE_REF;
  }
  
  public static String getClickCountRef() {
    return CLICK_COUNT_REF;
  }
  
  public static String getLastClickRef() {
    return LAST_CLICK_REF;
  }
  
  public static String getUsernameRef() {
    return USERNAME_REF;
  }
  
  public static String getAverageScoreRef() {
    return AVERAGE_SCORE_REF;
  }
}
