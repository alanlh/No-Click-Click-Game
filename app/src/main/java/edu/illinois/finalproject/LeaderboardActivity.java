package edu.illinois.finalproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class LeaderboardActivity extends AppCompatActivity {
  
  private final int LEADERBOARD_SIZE = 10;
  
  LeaderboardAdapter leaderboardAdapter = new LeaderboardAdapter(this, LEADERBOARD_SIZE);
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_leaderboard);
    
    final RecyclerView leaderboardRecycler =
      (RecyclerView) findViewById(R.id.leaderboard_recyclerview);
    leaderboardRecycler.setLayoutManager(new LinearLayoutManager(this));
    
    leaderboardRecycler.setAdapter(leaderboardAdapter);
    
    if (GameLogic.hasInternetConnection(this)) {
      getTopScoreData();
      getAverageScoreData();
    }
  }
  
  private void getTopScoreData() {
    final Query topTotalScorers = MainActivity.DATABASE.getReference(AccessKeys.getUserListRef())
      .orderByChild(AccessKeys.getTotalScoreRef()).limitToFirst(LEADERBOARD_SIZE);
    // Leaderboard not updated in real time as of yet. Possible future feature.
    topTotalScorers.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        UserProfile[] topTotalScores = new UserProfile[LEADERBOARD_SIZE];
        int counter = 0;
        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
          topTotalScores[counter] = userSnapshot.getValue(UserProfile.class);
          counter++;
        }
        while (counter < LEADERBOARD_SIZE) {
          topTotalScores[counter] = new UserProfile();
          counter++;
        }
        
        Arrays.sort(topTotalScores, UserProfile.totalScoreCompare);
        
        leaderboardAdapter.setTopTotalScores(topTotalScores);
        leaderboardAdapter.notifyDataSetChanged();
        topTotalScorers.removeEventListener(this);
      }
      
      @Override
      public void onCancelled(DatabaseError databaseError) {
        
      }
    });
  }
  
  private void getAverageScoreData() {
    final Query topAverageScorers = MainActivity.DATABASE.getReference(AccessKeys.getUserListRef())
      .orderByChild(AccessKeys.getAverageScoreRef()).limitToFirst(LEADERBOARD_SIZE);
    
    topAverageScorers.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        UserProfile[] topAverageScores = new UserProfile[LEADERBOARD_SIZE];
        int counter = 0;
        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
          topAverageScores[counter] = userSnapshot.getValue(UserProfile.class);
          counter++;
        }
        while (counter < LEADERBOARD_SIZE) {
          topAverageScores[counter] = new UserProfile();
          counter++;
        }
        
        Arrays.sort(topAverageScores, UserProfile.averageScoreCompare);
        
        leaderboardAdapter.setTopAvgScores(topAverageScores);
        leaderboardAdapter.notifyDataSetChanged();
        topAverageScorers.removeEventListener(this);
      }
      
      @Override
      public void onCancelled(DatabaseError databaseError) {
        
      }
    });
  }
}
