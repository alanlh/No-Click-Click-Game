package edu.illinois.finalproject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

/**
 * Activity containing the core of the game. Contains methods used to run the buttons.
 */
public class ButtonsActivity extends AppCompatActivity {
  
  private static final int NUMBER_OF_BUTTONS = 100;
  private final int MILLI_PER_SEC = 1000;
  private final int ROW_LENGTH_PORTRAIT = 2;
  private final int ROW_LENGTH_LANDSCAPE = 5;
  
  private final int MOST_RECENT_TIMESTAMP_REQUEST_SIZE = 1;
  
  private final String CLICK_AVAILABLE_MESSAGE = "Click Now!";
  private final String RECENT_CLICK_FOUND = "It looks like you clicked recently. " +
    "You must wait for %s.";
  
  private final int PENDING_INTENT_CODE = 0;
  
  private final ButtonAdapter buttonAdapter = new ButtonAdapter(this, this);
  private Thread stopwatchThread;
  static SharedPreferences localData;
  // Static because used in GameLogic
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_buttons);
    
    localData = getSharedPreferences(AccessKeys.getAppName(), MODE_PRIVATE);
    
    // Referenced: https://stackoverflow.com/questions/40587168/simple-android-grid-example-using
    // -recyclerview-with-gridlayoutmanager-like-the
    final RecyclerView buttonsRecycler = (RecyclerView) findViewById(R.id.buttons_recycler);
    buttonsRecycler.setLayoutManager(new GridLayoutManager(this, determineRowLength()));
    
    buttonsRecycler.setAdapter(buttonAdapter);
    
    if (!GameLogic.hasInternetConnection(this)) {
      setErrorMessageStatus(GameLogic.NO_INTERNET_CONNECTION_MESSAGE);
    } else {
      setInitialMessageStatus();
      addButtonChangeEventListeners();
    }
  }
  
  @Override
  protected void onResume() {
    super.onResume();
    // Retrieves button information again to ensure that the information is the most updated.
    getInitialButtonInformation();
    startButtonIncrement();
  }
  
  @Override
  protected void onPause() {
    super.onPause();
    // Don't use when not needed.
    stopButtonIncrement();
  }
  
  /**
   * Sets the message to be displayed at the top of the screen, depending on the last click time.
   * If the user can click, then says so. Otherwise, states remaining time.
   */
  private void setInitialMessageStatus() {
    TextView mStatusMessage = (TextView) findViewById(R.id.buttons_tv_click_status);
    long currentTime = new Date().getTime();
    long remainingTime = GameLogic.remainingTimeUntilClick(currentTime);
    if (remainingTime == GameLogic.CLICK_AVAILABLE_STATE) {
      mStatusMessage.setText(CLICK_AVAILABLE_MESSAGE);
    } else {
      mStatusMessage.setText(String.format(RECENT_CLICK_FOUND,
        NumberFormatter.formatTimeMinutes(remainingTime)));
    }
  }
  
  /**
   * Sets a custom error message to be displayed at the top of the screen. At this point, does
   * nothing else. (In future, add a onClickListener to refresh.)
   *
   * @param message The message to be displayed.
   */
  private void setErrorMessageStatus(String message) {
    TextView mStatusMessage = (TextView) findViewById(R.id.buttons_tv_click_status);
    
    mStatusMessage.setText(message);
  }
  
  /**
   * Gets the initial set of button information using a SingleValueEvent listener for each button.
   * Gets the most recent timestamp on each button, and uses it to set the value of each button
   * in the button adapter.
   */
  private void getInitialButtonInformation() {
    DatabaseReference buttonsListRef = MainActivity.DATABASE.getReference
      (AccessKeys.getButtonListRef());
    
    for (int index = 0; index < NUMBER_OF_BUTTONS; index++) {
      DatabaseReference individualButtonRef = buttonsListRef.child(AccessKeys.getButtonIRef()
        + index);
      
      // Redeclared because used in inner class.
      final int position = index;
      // Final because used in inner class.
      final Query LAST_TIMESTAMP_QUERY = individualButtonRef.orderByValue()
        .limitToLast(MOST_RECENT_TIMESTAMP_REQUEST_SIZE);
      individualButtonRef.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
          long lastPressedTime = 0;
          
          // Should only be one.
          for (DataSnapshot timeSnapshot : dataSnapshot.getChildren()) {
            lastPressedTime = timeSnapshot.getValue(Long.class);
          }
          buttonAdapter.setButtonValue(lastPressedTime, position);
          LAST_TIMESTAMP_QUERY.removeEventListener(this);
          // Only want events to trigger once.
        }
        
        @Override
        public void onCancelled(DatabaseError databaseError) {
          // TODO: Error connecting?
        }
      });
    }
  }
  
  /**
   * Adds onClickListeners for each of the buttons. When a button is updated, notifies the
   * ButtonAdapter.
   */
  private void addButtonChangeEventListeners() {
    DatabaseReference buttonsListRef = MainActivity.DATABASE.getReference
      (AccessKeys.getButtonListRef());
    
    for (int index = 0; index < NUMBER_OF_BUTTONS; index++) {
      DatabaseReference individualButtonRef = buttonsListRef.child(AccessKeys.getButtonIRef()
        + index);
      
      // Redeclared because used in inner class.
      final int position = index;
      individualButtonRef.addChildEventListener(new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
          long lastPressedTime = dataSnapshot.getValue(Long.class);
          buttonAdapter.setButtonValue(lastPressedTime, position);
        }
        
        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
          // Should never happen
        }
        
        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
          // Might happen depending on how much I update this app, but nothing to worry about
        }
        
        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
          // Should never happen
        }
        
        @Override
        public void onCancelled(DatabaseError databaseError) {
          // TODO: FIREBASE ERROR?
        }
      });
    }
  }
  
  /**
   * Creates a new thread that updates the buttons every second and starts the new thread.
   */
  private void startButtonIncrement() {
    stopwatchThread = new Thread() {
      Runnable incrementAllButtons = new Runnable() {
        /**
         * Run on the main UI thread. Updates the button time displayed and notifies data set to
         * be changed.
         */
        @Override
        public void run() {
          buttonAdapter.incrementAllButtons();
          buttonAdapter.notifyDataSetChanged();
        }
      };
      
      /**
       * Method that is called when thread is started. Calls a new Runnable to be run on the main
       * UI thread.
       */
      @Override
      public void run() {
        try {
          while (!this.isInterrupted()) {
            Thread.sleep(MILLI_PER_SEC);
            runOnUiThread(incrementAllButtons);
          }
        } catch (InterruptedException e) {
          // TODO: ERROR MESSAGE
        }
      }
    };
    stopwatchThread.start();
  }
  
  /**
   * Stops the second thread, when the app isn't the main focus of the user.
   */
  private void stopButtonIncrement() {
    stopwatchThread.interrupt();
  }
  
  /**
   * Determines whether the device is in landscape or portrait mode, and adjusts the number of
   * buttons in a row.
   *
   * @return The number of buttons in a row.
   */
  private int determineRowLength() {
    // Referenced for getting device orientation:
    // https://stackoverflow.com/questions/5112118/how-to-detect-orientation-of-android-device
    final Context context = ButtonsActivity.this;
    Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
      .getDefaultDisplay();
    int rotation = display.getRotation();
    
    if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) {
      return ROW_LENGTH_PORTRAIT;
    }
    return ROW_LENGTH_LANDSCAPE;
  }
  
  /**
   * Starts a notification to be shown when the user is allowed to click again.
   */
  void startNotificationProcess() {
    Intent notificationIntent = new Intent(ButtonsActivity.this, ShowNotification.class);
    
    PendingIntent showNotificationIntent
      = PendingIntent.getService(getApplicationContext(), PENDING_INTENT_CODE, notificationIntent,
      PendingIntent.FLAG_CANCEL_CURRENT);
    
    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
    alarmManager.cancel(showNotificationIntent);
    alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()
      + GameLogic.TIME_BETWEEN_CLICK_MILLI, showNotificationIntent);
  }
}
