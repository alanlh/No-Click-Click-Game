package edu.illinois.finalproject;

import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Activity containing the user's personal statistics.
 */
public class StatsActivity extends AppCompatActivity {
  
  SharedPreferences localData;
  
  private static final String DEFAULT_USERNAME = "Guest";
  private static final long DEFAULT_POINTS_LONG = 0;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_stats);
    
    localData = getSharedPreferences(AccessKeys.getAppName(), MODE_PRIVATE);
    setTextViews();
    
    Button mChangeUsernameButton = (Button) findViewById(R.id.stats_btn_change_username_text);
    mChangeUsernameButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        DialogFragment newFragment = new ChangeUsernameDialog();
        newFragment.show(getSupportFragmentManager(), "Change Username");
      }
    });
  }
  
  /**
   * Sets all TextViews in the Stats activity. Note: Player's point values are also stored
   * locally. This way, user can still access statistics when not using internet.
   */
  private void setTextViews() {
    String username = localData.getString(AccessKeys.getUsernameKey(), DEFAULT_USERNAME);
    long totalPoints = localData.getLong(AccessKeys.getTotalScoreKey(), DEFAULT_POINTS_LONG);
    long clickCount = localData.getLong(AccessKeys.getClickCountKey(), DEFAULT_POINTS_LONG);
    double avgPoints = computeAveragePointsDouble(totalPoints, clickCount);
    // Note: Average points is stored on Firebase but not locally.
    
    TextView mUsernameTextView = (TextView) findViewById(R.id.stats_tv_username);
    TextView mTotalPointsTextView = (TextView) findViewById(R.id.stats_tv_total_points);
    TextView mClickCountTextView = (TextView) findViewById(R.id.stats_tv_click_count);
    TextView mAvgClickPointsTextView = (TextView) findViewById(R.id.stats_tv_avg_pts);
    
    mUsernameTextView.setText(username);
    mTotalPointsTextView.setText(getResources()
      .getString(R.string.total_points_value, NumberFormatter.formatNumber(totalPoints)));
    mClickCountTextView.setText(getResources()
      .getString(R.string.number_clicks_count, NumberFormatter.formatNumber(clickCount)));
    mAvgClickPointsTextView.setText(getResources()
      .getString(R.string.average_click_value, NumberFormatter.formatNumber(avgPoints)));
  }
  
  /**
   * Computes the average points using total points and click count using stored
   * SharedPreferences data. Thus does not require internet to access.
   *
   * @param totalPoints The user's total points, as stored in SharedPreferences
   * @param clickCount  The user's total click count, as stored in SharedPreferences
   * @return The average points per click. Returns 0 if no clicks yet.
   */
  static double computeAveragePointsDouble(long totalPoints, long clickCount) {
    return (clickCount == 0) ? 0 : ((double) totalPoints / (double) clickCount);
  }
  
  /**
   * Resets the username in the Statistics activity. Called after changing it in the change
   * username dialog.
   *
   * @param newUsername The user's new username.
   */
  void refreshUsernameTextView(String newUsername) {
    TextView mUsernameTextView = (TextView) findViewById(R.id.stats_tv_username);
    mUsernameTextView.setText(newUsername);
  }
}
