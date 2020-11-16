package com.apps.trollino.data.networking;

import com.apps.trollino.data.model.PostModel;
import com.apps.trollino.data.model.PostsModel;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService {
    private final String BASE_URL = "http://newsapp.art-coral.com";

    private PostApi postApi;

    private static volatile ApiService instance = null;
    public static ApiService getInstance() {
        if(instance == null) {
            instance = new ApiService();
        }
        return instance;
    }

    private ApiService() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        postApi = retrofit.create(PostApi.class);
    }

    // GET and POST request for work with registration and authorization block
    public void getNewPosts(String cookie, Callback<PostsModel> callback) {
        postApi.getNewPosts(cookie).enqueue(callback);
    }

}
