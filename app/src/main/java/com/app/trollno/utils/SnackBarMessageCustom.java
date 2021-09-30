package com.app.trollno.utils;

import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class SnackBarMessageCustom {

    public static void showSnackBar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }

    public static void showSnackBarOnTheTopByBottomNavigation(View view, String message) {
        Snackbar snackbar  = Snackbar
                .make(view, message, Snackbar.LENGTH_SHORT);
        snackbar.setAnchorView(view);
        snackbar.show();
    }
}
