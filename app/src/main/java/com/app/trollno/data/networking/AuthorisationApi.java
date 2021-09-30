package com.app.trollno.data.networking;

import com.app.trollno.data.model.login.FacebookRequestModel;
import com.app.trollno.data.model.login.RegistrationRequestModel;
import com.app.trollno.data.model.login.RegistrationResponseModel;
import com.app.trollno.data.model.login.RequestLoginModel;
import com.app.trollno.data.model.login.ResponseLoginModel;
import com.app.trollno.data.model.profile.UserProfileModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AuthorisationApi {

    @Headers({
            "Content-Type: application/json",
            "Accepts: application/json"
    })
    @POST("/user/login?_format=json")
    Call<ResponseLoginModel> postLogin(@Body RequestLoginModel login);

    @Headers({
            "Content-Type: application/json",
            "Accepts: application/json"
    })
    @POST("/user/register?_format=json")
    Call<RegistrationResponseModel> postRegistration(@Body RegistrationRequestModel registration);

    @Headers({
            "Content-Type: application/json",
            "Accepts: application/json"
    })
    @GET("/user/{uid}?_format=json")
    Call<UserProfileModel> getUserProfileData(@Header("Cookie") String cookie, @Path("uid") String uidUser);

    @Headers({
            "Content-Type: application/json",
            "Accepts: application/json"
    })
    @POST("/user/logout/?_format=json")
    Call<Void> postLogout(@Header("Cookie") String cookie, @Header("X-CSRF-Token") String token,
                          @Query("token") String logoutToken);

    @POST("/user/lost-password?_format=json")
    Call<Void> postLostPassword(@Body RequestLoginModel email);

    @POST("/user/facebook/login?_format=json")
    Call<ResponseLoginModel> loginWithFacebook(@Body FacebookRequestModel facebookRequestModel);
}
