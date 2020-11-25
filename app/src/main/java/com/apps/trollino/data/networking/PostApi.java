package com.apps.trollino.data.networking;

import com.apps.trollino.data.model.CategoryModel;
import com.apps.trollino.data.model.ItemPostModel;
import com.apps.trollino.data.model.PostsModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
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

    @Headers({
            "Content-Type: application/json",
            "Accepts: application/json"
    })
    @GET("/categories/list?_format=json")
    Call<List<CategoryModel>> getCategoryList(@Header("Cookie") String cookie);

    @Headers({
            "Content-Type: application/json",
            "Accepts: application/json"
    })
    @GET("/posts/category/{category_id}/list?_format=json")
    Call<PostsModel> getPostsByCategory(@Header("Cookie") String cookie, @Path("category_id") String categoryId, @Query("page") int page);

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
    @GET("/posts/search")
    Call<PostsModel> getSearchPosts(@Header("Cookie") String cookie, @Query("title") String textSearch, @Query("page") int page);
//    "/posts/search?title=<STEXT>"
}
