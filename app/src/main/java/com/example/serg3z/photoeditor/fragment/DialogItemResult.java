package com.example.serg3z.photoeditor.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.example.serg3z.photoeditor.R;

/**
 * Created by serg3z on 15.05.16.
 */
public class DialogItemResult extends DialogFragment {

    private static final String POSITION_KEY = "position";
    private DialogItemResultListener listener;
    private int position;

    public static DialogItemResult newIntent(int position) {
        DialogItemResult dialogItemResult = new DialogItemResult();
        Bundle arguments = new Bundle();
        arguments.putInt(POSITION_KEY, position);
        dialogItemResult.setArguments(arguments);
        return dialogItemResult;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(POSITION_KEY);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        this.listener = (DialogItemResultListener) getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_title_item_result)
                .setItems(R.array.array_dialog_item_result, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (listener != null) {
                            listener.onDialogItemResultClicked(which, position);
                        }
                        dialog.dismiss();
                    }
                });
        return builder.create();
    }

    public interface DialogItemResultListener {
        void onDialogItemResultClicked(int id, int position);
    }

}
