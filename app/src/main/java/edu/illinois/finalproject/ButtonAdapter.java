package edu.illinois.finalproject;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Alan Hu on 12/5/2017.
 */

public class ButtonAdapter extends RecyclerView.Adapter<ButtonAdapter.ViewHolder> {
  
  // Referenced: https://stackoverflow.com/questions/40587168/simple-android-grid-example-using-
  // recyclerview-with-gridlayoutmanager-like-the
  
  private long[] buttonTimeClicked;
  
  // TODO: Use NotifyItemChanged to update individual buttons
  
  
  public ButtonAdapter(long[] buttonTimeClicked) {
    this.buttonTimeClicked = buttonTimeClicked;
  }
  
  @Override
  public int getItemViewType(int position) {
    return R.layout.activity_game_button;
  }
  
  @Override
  public ButtonAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View gameButtonView = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
    
    return new ViewHolder(gameButtonView);
  }
  
  @Override
  public void onBindViewHolder(ButtonAdapter.ViewHolder holder, int position) {
    int buttonTime = calculateButtonTime(position);
    holder.mGameButton.setText(buttonTime);
    
    holder.mGameButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        startButtonClickProcess();
      }
    });
  }
  
  @Override
  public int getItemCount() {
    // TODO: Replace?
    return buttonTimeClicked.length;
  }
  
  private int calculateButtonTime(int position) {
    // TODO:
    
    return 0;
  }
  
  private void startButtonClickProcess() {
    // TODO: D:
  }
  
  public class ViewHolder extends RecyclerView.ViewHolder {
    
    private Button mGameButton;
    
    public ViewHolder(View buttonView) {
      super(buttonView);
      mGameButton = (Button) buttonView.findViewById(R.id.game_button);
    }
  }
}
