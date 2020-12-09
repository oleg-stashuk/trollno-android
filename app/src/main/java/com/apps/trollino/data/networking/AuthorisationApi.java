package com.apps.trollino.data.networking;

import com.apps.trollino.data.model.RequestLoginModel;
import com.apps.trollino.data.model.ResponseLoginModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface AuthorisationApi {

    @Headers({
            "Content-Type: application/json",
            "Accepts: application/json"
    })
    @POST("/user/login?_format=json")
    Call<ResponseLoginModel> postLogin(@Body RequestLoginModel login);

}
