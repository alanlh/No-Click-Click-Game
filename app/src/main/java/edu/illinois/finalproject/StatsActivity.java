package edu.illinois.finalproject;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StatsActivity extends AppCompatActivity {
  
  final SharedPreferences localData = getSharedPreferences(MainActivity.APP_NAME, MODE_PRIVATE);
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_stats);
  
    TextView mUsernameTextView = (TextView) findViewById(R.id.stats_tv_username);
    TextView mClickMessageTextView = (TextView) findViewById(R.id.stats_tv_time_since_click);
    TextView mTotalPointsTextView = (TextView) findViewById(R.id.stats_tv_total_points);
    TextView mClickCountTextView = (TextView) findViewById(R.id.stats_tv_click_count);
    TextView mAvgClickPointsTextView = (TextView) findViewById(R.id.stats_tv_avg_pts);
    Button mChangeUsernameButton = (Button) findViewById(R.id.stats_btn_change_username);
    
    
    
    mChangeUsernameButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        // TODO: Fragment allowing user to set username
      }
    });
  }
}
