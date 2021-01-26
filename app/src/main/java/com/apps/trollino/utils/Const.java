package com.apps.trollino.utils;

public class Const {
    public static final int COUNT_TRY_REQUEST = 5;
    public static final String BASE_URL = "http://newsblog.app.km.ua";
    public static final String YOUTUBE_API_KEY = "YOUR API KEY";

    public static final String LOG_TAG = "OkHttp";

    private static final String AUTHURL = "https://api.instagram.com/oauth/authorize/";
    private static final String TOKENURL = "https://api.instagram.com/oauth/access_token";
    public static final String APIURL = "https://api.instagram.com/v1";
    public static final String CALLBACKURL = "Your Redirect URI";

    public static final String SORT_BY_COUNT = "count"; // сортировать по количеству лайков
    public static final String SORT_BY_CHANGE = "changed"; // сортировать по дате создания
    public static final String SORT_ORDER_BY_DESC = "DESC"; // сортировать по возрастанию
    public static final String SORT_ORDER_BY_ASC = "ASC"; // сортировать по убыванию
}
