package com.apps.trollino.utils;

import android.content.SharedPreferences;

public class PrefUtils {
    private SharedPreferences sharedPreferences;

    public PrefUtils(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    private SharedPreferences.Editor getEditor() {
        return sharedPreferences.edit();
    }
}
