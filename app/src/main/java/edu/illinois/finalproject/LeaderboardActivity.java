package edu.illinois.finalproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

/**
 * Activity which contains a global leaderboard of high scorers.
 */
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
  
  /**
   * Gets the top total scorer user profiles via a SingleValueListener, creating LEADERBOARD_SIZE
   * User Profile objects, regardless of the number of players. Sorts the data, and
   */
  private void getTopScoreData() {
    final Query topTotalScoreQuery = MainActivity.DATABASE.getReference(AccessKeys.getUserListRef())
      .orderByChild(AccessKeys.getTotalScoreRef()).limitToFirst(LEADERBOARD_SIZE);
    // Leaderboard not updated in real time as of yet. Possible future feature.
    topTotalScoreQuery.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        UserProfile[] topTotalScorers = new UserProfile[LEADERBOARD_SIZE];
        int counter = 0;
        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
          topTotalScorers[counter] = userSnapshot.getValue(UserProfile.class);
          counter++;
        }
        while (counter < LEADERBOARD_SIZE) {
          topTotalScorers[counter] = new UserProfile();
          counter++;
        }
        
        Arrays.sort(topTotalScorers, UserProfile.totalScoreCompare);
        
        leaderboardAdapter.setTopTotalScorers(topTotalScorers);
        leaderboardAdapter.notifyDataSetChanged();
        topTotalScoreQuery.removeEventListener(this);
      }
      
      @Override
      public void onCancelled(DatabaseError databaseError) {
        
      }
    });
  }
  
  /**
   * Gets the top average scorer user profiles via a SingleValueListener, creating LEADERBOARD_SIZE
   * User Profile objects, regardless of the number of players. Sorts the data, and
   */
  private void getAverageScoreData() {
    final Query topAverageScoreQuery = MainActivity.DATABASE
      .getReference(AccessKeys.getUserListRef()).orderByChild(AccessKeys.getAverageScoreRef())
      .limitToFirst(LEADERBOARD_SIZE);
    
    topAverageScoreQuery.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        UserProfile[] topAverageScorers = new UserProfile[LEADERBOARD_SIZE];
        int counter = 0;
        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
          topAverageScorers[counter] = userSnapshot.getValue(UserProfile.class);
          counter++;
        }
        while (counter < LEADERBOARD_SIZE) {
          topAverageScorers[counter] = new UserProfile();
          counter++;
        }
        
        Arrays.sort(topAverageScorers, UserProfile.averageScoreCompare);
        
        leaderboardAdapter.setTopAvgScorers(topAverageScorers);
        leaderboardAdapter.notifyDataSetChanged();
        topAverageScoreQuery.removeEventListener(this);
      }
      
      @Override
      public void onCancelled(DatabaseError databaseError) {
        
      }
    });
  }
}
