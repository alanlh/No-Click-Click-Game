package edu.illinois.finalproject;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;

import static android.content.Context.MODE_PRIVATE;

public class ChangeUsernameDialog extends DialogFragment {
  
  SharedPreferences localData;
  View dialog;
  
  // https://developer.android.com/guide/topics/ui/dialogs.html
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    
    LayoutInflater inflater = getActivity().getLayoutInflater();
    dialog = inflater.inflate(R.layout.dialog_change_username, null);
    
    localData = this.getActivity().getSharedPreferences(AccessKeys.getAppName(), MODE_PRIVATE);
    
    EditText mChangeUsernameField = (EditText) dialog.findViewById(R.id.username_ev_change);
    mChangeUsernameField.setText(localData.getString(AccessKeys.getUsernameKey(), null));
    
    Button mConfirmChangesButton = (Button) dialog.findViewById(R.id.username_btn_confirm);
    mConfirmChangesButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        EditText mChangeUsernameField = (EditText) dialog.findViewById(R.id.username_ev_change);
        String newUsername = mChangeUsernameField.getText().toString();
  
        if (validUsername(newUsername)) {
          updateUsername(newUsername);
          ChangeUsernameDialog.this.getDialog().cancel();
        }
      }
    });
    
    Button mCancelButton = (Button) dialog.findViewById(R.id.username_btn_cancel);
    mCancelButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        ChangeUsernameDialog.this.getDialog().cancel();
      }
    });
    
    builder.setView(dialog);
    
    return builder.create();
  }
  
  /**
   * Checks whether the username given is valid (not too long, not empty).
   *
   * Can add language filtering here in the future
   *
   * @return Whether or not the new username is valid
   */
  private boolean validUsername(String newUsername) {
    // TODO: IMPLEMENT USERNAME LENGTH RESTRICTIONS
    return true;
  }
  
  /**
   * Saves the username to SharedPreferences and Firebase
   *
   * @param newUsername The new username that the user wishes to have
   */
  private void updateUsername(String newUsername){
    SharedPreferences.Editor editor = localData.edit();
    editor.putString(AccessKeys.getUsernameKey(), newUsername);
    editor.apply();
  
    String userId = localData.getString(AccessKeys.getUserFirebaseKey(), null);
    MainActivity.DATABASE.getReference(AccessKeys.getUserListRef()).child(userId)
      .child(AccessKeys.getUsernameRef()).setValue(newUsername);
  }
}
