package edu.illinois.finalproject;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

public class ButtonsActivity extends AppCompatActivity {
  
  static final int NUMBER_OF_BUTTONS = 100;
  
  private final int ROW_LENGTH_PORTRAIT = 2;
  private final int ROW_LENGTH_LANDSCAPE = 5;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_buttons);
    
    // Referenced: https://stackoverflow.com/questions/40587168/simple-android-grid-example-using
    // -recyclerview-with-gridlayoutmanager-like-the
    final RecyclerView buttonsRecycler = (RecyclerView) findViewById(R.id.buttons_recycler);
    buttonsRecycler.setLayoutManager(new GridLayoutManager(this, determineRowLength()));
    
    long[] gameButtons = getButtonInformation();
    
    ButtonAdapter buttonAdapter = new ButtonAdapter(this, gameButtons);
    buttonsRecycler.setAdapter(buttonAdapter);
  }
  
  private long[] getButtonInformation() {
    // TODO: add listeners to buttons, specifically for onchange. For each button, get most
    // recent click
    //TODO: FIX HOW THE TIME IS STORED
    
    // Yes I know this is a magic number. Promise won't be here on final app.
    long[] buttonRecentTimeStamps = new long[100];
    for (int i = 0; i < buttonRecentTimeStamps.length; i ++) {
      buttonRecentTimeStamps[i] = 5;
    }
    
    return buttonRecentTimeStamps;
  }
  
  /**
   * Determines whether the device is in landscape or portrait mode, and adjusts the number of
   * buttons in a row.
   * @return The number of buttons in a row.
   */
  private int determineRowLength() {
    // Referenced for getting device orientation:
    // https://stackoverflow.com/questions/5112118/how-to-detect-orientation-of-android-device
    final Context context = ButtonsActivity.this;
    Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE))
      .getDefaultDisplay();
    int rotation = display.getRotation();
  
    if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) {
      return ROW_LENGTH_PORTRAIT;
    }
    return ROW_LENGTH_LANDSCAPE;
  }
}
