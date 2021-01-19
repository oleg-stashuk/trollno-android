package com.apps.trollino.data.networking;

import com.apps.trollino.data.model.single_post.ItemPostModel;
import com.apps.trollino.data.model.single_post.MarkPostAsReadModel;
import com.apps.trollino.data.model.single_post.RequestBookmarkPostModel;
import com.apps.trollino.data.model.single_post.ResponseBookmarkModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface SinglePostApi {
    @Headers({
            "Content-Type: application/json",
            "Accepts: application/json"
    })
    @GET("/node/{post_id}?_format=json")
    Call<ItemPostModel> getPostItem(@Header("Cookie") String cookie, @Path("post_id") String postId);

    @Headers({
            "Content-Type: application/json",
            "Accepts: application/json"
    })
    @POST("/api/flag?_format=json")
    Call<ResponseBookmarkModel> addPostInFavorite(@Header("Cookie") String cookie, @Header("X-CSRF-Token") String token, @Body RequestBookmarkPostModel bookmarkPostModel);

    @Headers({
            "Content-Type: application/json",
            "Accepts: application/json"
    })
    @POST("/api/unflag?_format=json")
    Call<Void> removePostFromFavorite(@Header("Cookie") String cookie, @Header("X-CSRF-Token") String token, @Body RequestBookmarkPostModel bookmarkPostModel);

    @Headers({
            "Content-Type: application/json",
            "Accepts: application/json"
    })
    @POST("/api/flag?_format=json")
    Call<Void> markPostAsRead(@Header("Cookie") String cookie, @Header("X-CSRF-Token") String token, @Body MarkPostAsReadModel markPostAsReadModel);
}
