package com.apps.trollino.utils;

import android.content.SharedPreferences;

public class PrefUtils {
    private SharedPreferences sharedPreferences;
    private final String USER_AUTHORIZATION_KEY = "USER_AUTHORIZATION_KEY";

    private final String COOKIE_KEY = "COOKIE_KEY";
    private final String NEW_POST_CURRENT_PAGE = "NEW_POST_CURRENT_PAGE"; // запоминание текущей страницы данных с Api для постов из категории "Свежее"

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

    public int getNewPostCurrentPage() {
        return sharedPreferences.getInt(NEW_POST_CURRENT_PAGE, 0);
    }

    public void saveNewPostCurrentPage(int currentPage) {
        getEditor().putInt(NEW_POST_CURRENT_PAGE, currentPage).apply();
    }

    public String getCookie() {
        return sharedPreferences.getString(COOKIE_KEY, "");
    }

    public void saveCookie(String cookie) {
        getEditor().putString(COOKIE_KEY, cookie).apply();
    }
}
