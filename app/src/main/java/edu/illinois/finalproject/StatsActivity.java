package edu.illinois.finalproject;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StatsActivity extends AppCompatActivity {
  
  SharedPreferences localData;
  private static final String USERNAME_STORE_KEY = "username";
  private static final String DEFAULT_USERNAME = "Guest";
  
  private static final String TOTAL_POINTS_STORE_KEY = "Total Points";
  private static final String CLICK_COUNT_STORE_KEY = "Click Count";
  private static final String AVG_POINTS_STORE_KEY = "Average Points";
  private static final long DEFAULT_POINTS = 0;
  
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_stats);
  
    localData = getSharedPreferences(MainActivity.APP_NAME, MODE_PRIVATE);
    
    setTextViews();
  
    Button mChangeUsernameButton = (Button) findViewById(R.id.stats_btn_change_username);
    mChangeUsernameButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        // TODO: Fragment allowing user to set username
      }
    });
  }
  
  /**
   * Sets all TextViews in the Stats activity. Note: Player's point values are also stored
   * locally. This way, user can still access statistics when not using internet.
   */
  private void setTextViews() {
    // Sets username
    String username = localData.getString(USERNAME_STORE_KEY, DEFAULT_USERNAME);
    TextView mUsernameTextView = (TextView) findViewById(R.id.stats_tv_username);
    mUsernameTextView.setText(username);
    
    // Stored as a string so can later change to, for example, 576M instead of 576830128.
    long totalPoints = localData.getLong(TOTAL_POINTS_STORE_KEY, DEFAULT_POINTS);
    TextView mTotalPointsTextView = (TextView) findViewById(R.id.stats_tv_total_points);
    mTotalPointsTextView.setText(getResources()
      .getString(R.string.total_points_value, formatValues(totalPoints)));
  
    long clickCount = localData.getLong(CLICK_COUNT_STORE_KEY, DEFAULT_POINTS);
    TextView mClickCountTextView = (TextView) findViewById(R.id.stats_tv_click_count);
    mClickCountTextView.setText(getResources()
      .getString(R.string.number_clicks_count, formatValues(clickCount)));
  
    long avgPoints = localData.getLong(AVG_POINTS_STORE_KEY, DEFAULT_POINTS);
    TextView mAvgClickPointsTextView = (TextView) findViewById(R.id.stats_tv_avg_pts);
    mAvgClickPointsTextView.setText(getResources()
      .getString(R.string.average_click_value, formatValues(avgPoints)));
  }
  
  /**
   * Reformats the number to a more concise format, especially for larger numbers. Cuts down on
   * decimal points that may occur. Converts final result to a string.
   *
   * // TODO: IMPLEMENT METHOD
   *
   * @param score The score to be formatted.
   * @return The resulting string.
   */
  private String formatValues(long score) {
    return String.valueOf(score);
  }
}
