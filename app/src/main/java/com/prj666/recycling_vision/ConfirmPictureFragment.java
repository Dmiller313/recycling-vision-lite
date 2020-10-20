package com.prj666.recycling_vision;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.io.IOException;

public class ConfirmPictureFragment extends DialogFragment {
    public interface ConfirmPictureListener{
        public void onDialogPositiveClick(DialogFragment dialog) throws IOException;
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    ConfirmPictureListener listener;

    public void sendBackResult(){
        ConfirmPictureListener listener = (ConfirmPictureListener) getTargetFragment();
        //listener.onFinishConfirmPicture();
        dismiss();
    }

    /*@Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        m
    }*/

    /*@Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(EditorInfo.IME_ACTION_DONE == actionId){
            ConfirmPictureListener listener = (ConfirmPictureListener) getActivity();
            //listener.onFinishConfirmPicture();
            dismiss();
            return true;
        }
        return false;
    }*/

    public ConfirmPictureFragment(){

    }

    public static ConfirmPictureFragment newInstance(String header){
        ConfirmPictureFragment fragment = new ConfirmPictureFragment();
        Bundle savedInstanceState = new Bundle();
        savedInstanceState.putString("title", header);
        fragment.setArguments(savedInstanceState);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            listener = (ConfirmPictureListener) context;
        } catch (Exception e){
            throw new ClassCastException(getActivity().toString() + " must implement ConfirmPictureListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getArguments().getString("title"));
        builder.setMessage("Send this photo for recognition?");
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    listener.onDialogPositiveClick(ConfirmPictureFragment.this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                /*if(dialog != null){
                    dialog.dismiss();
                }*/
                listener.onDialogNegativeClick(ConfirmPictureFragment.this);
            }
        });
        return builder.create();
    }
}
