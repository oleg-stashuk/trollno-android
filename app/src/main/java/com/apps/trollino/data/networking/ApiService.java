package com.apps.trollino.data.networking;

import com.apps.trollino.data.model.CategoryModel;
import com.apps.trollino.data.model.PostsModel;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.apps.trollino.utils.Const.BASE_URL;

public class ApiService {
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

    // GET and POST request for work with for Tape Post block
    public void getNewPosts(String cookie, int page, Callback<PostsModel> callback) {
        postApi.getNewPosts(cookie, page).enqueue(callback);
    }

    public void getMostDiscusPosts(String cookie, int page, Callback<PostsModel> callback) {
        postApi.getMostDiscusPosts(cookie, page).enqueue(callback);
    }

    public void getCategoryList(String cookie, Callback<List<CategoryModel>> callback) {
        postApi.getCategoryList(cookie).enqueue(callback);
    }
}
