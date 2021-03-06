package edu.illinois.finalproject;

import java.util.Comparator;
import java.util.Date;

/**
 * A UserProfile object, which forms the template for all user objects in Firebase. Contains
 * methods for comparison.
 */
public class UserProfile {
  
  private String username;
  private int totalPoints;
  private int clickCount;
  private double averagePoints;
  private long lastClickTime;
  
  private final String DEFAULT_USERNAME = "Clicker";
  private final int STARTING_POINTS = 0;
  private final int STARTING_CLICK_COUNT = 0;
  private final int STARTING_AVERAGE_POINTS = 0;
  private final int DEFAULT_LAST_CLICK_TIME = 0;
  
  private static final int COMES_BEFORE = 1;
  private static final int COMES_AFTER = -1;
  
  public UserProfile() {
    username = DEFAULT_USERNAME;
    totalPoints = STARTING_POINTS;
    clickCount = STARTING_CLICK_COUNT;
    averagePoints = STARTING_AVERAGE_POINTS;
    lastClickTime = new Date(DEFAULT_LAST_CLICK_TIME).getTime();
  }
  
  // Saving here for possible future usage.
  public UserProfile(String username, int points, int clickCount) {
    this.username = username;
    this.totalPoints = points;
    this.clickCount = clickCount;
    this.averagePoints = StatsActivity.computeAveragePointsDouble(totalPoints, clickCount);
    lastClickTime = new Date(DEFAULT_LAST_CLICK_TIME).getTime();
  }
  
  public String getUsername() {
    return username;
  }
  
  public int getTotalPoints() {
    return totalPoints;
  }
  
  public long getLastClickTime() {
    return lastClickTime;
  }
  
  public int getClickCount() {
    return clickCount;
  }
  
  public double getAveragePoints() {
    return averagePoints;
  }
  
  // Referenced:
  // https://beginnersbook.com/2013/12/java-arraylist-of-object-sort-example-comparable-and-
  // comparator/
  /**
   * Compares two UserProfiles by total score. Allows UserProfiles to be sorted by total score.
   */
  public static Comparator<UserProfile> totalScoreCompare = new Comparator<UserProfile>() {
    @Override
    public int compare(UserProfile userProfile, UserProfile otherProfile) {
      return (userProfile.getTotalPoints() - otherProfile.getTotalPoints() > 0)
        ? COMES_AFTER : COMES_BEFORE;
    }
  };
  
  /**
   * Compares two UserProfiles by average score. Allows UserProfiles to be sorted by average score.
   */
  public static Comparator<UserProfile> averageScoreCompare = new Comparator<UserProfile>() {
    @Override
    public int compare(UserProfile userProfile, UserProfile otherProfile) {
      return (userProfile.getAveragePoints() - otherProfile.getAveragePoints() > 0)
        ? COMES_AFTER : COMES_BEFORE;
    }
  };
}
