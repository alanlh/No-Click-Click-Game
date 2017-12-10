package edu.illinois.finalproject;

/**
 * Created by Alan Hu on 12/9/2017.
 * <p>
 * This class contains the keys of most SharedPreferences values and Firebase database locations.
 * A new class is used to keep track of strings more easily, avoid accidental duplicates, and
 * maintain consistency. Other widely used strings (namely, the app name) are stored here as well.
 */

public class AccessKeys {
  
  private static final String APP_NAME = "No-Click Click Game";
  
  // Keys used for ShardPreferences
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
}
