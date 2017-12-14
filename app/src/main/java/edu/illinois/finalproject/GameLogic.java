package edu.illinois.finalproject;

import android.content.SharedPreferences;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

/**
 * Created by Alan Hu on 12/13/2017.
 */

public class GameLogic {
  // Referenced for SharedPreferences static workaround:
  // https://stackoverflow.com/questions/3806051/accessing-sharedpreferences-through-static-methods
  
  static SharedPreferences localData = ButtonsActivity.localData;
  static final int TIME_BETWEEN_CLICK_MILLI = 1800000; // Should be 1800000
  private static final double MILLI_TO_SEC = 0.001;
  private static final int DEFAULT_VALUE = 0;
  static final int CLICK_AVAILABLE_STATE = -1;
  
  static boolean startButtonClickProcess(int position) {
    long pressTime = new Date().getTime(); // TODO: REPLACE WITH INTERNET TIME
    
    boolean clickAvailable = (remainingTimeUntilClick(pressTime) == CLICK_AVAILABLE_STATE);
    
    if (clickAvailable) {
      evaluatePoints(pressTime, position); // TODO: Create timer instead, and put date back first
      addDateToButton(pressTime, position);
    }
//    cannotClickMessage();
    
    return clickAvailable;
  }
  
  /**
   * Calculates the time difference milliseconds, until the button can be pressed again.
   *
   * @param pressTime The time at which the user pressed the button.
   * @return a postive integer if there is remaining time, -1 if time is up.
   */
  static long remainingTimeUntilClick(long pressTime) {
    long lastClickTime = localData.getLong(AccessKeys.getLastClickKey(), DEFAULT_VALUE);
    
    long remainingTime = lastClickTime + TIME_BETWEEN_CLICK_MILLI - pressTime;
    
    return (remainingTime > 0) ? remainingTime : CLICK_AVAILABLE_STATE;
  }
  
  /**
   * Adds the current time to the button.
   *
   * @param time     The time stamp to be added to the button
   * @param position The button to which the timestamp is to be added
   */
  static void addDateToButton(long time, int position) {
    DatabaseReference buttonRef = MainActivity.DATABASE.getReference(AccessKeys.getButtonListRef())
      .child(AccessKeys.getButtonIRef() + position);
    buttonRef.push().setValue(time);
  }
  
  static void evaluatePoints(final long pressTime, int position) {
    final Query LAST_TIME_QUERY = MainActivity.DATABASE.getReference(AccessKeys.getButtonListRef())
      .child(AccessKeys.getButtonIRef() + position).orderByValue().limitToLast(1);
    
    LAST_TIME_QUERY.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        long lastClickTime = 0;
        for (DataSnapshot timeSnapshot : dataSnapshot.getChildren()) {
          lastClickTime = timeSnapshot.getValue(Long.class);
        }
        
        long timeDifference = calculateButtonTime(lastClickTime, pressTime);
        addPointsToPlayer(timeDifference, pressTime); // TODO: Change to creating a new thread
        LAST_TIME_QUERY.removeEventListener(this);
        // Only want events to trigger once.
      }
      
      @Override
      public void onCancelled(DatabaseError databaseError) {
        
      }
    });
  }
  
  /**
   * Adds a number of points to the user, updating both the total points, click count, and last
   * pressed values in SharedPreferences and Firebase.
   *
   * @param pointValue The number of points to be added
   * @param pressTime  The time at which the user just pressed a button
   */
  private static void addPointsToPlayer(long pointValue, long pressTime) {
    long currentPoints = localData.getLong(AccessKeys.getTotalScoreKey(), DEFAULT_VALUE);
    long currentClickCount = localData.getLong(AccessKeys.getClickCountKey(), DEFAULT_VALUE);
    
    currentPoints += pointValue;
    currentClickCount++;
    
    SharedPreferences.Editor editor = localData.edit();
    editor.putLong(AccessKeys.getTotalScoreKey(), currentPoints);
    editor.putLong(AccessKeys.getClickCountKey(), currentClickCount);
    editor.putLong(AccessKeys.getLastClickKey(), pressTime);
    editor.apply();
    
    String userId = localData.getString(AccessKeys.getUserFirebaseKey(), null);
    DatabaseReference userPointRef
      = MainActivity.DATABASE.getReference(AccessKeys.getUserListRef()).child(userId);
    userPointRef.child(AccessKeys.getTotalScoreRef()).setValue(currentPoints);
    userPointRef.child(AccessKeys.getClickCountRef()).setValue(currentClickCount);
    userPointRef.child(AccessKeys.getLastClickRef()).setValue(pressTime);
    userPointRef.child(AccessKeys.getAverageScoreRef())
      .setValue(StatsActivity.computeAveragePointsDouble(currentPoints, currentClickCount));
  }
  
  /**
   * Calculates the value to be displayed on the button by subtracting the timestamp value from
   * the given date object. Caller can create the Date object so that if this method is called
   * many times for the same purpose, all times are standardized.
   *
   * @param timeStampValue The value given on the timestamp of the button
   * @param nowTime        The base time to be used.
   * @return The time difference between now and the time stamp, in seconds.
   */
  static long calculateButtonTime(long timeStampValue, long nowTime) {
    long timeDifferenceMilli = nowTime - timeStampValue;
    // Converts timeDifferenceMilli into seconds
    return (long) ((double) timeDifferenceMilli * MILLI_TO_SEC);
  }
  
  /**
   * Wrapper method. Creates a new Date object every time it is called
   * Should not be used when called for a set of values, as there may be a discrepancy between
   * the "now" Date objects.
   *
   * @param timeStampValue
   * @return The time difference between now and the time stamp, in seconds.
   */
  static long calculateButtonTime(long timeStampValue) {
    long now = new Date().getTime();
    return calculateButtonTime(timeStampValue, now);
  }
  
}
