package edu.illinois.finalproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class LeaderboardActivity extends AppCompatActivity {
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_leaderboard);
  
    final RecyclerView leaderboardRecycler =
      (RecyclerView) findViewById(R.id.leaderboard_recyclerview);
    leaderboardRecycler.setLayoutManager(new LinearLayoutManager(this));
    
    // Yes I know these are magic numbers. These will also not be in the final project.
    long[] sampleData = new long[10];
    long[] moreSampleData = new long[10];
  
    for (int i = 0; i < sampleData.length; i++) {
      sampleData[i] = 712;
    }
  
    for (int i = 0; i < moreSampleData.length; i++) {
      moreSampleData[i] = 811;
    }
    
    LeaderboardAdapter leaderboardAdapter
      = new LeaderboardAdapter(this, sampleData, moreSampleData);
    leaderboardRecycler.setAdapter(leaderboardAdapter);
  }
}
