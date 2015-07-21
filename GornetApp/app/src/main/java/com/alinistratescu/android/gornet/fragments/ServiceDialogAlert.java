package com.alinistratescu.android.gornet.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.alinistratescu.android.gornet.R;

/**
 * Created by Alin on 6/2/2015.
 */
public class ServiceDialogAlert extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setContentView(R.layout.pop_up_dialog);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        String message = getArguments().getString("message");
        String title = getArguments().getString("title");

        TextView tvTitleAlert = (TextView) dialog.findViewById(R.id.tvTitleAlert);
        TextView tvDescriptionAlert = (TextView) dialog.findViewById(R.id.tvDescriptionAlert);
        TextView tvCloseAlert = (TextView) dialog.findViewById(R.id.tvCloseAlert);

        tvTitleAlert.setText(title);
        tvDescriptionAlert.setText(message);

        tvCloseAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return dialog;
    }

}