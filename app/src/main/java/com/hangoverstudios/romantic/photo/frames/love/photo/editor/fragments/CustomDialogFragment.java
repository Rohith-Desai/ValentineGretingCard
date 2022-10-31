package com.hangoverstudios.romantic.photo.frames.love.photo.editor.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;

import com.hangoverstudios.romantic.photo.frames.love.photo.editor.R;

public class CustomDialogFragment extends DialogFragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        // Set a theme on the dialog builder constructor!
        AlertDialog.Builder builder =
                new AlertDialog.Builder( getActivity(), R.style.MyCustomTheme );

        builder
                .setTitle( "Exit Conformation" )
                .setMessage( "Are you sure, you want to exit form app." )
                .setPositiveButton( "OK" , new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().finish();
                    }
                })
        .setNegativeButton("cancle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dismiss();
            }
        });
        return builder.create();
    }

}
