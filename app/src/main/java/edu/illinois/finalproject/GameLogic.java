package edu.illinois.finalproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.Date;

/**
 * Collection of methods to handle when a user clicks a button.
 */
public class GameLogic {
  // Referenced for SharedPreferences static workaround:
  // https://stackoverflow.com/questions/3806051/accessing-sharedpreferences-through-static-methods
  static SharedPreferences localData;
  
  static final int TIME_BETWEEN_CLICK_MILLI = 1800000; // Should be 1800000
  private static final double MILLI_TO_SEC = 0.001;
  private static final int BUTTON_PRESS_DELAY_MILLI = 750;
  private static final int DETAILED_BUTTON_TIMESTAMP_REQUEST = 10;
  // Highly unlikely that more people press the same button at the same time.
  private static final int DEFAULT_VALUE = 0;
  static final int CLICK_AVAILABLE_STATE = -1;
  
  static final String NO_INTERNET_CONNECTION_MESSAGE =
    "Whoops. Looks like your internet isn't connected.";
  static final String POINTS_EARNED_MESSAGE = "You just earned %s points!";
  
  /**
   * Called when a button is pressed. Determines whether the button press is valid (i.e. there is
   * internet connection and thirty minutes has passed since the last button press). If so, adds
   * calls other methods to handle appropriate processes. In the future, should get internet time
   * instead of relying on user's device time.
   *
   * @param parentActivity A ButtonActivity object, in order to update status message at the
   *                       correct time
   * @param context        A ButtonActivity context object, because certain methods need it.
   * @param position       The position of the button pressed.
   * @return Whether the button press was successful.
   */
  static boolean startButtonClickProcess(final ButtonsActivity parentActivity,
                                         final Context context, final int position) {
    final long pressTime = new Date().getTime();
    
    boolean clickAvailable = (remainingTimeUntilClick(context, pressTime) == CLICK_AVAILABLE_STATE);
    
    if (clickAvailable && hasInternetConnection(context)) {
      addDateToButton(pressTime, position);
      
      // Referenced:
      // https://stackoverflow.com/questions/3072173/how-to-call-a-method-after-a-delay-in-android
      // Points aren't evaluated until a short delay, in case two people press the same button at
      // the same time. Gives time for both people to add timestamps to the button.
      final Handler handler = new Handler();
      handler.postDelayed(new Runnable() {
        @Override
        public void run() {
          evaluatePoints(parentActivity, context, pressTime, position);
        }
      }, BUTTON_PRESS_DELAY_MILLI);
    }
    return clickAvailable;
  }
  
