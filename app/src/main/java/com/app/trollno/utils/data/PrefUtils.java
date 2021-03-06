package com.app.trollno.utils.data;

import android.content.SharedPreferences;

public class PrefUtils {
    private SharedPreferences sharedPreferences;

    private final String TOKEN_KEY = "TOKEN_KEY";
    private final String LOGOUT_TOKEN_KEY = "LOGOUT_TOKEN_KEY";
    private final String USER_UID = "USER_UID";
    private final String COOKIE_KEY = "COOKIE_KEY";
    private final String USER_PASSWORD = "USER_PASSWORD";
    private final String USER_AUTHORIZATION_KEY = "USER_AUTHORIZATION_KEY";
    private final String SWITCH_SHOW_READ_POST_KEY = "SWITCH_SHOW_READ_POST_KEY";
    private final String SWITCH_PUSH_ANSWER_TO_COMMENT = "SWITCH_PUSH_ANSWER_TO_COMMENT";
    private final String IS_USER_LOGIN_BY_FACEBOOK = "IS_USER_LOGIN_BY_FACEBOOK";
    private final String IS_USER_ADD_REAL_NAME = "IS_USER_ADD_REAL_NAME";

    private final String COUNT_BETWEEN_ADS = "COUNT_BETWEEN_ADS";
    private final String AD_MOB_ID = "AD_MOB_ID";
    private final String BANNER_ID = "BANNER_ID";
    private final String YOUTUBE_API_KEY = "YOUTUBE_API_KEY";

    private final String CURRENT_PAGE = "NEW_POST_CURRENT_PAGE"; // запоминание текущей страницы данных с Api для постов из категории "Свежее"
    private final String PREV_POST_ID = "PREV_POST_ID"; // запоминание ID следующего поста
    private final String NEXT_POST_ID = "NEXT_POST_ID"; // запоминание ID предыдущего поста
    private final String IS_FAVORITE = "IS_FAVORITE"; // запоминание текущий пост в Избранном или нет

    private final String ANSWER_COMMENT_ID = "ANSWER_COMMENT_ID";
    private final String ANSWER_TO_USER_NAME = "ANSWER_TO_USER_NAME";

    private final String CURRENT_ADAPTER_POSITION_COMMENT = "CURRENT_ADAPTER_POSITION_COMMENT";
    private final String CURRENT_ADAPTER_POSITION_ANSWERS = "CURRENT_ADAPTER_POSITION_ANSWERS";
    private final String CURRENT_ADAPTER_POSITION_FAVORITE = "CURRENT_ADAPTER_POSITION_FAVORITE";
    private final String CURRENT_ADAPTER_POSITION_POSTS = "CURRENT_ADAPTER_POSITION_POSTS";

    private final String SELECTED_POST_ID = "SELECTED_POST_ID";
    private final String POST_FROM_CATEGORY_LIST = "POST_FROM_CATEGORY_LIST";
    private final String SELECTED_CATEGORY_POSITION = "SELECTED_CATEGORY_POSITION";

    private final String COMMENT_ID_FROM_ACTIVITY = "COMMENT_ID_FROM_ACTIVITY";

    private final String CURRENT_ACTIVITY = "CURRENT_ACTIVITY";

    private final String WIDTH_FOR_IMAGE = "WIDTH_FOR_IMAGE";
    private final String WIDTH_FOR_IMAGE_ONE_COLUMN = "WIDTH_FOR_IMAGE_ONE_COLUMN";

