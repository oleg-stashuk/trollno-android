package com.apps.trollino.utils.networking.user;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.apps.trollino.data.model.RequestBlockUserModel;
import com.apps.trollino.data.model.UserProfileModel;
import com.apps.trollino.data.networking.ApiService;
import com.apps.trollino.utils.data.PrefUtils;
import com.apps.trollino.utils.networking_helper.ErrorMessageFromApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.apps.trollino.utils.Const.COUNT_TRY_REQUEST;

public class BlockUserProfile {
    private static Context cont;

    public static void blockUserProfile(Context context, PrefUtils prefUtils, RequestBlockUserModel blockUserModel) {
        cont = context;
        String cookie = prefUtils.getCookie();
        String token = prefUtils.getToken();
        int uidUser = Integer.parseInt(prefUtils.getUserUid());

        ApiService.getInstance(context).blockUser(cookie, token, blockUserModel, uidUser, new Callback<UserProfileModel>() {
            int countTry = 0;

            @Override
            public void onResponse(Call<UserProfileModel> call, Response<UserProfileModel> response) {
                if(response.isSuccessful()) {
                    Log.d("OkHttp","response.isSuccessful()");
                } else {
                    String errorMessage = ErrorMessageFromApi.errorMessageFromApi(response.errorBody());
                    showToast(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<UserProfileModel> call, Throwable t) {
                t.getStackTrace();
                if (countTry <= COUNT_TRY_REQUEST) {
                    call.clone().enqueue(this);
                    countTry++;
                } else {
                    showToast(t.getLocalizedMessage());
                    Log.d("OkHttp", "t.getLocalizedMessage() " + t.getLocalizedMessage());
                }
            }
        });


    }

    private static void showToast(String message) {
        Toast.makeText(cont, message, Toast.LENGTH_SHORT).show();
    }
}
