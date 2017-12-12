package edu.illinois.finalproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class ButtonsActivity extends AppCompatActivity {
  
  private static final int NUMBER_OF_BUTTONS = 100;
  private final int MILLI_PER_SEC = 1000;
  private final double MILLI_TO_SEC = 0.001;
  private final int ROW_LENGTH_PORTRAIT = 2;
  private final int ROW_LENGTH_LANDSCAPE = 5;
  private final int DEFAULT_VALUE = 0;
  
  private final int TIMESTAMPS_TO_ACCESS = 1;
  
  private final ButtonAdapter buttonAdapter = new ButtonAdapter(this, this);
  
  private Thread stopwatchThread;
  
  SharedPreferences localData;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_buttons);
    
    localData = getSharedPreferences(AccessKeys.getAppName(), MODE_PRIVATE);
    
    // TODO: Get Internet time, verify consistent with currentTime, record any inaccuracies, if
    // no internet display so at the front.
    
    // Referenced: https://stackoverflow.com/questions/40587168/simple-android-grid-example-using
    // -recyclerview-with-gridlayoutmanager-like-the
    final RecyclerView buttonsRecycler = (RecyclerView) findViewById(R.id.buttons_recycler);
    buttonsRecycler.setLayoutManager(new GridLayoutManager(this, determineRowLength()));
    
    buttonsRecycler.setAdapter(buttonAdapter);
    addButtonChangeEventListeners();
  }
  
  @Override
  protected void onPause() {
    super.onPause();
    stopButtonIncrement();
  }
  
  @Override
  protected void onResume() {
    super.onResume();
    getInitialButtonInformation();
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
        .limitToLast(TIMESTAMPS_TO_ACCESS);
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
      @Override
      public void run() {
        try {
          while (!this.isInterrupted()) {
            Thread.sleep(MILLI_PER_SEC);
            buttonAdapter.incrementAllButtons();
            buttonAdapter.notifyDataSetChanged();
          }
        } catch (InterruptedException e) {
          
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
  
  void startButtonClickProcess(int position) {
    long pressTime = new Date().getTime(); // TODO: REPLACE WITH INTERNET TIME
    
    if (clickAvailable()) {
      addDateToButton(pressTime, position);
      evaluatePoints(pressTime, position);
    }
//    cannotClickMessage();
  }
  
  /**
   * Determines whether or not the player can click again by determining if there's a thirty
   * minute difference between the last click and now.
   *
   * @return whether or not the click is valid
   */
  boolean clickAvailable() {
    return true;
  }
  
  /**
   * Adds a date object to the button.
   *
   * @param time     The time stamp to be added to the button
   * @param position
   */
  public void addDateToButton(long time, int position) {
    DatabaseReference buttonRef = MainActivity.DATABASE.getReference(AccessKeys.getButtonListRef())
      .child(AccessKeys.getButtonIRef() + position);
    Date currentTime = new Date();
    buttonRef.push().setValue(currentTime);
  }
  
  long evaluatePoints(final long pressTime, int position) {
    final Query LAST_TIME_QUERY = MainActivity.DATABASE.getReference(AccessKeys.getButtonListRef())
      .child(AccessKeys.getButtonIRef() + position).orderByValue()
      .limitToLast(TIMESTAMPS_TO_ACCESS);

    LAST_TIME_QUERY.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        long lastClickTime = 0;
        for (DataSnapshot timeSnapshot : dataSnapshot.getChildren()) {
          lastClickTime = timeSnapshot.child(AccessKeys.getLastClickRef()).getValue(Long.class);
        }
      
        long timeDifference = buttonAdapter.calculateButtonTime(lastClickTime, pressTime);
        addPointsToPlayer(timeDifference, pressTime); // TODO: Change to creating a new thread
        LAST_TIME_QUERY.removeEventListener(this);
        // Only want events to trigger once.
      }
    
      @Override
      public void onCancelled(DatabaseError databaseError) {
      
      }
    });
  
  
    return buttonAdapter.calculateButtonTime(pressTime);
  }
  
  /**
   * Adds a number of points to the user, updating both the total points, click count, and last
   * pressed values in SharedPreferences and Firebase.
   *
   * @param pointValue The number of points to be added
   * @param pressTime  The time at which the user just pressed a button
   */
  private void addPointsToPlayer(long pointValue, long pressTime) {
    long currentPoints = localData.getLong(AccessKeys.getTotalScoreKey(), DEFAULT_VALUE);
    long currentClickCount = localData.getLong(AccessKeys.getClickCountKey(), DEFAULT_VALUE);
    
    currentPoints += pointValue;
    currentClickCount++;
    
    SharedPreferences.Editor editor = localData.edit();
    editor.putLong(AccessKeys.getTotalScoreKey(), currentPoints);
    editor.putLong(AccessKeys.getClickCountKey(), currentClickCount);
    editor.putLong(AccessKeys.getLastClickKey(), pressTime);
    
    DatabaseReference userPointRef
      = MainActivity.DATABASE.getReference(AccessKeys.getUserListRef());
    userPointRef.child(AccessKeys.getLastClickKey()).setValue(pressTime);
    
    userPointRef.child(AccessKeys.getTotalScoreRef()).setValue(currentPoints);
    userPointRef.child(AccessKeys.getClickCountRef()).setValue(currentClickCount);
    userPointRef.child(AccessKeys.getLastClickRef()).setValue(pressTime);
  }
}
