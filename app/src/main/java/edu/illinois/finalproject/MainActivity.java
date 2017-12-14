package edu.illinois.finalproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
// Here because setUpFirebase() uses it and setUpFirebase() should be commented out

/**
 * Launcher Activity for this app. Contains methods for setting up users, and navigating to other
 * activities. Note: There are several commented out methods which should not be used.
 */
public class MainActivity extends AppCompatActivity {
  
  SharedPreferences localData;
  static final FirebaseDatabase DATABASE = FirebaseDatabase.getInstance();
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    
    localData = getSharedPreferences(AccessKeys.getAppName(), MODE_PRIVATE);
    
    // A method that deletes SharedPreferences. Used while testing, so that sharedpreferences
    // from previous versions won't affect it.
//    deleteSharedPreferenceData();
    
    // A method that sets up/deletes all the prereqs on Firebase without manually typing. I am
    // keeping keeping the code here, even though the entire thing will be commented out, for future
    // reference.
//    deleteFirebaseInfo();
//    setUpFirebase();
    
    if (isNewUser()) {
      setUpNewUser();
    }
    
    Button mPlayButton = (Button) findViewById(R.id.main_button_play);
    Button mStatsButton = (Button) findViewById(R.id.main_btn_stats);
    Button mLeaderboardButton = (Button) findViewById(R.id.main_btn_leaderboard);
    
    mPlayButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        goToPlayActivity();
      }
    });
    
    mStatsButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        goToStatsActivity();
      }
    });
    
    mLeaderboardButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        goToLeaderboardActivity();
      }
    });
  }
  
  /**
   * Checkes if the user is a new player by checking if there is any stored data. If not, then
   * the player is new.
   *
   * @return Whether or not the player is new.
   */
  private boolean isNewUser() {
    return !localData.contains(AccessKeys.getFirstRunKey());
  }
  
  /**
   * Generates a new default profile on Firebase, while storing information including the user
   * key, locally.
   */
  public void setUpNewUser() {
    DatabaseReference userListRef = DATABASE.getReference(AccessKeys.getUserListRef());
    
    UserProfile newUser = new UserProfile();
    DatabaseReference newUserRef = userListRef.push();
    newUserRef.setValue(newUser);
    String userFirebaseKey = newUserRef.getKey();
    SharedPreferences.Editor editor = localData.edit();
    
    // Actual type doesn't really matter. Only existence.
    editor.putBoolean(AccessKeys.getFirstRunKey(), true);
    
    // Saves everything within the user profile to SharedPreferences
    editor.putString(AccessKeys.getUserFirebaseKey(), userFirebaseKey);
    editor.putString(AccessKeys.getUsernameKey(), newUser.getUsername());
    editor.putLong(AccessKeys.getTotalScoreKey(), newUser.getTotalPoints());
    editor.putLong(AccessKeys.getClickCountKey(), newUser.getClickCount());
    editor.putLong(AccessKeys.getLastClickKey(), newUser.getLastClickTime());
    editor.apply();
  }
  
  /**
   * Opens the play activity.
   */
  private void goToPlayActivity() {
    final Context context = MainActivity.this;
    Intent playActivityIntent = new Intent(context, ButtonsActivity.class);
    
    context.startActivity(playActivityIntent);
  }
  
  /**
   * Opens the statistics activity
   */
  private void goToStatsActivity() {
    final Context context = MainActivity.this;
    Intent statsActivityIntent = new Intent(context, StatsActivity.class);
    
    context.startActivity(statsActivityIntent);
  }
  
  /**
   * Opens the leaderboard activity
   */
  private void goToLeaderboardActivity() {
    final Context context = MainActivity.this;
    Intent leaderboardActivityIntent = new Intent(context, LeaderboardActivity.class);
    
    context.startActivity(leaderboardActivityIntent);
  }
  
  // THE FOLLOWING METHODS WERE USED FOR TESTING PURPOSES ONLY. COMMENTED OUT AS A SECOND SAFEGUARD
  // TO PREVENT THEM FROM BEING RUN.

//  /**
//   * Initializes the database. Should never be called after the initial run, unless the database
//   * has been emptied
//   */
//  private void setUpFirebase() {
//    Date startDate = new Date();
//    long currentTime = startDate.getTime();
//
//    DatabaseReference buttonTreeRef = DATABASE.getReference(AccessKeys.getButtonListRef());
//
//    for (int i = 0; i < ButtonsActivity.NUMBER_OF_BUTTONS; i++) {
//      String buttonRefKey = AccessKeys.getButtonIRef() + String.valueOf(i);
//      DatabaseReference buttonRef = buttonTreeRef.child(buttonRefKey);
//      buttonRef.push().setValue(currentTime);
//    }
//
//    // Only used to create a child for the Users.
//    DATABASE.getReference(AccessKeys.getUserListRef());
//    setUpNewUser();
//  }

//  /**
//   * KEEP THIS COMMENTED OUT. DELETES ENTIRE FIREBASE DATABASE
//   */
//  private void deleteFirebaseInfo() {
//    DatabaseReference rootReference = DATABASE.getReference();
//    rootReference.setValue(null);
//  }

//  /**
//   * Deletes all SharedPreferences data. Should not be called in final app. Needed because
//   * SharedPreferences data is saved even when the app is updated.
//   */
//  private void deleteSharedPreferenceData() {
//    localData.edit().clear().apply();
//  }
}
