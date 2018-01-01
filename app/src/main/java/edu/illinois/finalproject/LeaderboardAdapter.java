package edu.illinois.finalproject;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Alan Hu on 12/7/2017.
 */

/**
 * Adapter which configures data for the leaderboard.
 */
public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {
  
  private Context context;
  private UserProfile[] topTotalScorers;
  private UserProfile[] topAvgScorers;
  
  private final int NUMBER_OF_LEADERBOARDS = 2;
  
  private final int LEADERBOARD_SIZE;
  
  // Unfortunately this is the most "systematic" method I can think of.
  private final int[] LEADERBOARD_TITLE_INDICES;
  private final String[] LEADERBOARD_TITLES = {"TOP TOTAL SCORES", "TOP AVERAGE SCORES"};
  private final String[] LEADERBOARD_SCORE_TYPE =
    {AccessKeys.getTotalScoreRef(), AccessKeys.getAverageScoreRef()};
  
  public LeaderboardAdapter(Context context, int leaderboardSize) {
    this.context = context;
    this.LEADERBOARD_SIZE = leaderboardSize;
    topTotalScorers = new UserProfile[LEADERBOARD_SIZE];
    topAvgScorers = new UserProfile[LEADERBOARD_SIZE];
    
    // Initializes with default data.
    for (int i = 0; i < LEADERBOARD_SIZE; i++) {
      topTotalScorers[i] = new UserProfile();
      topAvgScorers[i] = new UserProfile();
    }
    
    // Sets the indices so that the correct positions are for the title.
    int indexCounter = 0;
    LEADERBOARD_TITLE_INDICES = new int[NUMBER_OF_LEADERBOARDS];
    for (int i = 0; i < NUMBER_OF_LEADERBOARDS; i++) {
      LEADERBOARD_TITLE_INDICES[i] = indexCounter;
      indexCounter += LEADERBOARD_SIZE + 1;
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
    // Not a very scalable method, but works.
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
    // +1 needed to count titles.
    return ((LEADERBOARD_SIZE + 1) * NUMBER_OF_LEADERBOARDS);
  }
  
  /**
   * Called by the listeners in LeaderboardActivity once the top total scorer data has been
   * retrieved and sorted. Followed by notifyDataSetChanged() to update the adapter.
   *
   * @param topTotalScorers The array of top total scorers
   */
  void setTopTotalScorers(UserProfile[] topTotalScorers) {
    this.topTotalScorers = topTotalScorers;
  }
  
  /**
   * Called by listeners in LeaderboardActivity once the top average scorer data has been
   * retrieved and sorted. Followed by notifyDataSetChanged() to update the adapter.
   *
   * @param topAvgScorers The array of top average scorers.
   */
  void setTopAvgScorers(UserProfile[] topAvgScorers) {
    this.topAvgScorers = topAvgScorers;
  }
  
  /**
   * Formats a viewholder in the recyclerview as a title for a leaderboard. Sets bolded text.
   *
   * @param holder     The viewholder which is to be formatted
   * @param titleIndex The index of the leaderboard title to be formatted
   */
  private void setTitleFormat(LeaderboardAdapter.ViewHolder holder, int titleIndex) {
    holder.mRank.setText(" ");
    holder.mTitle.setText(LEADERBOARD_TITLES[titleIndex]);
    holder.mTitle.setTypeface(Typeface.SERIF, Typeface.BOLD);
    holder.mValue.setText(" ");
  }
  
  /**
   * Formats a viewholder in the recyclerview as a scorer in the leaderboard. Sets the username,
   * rank, and point value of the user.
   *
   * @param holder        The viewholder which is to be formatted
   * @param position      The viewholder position, used to calculate the rank
   * @param titlePosition The index of the leaderboard title to which this score belongs
   * @param scoreType     The leaderboard which this score is part of
   */
  private void formatTopScore(LeaderboardAdapter.ViewHolder holder, int position,
                              int titlePosition, String scoreType) {
    int rank = position - titlePosition;
    int rankIndex = rank - 1;
    holder.mRank.setText(String.valueOf(rank));
  
    holder.mTitle.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL);
  
    if (scoreType.equals(AccessKeys.getTotalScoreRef())) {
      String username = topTotalScorers[rankIndex].getUsername();
      String formattedScore = NumberFormatter.formatNumber(topTotalScorers[rankIndex]
        .getTotalPoints());
      
      holder.mTitle.setText(username);
      holder.mValue.setText(formattedScore);
      
    } else if (scoreType.equals(AccessKeys.getAverageScoreRef())) {
      String username = topAvgScorers[rankIndex].getUsername();
      String formattedScore = NumberFormatter.formatNumber(topAvgScorers[rankIndex]
        .getAveragePoints());
      
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
