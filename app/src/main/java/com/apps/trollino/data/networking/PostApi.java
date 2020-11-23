package com.apps.trollino.data.networking;

import com.apps.trollino.data.model.PostsModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface PostApi {

    @Headers({
            "Content-Type: application/json",
            "Accepts: application/json"
    })
    @GET("/posts/new/list?_format=json")
    Call<PostsModel> getNewPosts(@Header("Cookie") String cookie,  @Query("page") int page);

    @Headers({
            "Content-Type: application/json",
            "Accepts: application/json"
    })
    @GET("/posts/most_discus/list?_format=json")
    Call<PostsModel> getMostDiscusPosts(@Header("Cookie") String cookie, @Query("page") int page);
}
