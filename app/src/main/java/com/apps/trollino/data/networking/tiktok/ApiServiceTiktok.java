package com.apps.trollino.data.networking.tiktok;

import com.apps.trollino.data.model.TiktokModel;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiServiceTiktok {
    private TiktokApi tiktokApi;
    private String urlTiktok = "https://www.tiktok.com";

    private static volatile ApiServiceTiktok instance = null;
    public static ApiServiceTiktok getInstance() {
        if(instance == null) {
            instance = new ApiServiceTiktok();
        }
        return instance;
    }

    private ApiServiceTiktok() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(urlTiktok)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        tiktokApi = retrofit.create(TiktokApi.class);
    }

    public void getTiktok(String tiktokUrl, Callback<TiktokModel> callback) {
        tiktokApi.getTiktok(tiktokUrl).enqueue(callback);
    }
}
