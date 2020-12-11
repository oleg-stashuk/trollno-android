package com.apps.trollino.data.networking;

import com.apps.trollino.data.model.SettingsModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;

public interface SettingsApi {
    @Headers({
            "Content-Type: application/json",
            "Accepts: application/json"
    })
    @GET("/node/6?_format=json")
    Call<SettingsModel> getUserSettings(@Header("Cookie") String cookie);
}