    public PrefUtils(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    private SharedPreferences.Editor getEditor() {
        return sharedPreferences.edit();
    }

    public String getToken() {
        return sharedPreferences.getString(TOKEN_KEY, "");
    }

    public void saveToken(String token) {
        getEditor().putString(TOKEN_KEY, token).apply();
    }

    public String getLogoutToken() {
        return sharedPreferences.getString(LOGOUT_TOKEN_KEY, "");
    }

    public void saveLogoutToken(String logoutToken) {
        getEditor().putString(LOGOUT_TOKEN_KEY, logoutToken).apply();
    }

    public String getCurrentActivity() {
        return sharedPreferences.getString(CURRENT_ACTIVITY, "");
    }

    public void saveCurrentActivity(String currentActivity) {
        getEditor().putString(CURRENT_ACTIVITY, currentActivity).apply();
    }

    public int getCountBetweenAds() {
        return sharedPreferences.getInt(COUNT_BETWEEN_ADS, 0);
    }

    public void saveCountBetweenAds(int countBetweenAds) {
        getEditor().putInt(COUNT_BETWEEN_ADS, countBetweenAds).apply();
    }

    public String getAdMobId() {
        return sharedPreferences.getString(AD_MOB_ID, "");
    }

    public void saveAdMobId(String adMobId) {
        getEditor().putString(AD_MOB_ID, adMobId).apply();
    }

    public String getBannerId() {
        return sharedPreferences.getString(BANNER_ID, "");
    }

    public void saveBannerId(String bannerId) {
        getEditor().putString(BANNER_ID, bannerId).apply();
    }

    public String getYoutubeId() {
        return sharedPreferences.getString(YOUTUBE_API_KEY, "");
    }

    public void saveYoutubeId(String youtubeId) {
        getEditor().putString(YOUTUBE_API_KEY, youtubeId).apply();
    }

    public String getUserUid() {
        return sharedPreferences.getString(USER_UID, "");
    }

    public void saveUserUid(String userUid) {
        getEditor().putString(USER_UID, userUid).apply();
    }

    public String getCookie() {
        return sharedPreferences.getString(COOKIE_KEY, "");
    }

    public void saveCookie(String cookie) {
        getEditor().putString(COOKIE_KEY, cookie).apply();
    }

    public String getPassword() {
        return sharedPreferences.getString(USER_PASSWORD, "");
    }

    public void savePassword(String password) {
        getEditor().putString(USER_PASSWORD, password).apply();
    }

    public boolean getIsUserAuthorization() {
        return sharedPreferences.getBoolean(USER_AUTHORIZATION_KEY, false);
    }

    public void saveIsUserAuthorization(boolean isUserAuthorization) {
        getEditor().putBoolean(USER_AUTHORIZATION_KEY, isUserAuthorization).apply();
    }

    public boolean isShowReadPost() {
        return sharedPreferences.getBoolean(SWITCH_SHOW_READ_POST_KEY, true);
    }

    public void saveIsShowReadPost(boolean showReadPost) {
        getEditor().putBoolean(SWITCH_SHOW_READ_POST_KEY, showReadPost).apply();
    }

    public boolean isSendPushAboutAnswerToComment() {
        return sharedPreferences.getBoolean(SWITCH_PUSH_ANSWER_TO_COMMENT, true);
    }

    public void saveIsSendPushAboutAnswerToComment(boolean sendPushAboutAnswerToComment) {
        getEditor().putBoolean(SWITCH_PUSH_ANSWER_TO_COMMENT, sendPushAboutAnswerToComment).apply();
    }

    public boolean isUserLoginByFacebook() {
        return sharedPreferences.getBoolean(IS_USER_LOGIN_BY_FACEBOOK, false);
    }

    public void saveIsUserLoginByFacebook(boolean isUserLoginByFacebook) {
        getEditor().putBoolean(IS_USER_LOGIN_BY_FACEBOOK, isUserLoginByFacebook).apply();
    }

    public boolean isUserEnterRealName() {
        return sharedPreferences.getBoolean(IS_USER_ADD_REAL_NAME, false);
    }

    public void saveIsUserEnterRealName(boolean isEnterRealName) {
        getEditor().putBoolean(IS_USER_ADD_REAL_NAME, isEnterRealName).apply();
    }

    public int getCurrentPage() {
        return sharedPreferences.getInt(CURRENT_PAGE, 0);
    }

    public void saveCurrentPage(int currentPage) {
        getEditor().putInt(CURRENT_PAGE, currentPage).apply();
    }

    public String getPrevPostId() {
        return sharedPreferences.getString(PREV_POST_ID, "");
    }

    public void savePrevPostId(String postId) {
        getEditor().putString(PREV_POST_ID, postId).apply();
    }

    public String getNextPostId() {
        return sharedPreferences.getString(NEXT_POST_ID, "");
    }

    public void saveNextPostId(String postId) {
        getEditor().putString(NEXT_POST_ID, postId).apply();
    }

    public boolean getIsFavorite() {
        return sharedPreferences.getBoolean(IS_FAVORITE, false);
    }

    public void saveIsFavorite(boolean isFavorite) {
        getEditor().putBoolean(IS_FAVORITE, isFavorite).apply();
    }

    public String getCommentIdToAnswer() {
        return sharedPreferences.getString(ANSWER_COMMENT_ID, "");
    }

    public void saveCommentIdToAnswer(String commentId) {
        getEditor().putString(ANSWER_COMMENT_ID, commentId).apply();
    }

    public String getAnswerToUserName() {
        return sharedPreferences.getString(ANSWER_TO_USER_NAME, "");
    }

    public void saveCurrentAdapterPositionComment(int position) {
        getEditor().putInt(CURRENT_ADAPTER_POSITION_COMMENT, position).apply();
    }

    public int getCurrentAdapterPositionComment() {
        return sharedPreferences.getInt(CURRENT_ADAPTER_POSITION_COMMENT, 0);
    }

    public void saveCurrentAdapterPositionAnswers(int position) {
        getEditor().putInt(CURRENT_ADAPTER_POSITION_ANSWERS, position).apply();
    }

    public int getCurrentAdapterPositionAnswers() {
        return sharedPreferences.getInt(CURRENT_ADAPTER_POSITION_ANSWERS, 0);
    }

    public void saveCurrentAdapterPositionFavorite(int position) {
        getEditor().putInt(CURRENT_ADAPTER_POSITION_FAVORITE, position).apply();
    }

    public int getCurrentAdapterPositionFavorite() {
        return sharedPreferences.getInt(CURRENT_ADAPTER_POSITION_FAVORITE, 0);
    }

    public void saveCurrentAdapterPositionPosts(int position) {
        getEditor().putInt(CURRENT_ADAPTER_POSITION_POSTS, position).apply();
    }

    public int getCurrentAdapterPositionPosts() {
        return sharedPreferences.getInt(CURRENT_ADAPTER_POSITION_POSTS, 0);
    }

    public void saveAnswerToUserName(String userName) {
        getEditor().putString(ANSWER_TO_USER_NAME, userName).apply();
    }

    public String getCurrentPostId() {
        return sharedPreferences.getString(SELECTED_POST_ID, "");
    }

    public void saveCurrentPostId(String postId) {
        getEditor().putString(SELECTED_POST_ID, postId).apply();
    }

    public boolean isPostFromCategoryList() {
        return sharedPreferences.getBoolean(POST_FROM_CATEGORY_LIST, false);
    }

    public void saveValuePostFromCategoryList(boolean postFromCategoryList) {
        getEditor().putBoolean(POST_FROM_CATEGORY_LIST, postFromCategoryList).apply();
    }

    public int getSelectedCategoryPosition() {
        return sharedPreferences.getInt(SELECTED_CATEGORY_POSITION, 0);
    }

    public void saveSelectedCategoryPosition(int categoryPosition) {
        getEditor().putInt(SELECTED_CATEGORY_POSITION, categoryPosition).apply();
    }

    public String getCommentIdForActivity() {
        return sharedPreferences.getString(COMMENT_ID_FROM_ACTIVITY, "");
    }

    public void saveCommentIdForActivity(String commentId) {
        getEditor().putString(COMMENT_ID_FROM_ACTIVITY, commentId).apply();
    }

    public int getImageWidth() {
        return sharedPreferences.getInt(WIDTH_FOR_IMAGE, 0);
    }

    public void saveImageWidth(int imageWidth) {
        getEditor().putInt(WIDTH_FOR_IMAGE, imageWidth).apply();
    }

    public int getImageWidthForOneColumn() {
        return sharedPreferences.getInt(WIDTH_FOR_IMAGE_ONE_COLUMN, 0);
    }

    public void saveImageWidthForOneColumn(int imageWidth) {
        getEditor().putInt(WIDTH_FOR_IMAGE_ONE_COLUMN, imageWidth).apply();
    }
}
