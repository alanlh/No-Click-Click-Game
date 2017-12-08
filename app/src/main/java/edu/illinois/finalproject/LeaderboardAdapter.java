package edu.illinois.finalproject;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Alan Hu on 12/7/2017.
 */

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {
  
  private Context context;
  private long[] topTotalScores;
  private long[] topAvgScores;
  
  public LeaderboardAdapter(Context context, long[] topTotalScores, long[] topAvgScores) {
    this.context = context;
    this.topTotalScores = topTotalScores;
    this.topAvgScores = topAvgScores;
  }
  
  @Override
  public LeaderboardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View leaderboardScoreView = LayoutInflater.from(context).inflate(viewType, parent, false);
  
    return new LeaderboardAdapter.ViewHolder(leaderboardScoreView);
  }
  
  @Override
  public void onBindViewHolder(LeaderboardAdapter.ViewHolder holder, int position) {
    // For some indexes, should display title instead of score.
    if (position == 0) {
      holder.mRank.setText("");
      holder.mTitle.setText("Top Total Scores");
      holder.mValue.setText("");
    } else if (position == topTotalScores.length + 1) {
      holder.mRank.setText("");
      holder.mTitle.setText("Top Average Scores");
      holder.mValue.setText("");
    } else if (position <= topTotalScores.length) {
      holder.mRank.setText(String.valueOf(position));
      holder.mTitle.setText("Username Here");
      holder.mValue.setText(String.valueOf(topTotalScores[position - 1]));
    } else {
      holder.mRank.setText(String.valueOf(position - topTotalScores.length - 1));
      holder.mTitle.setText("Username Here");
      holder.mValue.setText(String.valueOf(topAvgScores[position - topTotalScores.length - 2]));
    }
  }
  
  @Override
  public int getItemViewType(int position) {
    return R.layout.leaderboard_score_row;
  }
  
  @Override
  public int getItemCount() {
    return topAvgScores.length + topAvgScores.length + 2;
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
