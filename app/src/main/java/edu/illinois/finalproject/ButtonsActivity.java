package edu.illinois.finalproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

public class ButtonsActivity extends AppCompatActivity {
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_buttons);
    
    final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.buttons_recycler);
    
    //TODO: FIX THIS
    long[] gameButtons = getButtonInformation();
    
    ButtonAdapter buttonAdapter = new ButtonAdapter(gameButtons);
    recyclerView.setAdapter(buttonAdapter);
    
  }
  
  private long[] getButtonInformation() {
    // TODO: add listeners to buttons, specifically for onchange. For each button, get most
    // recent click
    //TODO: FIX HOW THE TIME IS STORED
    return null;
  }
}
