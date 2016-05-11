package com.example.serg3z.photoeditor.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import com.example.serg3z.photoeditor.R;

/**
 * Created by serg3z on 11.05.16.
 */
public class DialogSelectPhoto extends DialogFragment {

    DialogFragmentListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        this.listener = (DialogFragmentListener) getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_about_title)
                .setItems(R.array.array_dialog_take_photo, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (listener != null) {
                            listener.onDialogFragmentClicked(which);
                        }
                        dialog.dismiss();
                    }
                });
        return builder.create();
    }

    public interface DialogFragmentListener {
        void onDialogFragmentClicked(int id);
    }
}