  /**
   * Checks the device's internet connection status
   *
   * @param context The context from which the
   * @return Whether or not the device has internet
   */
  static boolean hasInternetConnection(Context context) {
    // Referenced
    // https://developer.android.com/training/monitoring-device-state/connectivity-monitoring.html
    
    ConnectivityManager connectivityManager =
      (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    
    NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
    boolean isConnected = (activeNetwork != null && activeNetwork.isConnectedOrConnecting());
    if (!isConnected) {
      Toast.makeText(context, NO_INTERNET_CONNECTION_MESSAGE, Toast.LENGTH_LONG).show();
    }
    return isConnected;
  }
  
  /**
   * Calculates the time difference milliseconds, until the button can be pressed again.
   *
   * @param context   Method context in order to access SharedPreferences data.
   * @param pressTime The time at which the user pressed the button.
   * @return a positive integer if there is remaining time, -1 if time is up.
   */
  static long remainingTimeUntilClick(Context context, long pressTime) {
    localData = context.getSharedPreferences(AccessKeys.getAppName(), Context.MODE_PRIVATE);
    long lastClickTime = localData.getLong(AccessKeys.getLastClickKey(), DEFAULT_VALUE);
    
    long remainingTime = lastClickTime + TIME_BETWEEN_CLICK_MILLI - pressTime;
    
    return (remainingTime > 0) ? remainingTime : CLICK_AVAILABLE_STATE;
  }
  
  /**
   * Adds the current time to the appropriate button.
   *
   * @param time     The time stamp to be added to the button
   * @param position The button to which the timestamp is to be added
   */
  static void addDateToButton(long time, int position) {
    DatabaseReference buttonRef = MainActivity.DATABASE.getReference(AccessKeys.getButtonListRef())
      .child(AccessKeys.getButtonIRef() + position);
    buttonRef.push().setValue(time);
  }
  
  /**
   * Evaluates the number of points the user should earn after pressing a button. Checks the most
   * recent few timestamps from the button in question, and looks for the timestamp immediately
   * preceding the user's time stamp. Calculates the time difference and adds the correct amount
   * of points to the user.
   *
   * @param pressTime The user's press time
   * @param position  The button's index
   */
  static void evaluatePoints(final ButtonsActivity parentActivity, final Context context,
                             final long pressTime, int position) {
    final Query LAST_TIME_QUERY = MainActivity.DATABASE.getReference(AccessKeys.getButtonListRef())
      .child(AccessKeys.getButtonIRef() + position).orderByValue()
      .limitToLast(DETAILED_BUTTON_TIMESTAMP_REQUEST);
    
    LAST_TIME_QUERY.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        long[] lastClickTimes = new long[(int) dataSnapshot.getChildrenCount()];
        int counter = 0;
        for (DataSnapshot timeSnapshot : dataSnapshot.getChildren()) {
          lastClickTimes[counter] = timeSnapshot.getValue(Long.class);
          counter++;
        }
        
        long lastClickTime = findPreviousClick(lastClickTimes, pressTime);
        
        long timeDifference = calculateButtonTime(lastClickTime, pressTime);
        addPointsToPlayer(parentActivity, context, timeDifference, pressTime);
        LAST_TIME_QUERY.removeEventListener(this);
        // Only want events to trigger once.
      }
      
      @Override
      public void onCancelled(DatabaseError databaseError) {
        
      }
    });
  }
  
  /**
   * Finds the lastClickTime immediately preceding the user's click time, and returns it. If the
   * player's clickTime is the first (which should almost never happen), returns the player's
   * time. (In other words, the player earns 0 points.)
   *
   * @param lastClickTimes  An array of timestamps representing the most recent few timestamps on
   *                        a button.
   * @param playerClickTime The player's click time
   * @return The timestamp immediately preceding the player's timestamp.
   */
  private static long findPreviousClick(long[] lastClickTimes, long playerClickTime) {
    Arrays.sort(lastClickTimes);
    
    for (int i = 0; i < lastClickTimes.length; i++) {
      if (lastClickTimes[i] == playerClickTime) {
        return lastClickTimes[i - 1];
      }
    }
    return lastClickTimes[0];
  }
  
  /**
   * Adds a number of points to the user, updating both the total points, click count, and last
   * pressed values in SharedPreferences and Firebase. Also displays a toast with the amount of
   * points earned.
   *
   * @param pointValue The number of points to be added
   * @param pressTime  The time at which the user just pressed a button
   */
  private static void addPointsToPlayer(ButtonsActivity parentActivity, Context context,
                                        long pointValue, long pressTime) {
    localData = context.getSharedPreferences(AccessKeys.getAppName(), Context.MODE_PRIVATE);
    
    String pointValueAsString = NumberFormatter.formatNumber(pointValue);
    Toast.makeText(context, String.format(POINTS_EARNED_MESSAGE, pointValueAsString),
      Toast.LENGTH_LONG).show();
    
    long currentPoints = localData.getLong(AccessKeys.getTotalScoreKey(), DEFAULT_VALUE);
    long currentClickCount = localData.getLong(AccessKeys.getClickCountKey(), DEFAULT_VALUE);
    
    currentPoints += pointValue;
    currentClickCount++;
    
    SharedPreferences.Editor editor = localData.edit();
    editor.putLong(AccessKeys.getTotalScoreKey(), currentPoints);
    editor.putLong(AccessKeys.getClickCountKey(), currentClickCount);
    editor.putLong(AccessKeys.getLastClickKey(), pressTime);
    editor.apply();
  
    parentActivity.setDefaultMessageStatus();
  
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
