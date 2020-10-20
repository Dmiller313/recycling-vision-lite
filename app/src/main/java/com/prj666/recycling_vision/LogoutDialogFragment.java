package com.prj666.recycling_vision;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import com.prj666.recycling_vision.user.Login;

public class LogoutDialogFragment extends DialogFragment
{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder logoutDialog = new AlertDialog.Builder(getActivity());
            logoutDialog.setTitle("Logout")
                        .setMessage("Are you sure you want to logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Login.setUserLoginFlag(false);
                                startActivity(new Intent(getActivity(), Login.class));
                            }
                        })
                        .setNegativeButton("No", null)
                        .create();

        Dialog dialog = logoutDialog.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        return dialog;
    }
}
