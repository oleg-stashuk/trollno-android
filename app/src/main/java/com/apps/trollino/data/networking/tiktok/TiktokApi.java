package com.apps.trollino.data.networking.tiktok;

import com.apps.trollino.data.model.TiktokModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TiktokApi {
    @GET("/oembed")
    Call<TiktokModel> getTiktok(@Query("url") String url);
}
