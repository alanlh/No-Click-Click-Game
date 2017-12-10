package edu.illinois.finalproject;

import java.util.Date;

/**
 * Created by Alan Hu on 11/27/2017.
 */

public class UserProfile {
  
  private String username;
  private int totalPoints;
  private int clickCount;
  private long lastClickTime;
  
  private final String DEFAULT_USERNAME = "Clicker";
  private final int STARTING_POINTS = 0;
  private final int STARTING_CLICK_COUNT = 0;
  private final int DEFAULT_LAST_CLICK_TIME = 0;
  
  public UserProfile() {
    username = DEFAULT_USERNAME;
    totalPoints = STARTING_POINTS;
    clickCount = STARTING_CLICK_COUNT;
    lastClickTime = new Date(DEFAULT_LAST_CLICK_TIME).getTime();
  }
  
  public UserProfile(String username, int points, int clickCount) {
    this.username = username;
    this.totalPoints = points;
    this.clickCount = clickCount;
    lastClickTime = new Date(DEFAULT_LAST_CLICK_TIME).getTime();
  }
  
  // These methods could potentially be used in the final version, so I'm keeping it here.
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
  
  public void setUsername(String username) {
    this.username = username;
  }
  
  public void addPoints(int points) {
    this.totalPoints += points;
  }
}
