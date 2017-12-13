package edu.illinois.finalproject;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Alan Hu on 12/7/2017.
 */

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {
  
  private Context context;
  private UserProfile[] topTotalScores;
  private UserProfile[] topAvgScores;
  
  private final int NUMBER_OF_LEADERBOARDS = 2;
  
  private final int LEADERBOARD_SIZE;
  
  private final int[] LEADERBOARD_TITLE_INDICES;
  private final String[] LEADERBOARD_TITLES = {"TOP TOTAL SCORES", "TOP AVERAGE SCORES"};
  private final String[] LEADERBOARD_SCORE_TYPE =
    {AccessKeys.getTotalScoreRef(), AccessKeys.getAverageScoreRef()};
  // Unfortunately hard-coded for now. Should change in future.
  
  public LeaderboardAdapter(Context context, int leaderboardSize) {
    this.context = context;
    this.LEADERBOARD_SIZE = leaderboardSize;
    topTotalScores = new UserProfile[LEADERBOARD_SIZE];
    topAvgScores = new UserProfile[LEADERBOARD_SIZE];
  
    for (int i = 0; i < LEADERBOARD_SIZE; i++) {
      topTotalScores[i] = new UserProfile();
      topAvgScores[i] = new UserProfile();
    }
    
    int indexCounter = 0;
    LEADERBOARD_TITLE_INDICES = new int[NUMBER_OF_LEADERBOARDS];
    for (int i = 0; i < NUMBER_OF_LEADERBOARDS; i++) {
      LEADERBOARD_TITLE_INDICES[i] = indexCounter;
      indexCounter += LEADERBOARD_SIZE + 1;
      Log.d("DATASNAPSHOT", String.valueOf(indexCounter));
    }
  }
  
  @Override
  public LeaderboardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View leaderboardScoreView = LayoutInflater.from(context).inflate(viewType, parent, false);
    
    return new LeaderboardAdapter.ViewHolder(leaderboardScoreView);
  }
  
  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    // For appropriate indexes, should display title instead of score.
    Log.d("DATASNAPSHOT", "On Bind View Holder " + position);
    for (int i = 0; i < LEADERBOARD_TITLE_INDICES.length; i++) {
      if (position == LEADERBOARD_TITLE_INDICES[i]) { // Must be a title
        setTitleFormat(holder, i);
        return;
      } else if (position > LEADERBOARD_TITLE_INDICES[i]
        && (i == LEADERBOARD_TITLE_INDICES.length - 1
        || position < LEADERBOARD_TITLE_INDICES[i + 1])) {
        formatTopScore(holder, position, LEADERBOARD_TITLE_INDICES[i], LEADERBOARD_SCORE_TYPE[i]);
        return;
      }
    }
  }
  
  @Override
  public int getItemViewType(int position) {
    return R.layout.leaderboard_score_row;
  }
  
  @Override
  public int getItemCount() {
    return ((LEADERBOARD_SIZE + 1) * NUMBER_OF_LEADERBOARDS);
  }
  
  void setTopTotalScores(UserProfile[] topTotalScores) {
    this.topTotalScores = topTotalScores;
  }
  
  void setTopAvgScores(UserProfile[] topAvgScores) {
    this.topAvgScores = topAvgScores;
  }
  
  private void setTitleFormat(LeaderboardAdapter.ViewHolder holder, int titleIndex) {
    holder.mRank.setText(" ");
    holder.mTitle.setText(LEADERBOARD_TITLES[titleIndex]);
//    holder.mTitle.setTextSize(R.dimen.leaderboard_title_text_size);
    holder.mTitle.setTypeface(Typeface.SERIF, Typeface.BOLD);
    holder.mValue.setText(" ");
  }
  
  private void formatTopScore(LeaderboardAdapter.ViewHolder holder, int position,
                              int titlePosition, String scoreType) {
    Log.d("DATASNAPSHOT", "Reach FormatTopScore");
    int rank = position - titlePosition;
    int rankIndex = rank - 1;
    holder.mRank.setText(String.valueOf(rank));
    
    if (scoreType.equals(AccessKeys.getTotalScoreRef())) {
      String username = topTotalScores[rankIndex].getUsername();
      String formattedScore = NumberFormatter.formatNumber(topTotalScores[rankIndex]
        .getTotalPoints());
      
      holder.mTitle.setText(username);
      holder.mValue.setText(formattedScore);
            
    } else if (scoreType.equals(AccessKeys.getAverageScoreRef())) {
      Log.d("DATASNAPSHOT", "Reach AverageScore");
  
      String username = topAvgScores[rankIndex].getUsername();
      String formattedScore = NumberFormatter.formatNumber(topAvgScores[rankIndex]
        .getAveragePoints());
  
      Log.d("DATASNAPSHOT", username + " " + formattedScore);
  
  
      holder.mTitle.setText(username);
      holder.mValue.setText(formattedScore);
    }
  }
  
  public class ViewHolder extends RecyclerView.ViewHolder {
    
    private TextView mRank;
    private TextView mTitle;
    private TextView mValue;
    
    public ViewHolder(View leaderboardRow) {
      super(leaderboardRow);
      mRank = (TextView) leaderboardRow.findViewById(R.id.leaderboard_tv_row_rank);
      mTitle = (TextView) leaderboardRow.findViewById(R.id.leaderboard_tv_row_title);
      mValue = (TextView) leaderboardRow.findViewById(R.id.leaderboard_tv_row_value);
    }
  }
}
