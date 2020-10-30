package com.apps.trollino.utils;

import android.content.SharedPreferences;

public class PrefUtils {
    private SharedPreferences sharedPreferences;
    private final String USER_AUTHORIZATION_KEY = "USER_AUTHORIZATION_KEY";

    public PrefUtils(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    private SharedPreferences.Editor getEditor() {
        return sharedPreferences.edit();
    }


    public boolean getIsUserAuthorization() {
        return sharedPreferences.getBoolean(USER_AUTHORIZATION_KEY, false);
    }

    public void saveIsUserAuthorization(boolean isUserAuthorization) {
        getEditor().putBoolean(USER_AUTHORIZATION_KEY, isUserAuthorization).apply();
    }
}
