package edu.illinois.finalproject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Date;

/**
 * Created by Alan Hu on 12/5/2017.
 */

public class ButtonAdapter extends RecyclerView.Adapter<ButtonAdapter.ViewHolder> {
  
  // Referenced: https://stackoverflow.com/questions/40587168/simple-android-grid-example-using-
  // recyclerview-with-gridlayoutmanager-like-the
  
  private Context context;
  private ButtonsActivity parentActivity;
  private long[] currentButtonValue;
  
  private final int NUMBER_OF_BUTTONS = 100;
  
  
  public ButtonAdapter(Context context, ButtonsActivity parent) {
    this.context = context;
    this.parentActivity = parent;
    currentButtonValue = new long[NUMBER_OF_BUTTONS];
    // Actual values are created after the childlisteners in ButtonsActivity return a value.
    for (int index = 0; index < NUMBER_OF_BUTTONS; index++) {
      currentButtonValue[index] = 0;
    }
  }
  
  @Override
  public int getItemViewType(int position) {
    return R.layout.activity_game_button;
  }
  
  @Override
  public ButtonAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View gameButtonView = LayoutInflater.from(context).inflate(viewType, parent, false);
    
    return new ViewHolder(gameButtonView);
  }
  
  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    // TODO: Replace with something that also sets color/font
    final int index = position;
    
    holder.mGameButton.setText(NumberFormatter.formatNumber(currentButtonValue[position]));
    holder.mGameButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (GameLogic.startButtonClickProcess(index)) {
          parentActivity.startNotificationProcess();
        }
      }
    });
  }
  
  @Override
  public int getItemCount() {
    return currentButtonValue.length;
  }
  
  /**
   * Sets a certain button value to the appropriate time
   *
   * @param timeStamp The most recent time stamp on the button determined by the position
   * @param position  The position for which the data has been retrieved
   */
  void setButtonValue(long timeStamp, int position) {
    currentButtonValue[position] = GameLogic.calculateButtonTime(timeStamp);
    notifyItemChanged(position);
  }
  
  void incrementAllButtons() {
    for (int i = 0; i < NUMBER_OF_BUTTONS; i++) {
      currentButtonValue[i]++;
    }
  }
  
  public class ViewHolder extends RecyclerView.ViewHolder {
    
    private Button mGameButton;
    
    public ViewHolder(View buttonView) {
      super(buttonView);
      mGameButton = (Button) buttonView.findViewById(R.id.game_button);
    }
  }
}
