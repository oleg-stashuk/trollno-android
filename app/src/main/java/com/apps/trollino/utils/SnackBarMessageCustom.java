package com.apps.trollino.utils;

import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.snackbar.Snackbar;

public class SnackBarMessageCustom {

    public static void showSnackBar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }

    public static void showSnackBarOnTheTop(View view, String message) {
        Snackbar snack = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        View viewSnackbar = snack.getView();
        FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)viewSnackbar.getLayoutParams();
        params.gravity = Gravity.TOP;
        viewSnackbar.setLayoutParams(params);
        snack.show();
    }
}
