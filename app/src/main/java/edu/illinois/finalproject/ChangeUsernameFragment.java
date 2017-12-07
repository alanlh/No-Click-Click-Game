package edu.illinois.finalproject;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

public class ChangeUsernameFragment extends DialogFragment {
  
  // https://developer.android.com/guide/topics/ui/dialogs.html
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

    LayoutInflater inflater = getActivity().getLayoutInflater();
  
    View dialog = inflater.inflate(R.layout.dialog_change_username, null);
//      // Add action buttons
//      .setPositiveButton("Change Username", new DialogInterface.OnClickListener() {
//        @Override
//        public void onClick(DialogInterface dialog, int id) {
//          ChangeUsernameFragment.this.getDialog().cancel();
//        }
//      })
//      .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//        public void onClick(DialogInterface dialog, int id) {
//          ChangeUsernameFragment.this.getDialog().cancel();
//        }
//      });
    Button confirm = (Button) dialog.findViewById(R.id.confirmUsername);
    confirm.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        ChangeUsernameFragment.this.getDialog().cancel();
      }
    });
  
    builder.setView(dialog);
  
    return builder.create();
  }
}
