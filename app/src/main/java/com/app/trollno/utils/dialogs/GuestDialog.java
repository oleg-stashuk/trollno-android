package com.app.trollno.utils.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;

import com.app.trollno.R;
import com.app.trollno.ui.authorisation.LoginActivity;
import com.app.trollno.ui.authorisation.RegistrationActivity;

public class GuestDialog {

    public void showDialog(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialog_for_guest);

        ImageButton closeImageButton = dialog.findViewById(R.id.close_button_dialog_for_guest);
        Button loginButton = dialog.findViewById(R.id.login_button_dialog_for_guest);
        Button registrationButton = dialog.findViewById(R.id.registration_button_dialog_for_guest);

        closeImageButton.setOnClickListener(v -> {
            dialog.dismiss();
        });

        loginButton.setOnClickListener(v -> {
            context.startActivity(new Intent(context, LoginActivity.class));
            ((Activity) context).finish();
        });

        registrationButton.setOnClickListener(v -> {
            context.startActivity(new Intent(context, RegistrationActivity.class));
            ((Activity) context).finish();
        });

        dialog.show();
    }
}
