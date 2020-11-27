package com.apps.trollino.utils.data;

import android.content.SharedPreferences;

public class PrefUtils {
    private SharedPreferences sharedPreferences;
    private final String USER_AUTHORIZATION_KEY = "USER_AUTHORIZATION_KEY";

    private final String COOKIE_KEY = "COOKIE_KEY";
    private final String NEW_POST_CURRENT_PAGE = "NEW_POST_CURRENT_PAGE"; // запоминание текущей страницы данных с Api для постов из категории "Свежее"
    private final String POST_BY_CATEGORY_CURRENT_PAGE = "POST_BY_CATEGORY_CURRENT_PAGE"; // запоминание текущей страницы данных с Api для постов из категорий, загружаемых с Api
    private final String SELECTED_CATEGORY_ID = "SELECTED_CATEGORY_ID"; // запоминание ID выбранной категории
    private final String NEXT_POST_ID = "NEXT_POST_ID"; // запоминание ID следующего поста
    private final String PREV_POST_ID = "PREV_POST_ID"; // запоминание ID предыдущего поста

    public PrefUtils(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    private SharedPreferences.Editor getEditor() {
        return sharedPreferences.edit();
    }

    public String getCookie() {
        return sharedPreferences.getString(COOKIE_KEY, "");
    }

    public void saveCookie(String cookie) {
        getEditor().putString(COOKIE_KEY, cookie).apply();
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

    public int getPostByCategoryCurrentPage() {
        return sharedPreferences.getInt(POST_BY_CATEGORY_CURRENT_PAGE, 0);
    }

    public void savePostByCategoryCurrentPage(int currentPage) {
        getEditor().putInt(POST_BY_CATEGORY_CURRENT_PAGE, currentPage).apply();
    }

    public String getSelectedCategoryId() {
        return sharedPreferences.getString(SELECTED_CATEGORY_ID, "");
    }

    public void saveSelectedCategoryId(String categoryId) {
        getEditor().putString(SELECTED_CATEGORY_ID, categoryId).apply();
    }

    public String getNextPostId() {
        return sharedPreferences.getString(NEXT_POST_ID, "");
    }

    public void saveNextPostId(String postId) {
        getEditor().putString(NEXT_POST_ID, postId).apply();
    }

    public String gePrevPostId() {
        return sharedPreferences.getString(PREV_POST_ID, "");
    }

    public void savePrevPostId(String postId) {
        getEditor().putString(PREV_POST_ID, postId).apply();
    }
}
