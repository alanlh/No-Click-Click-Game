package edu.illinois.finalproject;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
  
  private static final String APP_NAME = "No-Click Click Game";
  private static final String FIRST_RUN_KEY = "Obviously Not";
  
  final SharedPreferences localData = getSharedPreferences(APP_NAME, MODE_PRIVATE);
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    
    if (isNewUser()) {
      // TODO: Initialize stuff for new users
    }
    
    Button mPlayButton = (Button) findViewById(R.id.play_button_view);
    Button mStatsButton = (Button) findViewById(R.id.stats_button_view);
    Button mLeaderboardButton = (Button) findViewById(R.id.leaderboard_button_view);
    
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
  
  private boolean isNewUser() {
    return !localData.contains(FIRST_RUN_KEY);
  }
  
  private void goToPlayActivity() {
    // TODO: Create intent to Play activity
  }
  
  private void goToStatsActivity() {
    // TODO: Create intent to Stats activity
  }
  
  private void goToLeaderboardActivity() {
    // TODO: Create intent to Leaderboard Activity
  }
}
