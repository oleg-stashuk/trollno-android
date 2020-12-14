package com.apps.trollino.data.networking;

import com.apps.trollino.data.model.RequestBlockUserModel;
import com.apps.trollino.data.model.RequestUpdateAvatarModel;
import com.apps.trollino.data.model.RequestUpdateUserPassword;
import com.apps.trollino.data.model.UserProfileModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

public interface UserApi {

    @Headers({
            "Content-Type: application/json",
            "Accepts: application/json",
    })
    @PATCH("/user/{uid}?_format=json")
    Call<UserProfileModel> updateAvatar(@Header("Cookie") String cookie, @Header("X-CSRF-Token") String token, @Body RequestUpdateAvatarModel uidAvatarImage, @Path("uid") int userUid);

    @Headers({
            "Content-Type: application/json",
            "Accepts: application/json",
    })
    @PATCH("/user/{uid}?_format=json")
    Call<UserProfileModel> blockUser(@Header("Cookie") String cookie, @Header("X-CSRF-Token") String token, @Body RequestBlockUserModel blockUserModel, @Path("uid") int userUid);

    @Headers({
            "Content-Type: application/json",
            "Accepts: application/json"
    })
    @PATCH("/user/{uid}?_format=json")
    Call<UserProfileModel> updatePassword(@Header("Cookie") String cookie, @Header("X-CSRF-Token") String token, @Path("uid") int userUid, @Body RequestUpdateUserPassword updateUserPassword);
}
