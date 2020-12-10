package com.apps.trollino.data.networking;

import android.content.Context;

import com.apps.trollino.data.model.CategoryModel;
import com.apps.trollino.data.model.ItemPostModel;
import com.apps.trollino.data.model.PostsModel;
import com.apps.trollino.data.model.RegistrationRequestModel;
import com.apps.trollino.data.model.RegistrationResponseModel;
import com.apps.trollino.data.model.RequestLoginModel;
import com.apps.trollino.data.model.ResponseLoginModel;
import com.apps.trollino.data.model.UserProfileModel;
import com.apps.trollino.utils.networking_helper.CustomConverterForItemPost;
import com.apps.trollino.utils.networking_helper.ReceivedCookiesInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.apps.trollino.utils.Const.BASE_URL;

public class ApiService {
    private PostApi postApi;
    private AuthorisationApi authorisationApi;

    private static volatile ApiService instance = null;
    public static ApiService getInstance(Context context) {
        if(instance == null) {
            instance = new ApiService(context);
        }
        return instance;
    }

    private ApiService(Context context) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(ItemPostModel.class, new CustomConverterForItemPost())
//                .registerTypeAdapter(ItemPostModel.class, new CustomConverterForItemPost())
                .create();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new ReceivedCookiesInterceptor(context));
        httpClient.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        postApi = retrofit.create(PostApi.class);
        authorisationApi = retrofit.create(AuthorisationApi.class);
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

    public void getPostsByCategory(String cookie, String categoryId, int page, Callback<PostsModel> callback) {
        postApi.getPostsByCategory(cookie, categoryId, page).enqueue(callback);
    }

    public void getItemPost(String cookie, String postId, Callback<ItemPostModel> callback) {
        postApi.getPostItem(cookie, postId).enqueue(callback);
    }

    public void getSearchPosts(String cookie, String searchText, int page, Callback<PostsModel> callback) {
        postApi.getSearchPosts(cookie, searchText, page).enqueue(callback);
    }

    // GET and POST request for work with Authorisation block
    public void postLogin(String login, String password, Callback<ResponseLoginModel> callback) {
        authorisationApi.postLogin(new RequestLoginModel(login, password)).enqueue(callback);
    }

    public void postRegistration(List<String> login, List<String> mail, List<String> pass, Callback<RegistrationResponseModel> callback) {
        authorisationApi.postRegistration(new RegistrationRequestModel(login, mail, pass)).enqueue(callback);
    }

    public void getUserProfileData(String cookie, String userUid, Callback<UserProfileModel> callback) {
        authorisationApi.getUserProfileData(cookie, userUid).enqueue(callback);
    }

    public void postLogout(String cookie, String token, String logoutToken, Callback<Void> callback) {
        authorisationApi.postLogout(cookie, token, logoutToken).enqueue(callback);
    }

    public void postLostPassword(String email, Callback<Void> callback) {
        authorisationApi.postLostPassword(new RequestLoginModel(email)).enqueue(callback);
    }
}
