package edu.illinois.finalproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
  
  static final String APP_NAME = "No-Click Click Game";
  private static final String FIRST_RUN_KEY = "Obviously Not";
  
  final SharedPreferences localData = getSharedPreferences(APP_NAME, MODE_PRIVATE);
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    
    if (isNewUser()) {
      // TODO: Initialize stuff for new users
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
  
  private boolean isNewUser() {
    return !localData.contains(FIRST_RUN_KEY);
  }
  
  
  private void goToPlayActivity() {
    final Context context = MainActivity.this;
    Intent statsActivityIntent = new Intent(context, ButtonsActivity.class);
    
    context.startActivity(statsActivityIntent);
  }
  
  private void goToStatsActivity() {
    final Context context = MainActivity.this;
    Intent statsActivityIntent = new Intent(context, StatsActivity.class);
  
    context.startActivity(statsActivityIntent);
  }
  
  private void goToLeaderboardActivity() {
    final Context context = MainActivity.this;
    Intent statsActivityIntent = new Intent(context, LeaderboardActivity.class);
  
    context.startActivity(statsActivityIntent);
  }
}
