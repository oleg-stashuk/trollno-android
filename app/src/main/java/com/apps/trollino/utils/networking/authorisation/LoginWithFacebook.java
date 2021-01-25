package com.apps.trollino.utils.networking.authorisation;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;

import com.apps.trollino.R;
import com.apps.trollino.data.model.login.ResponseLoginModel;
import com.apps.trollino.data.networking.ApiService;
import com.apps.trollino.utils.OpenActivityHelper;
import com.apps.trollino.utils.SnackBarMessageCustom;
import com.apps.trollino.utils.data.PrefUtils;
import com.apps.trollino.utils.networking_helper.ErrorMessageFromApi;
import com.facebook.login.LoginManager;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.apps.trollino.utils.Const.COUNT_TRY_REQUEST;

public class LoginWithFacebook {

    public static void loginWithFacebook(Context context, PrefUtils prefUtils, String facebookToken, View view) {

        ApiService.getInstance(context).loginWithFacebook(facebookToken, new Callback<ResponseLoginModel>() {
            int countTry = 0;

            @Override
            public void onResponse(Call<ResponseLoginModel> call, Response<ResponseLoginModel> response) {
                if(response.isSuccessful()) {
                    Log.d("OkHttp", "!!!!!!!!!!!! @@@@2222 isSuccessful");

                    ResponseLoginModel loginModel = response.body();
                    ResponseLoginModel.User user = loginModel.getCurrentUser();

                    prefUtils.saveUserUid(user.getUid());
                    prefUtils.saveToken(loginModel.getToken());;
                    prefUtils.saveLogoutToken(loginModel.getLogoutToken());
                    prefUtils.saveIsUserAuthorization(true);
                    prefUtils.savePassword("");

                    LoginManager.getInstance().logOut();
                    context.startActivity(OpenActivityHelper.openActivity(context,prefUtils));
                    ((Activity) context).finish();
                } else {
                    LoginManager.getInstance().logOut();
                    String errorMessage = ErrorMessageFromApi.errorMessageFromApi(response.errorBody());
                    SnackBarMessageCustom.showSnackBar(view, errorMessage);
                }
            }

            @Override
            public void onFailure(Call<ResponseLoginModel> call, Throwable t) {
                t.getStackTrace();
                if (countTry <= COUNT_TRY_REQUEST) {
                    call.clone().enqueue(this);
                    countTry++;
                } else {
                    boolean isHaveNotInternet = t.getLocalizedMessage().contains(context.getString(R.string.internet_error_from_api));
                    String noInternetMessage = context.getResources().getString(R.string.internet_error_message);
                    if (isHaveNotInternet) {
                        Snackbar
                                .make(view, noInternetMessage, Snackbar.LENGTH_INDEFINITE)
                                .setMaxInlineActionWidth(3)
                                .setAction(R.string.refresh_button, v -> {
                                    call.clone().enqueue(this);
                                })
                                .show();
                    } else {
                        SnackBarMessageCustom.showSnackBar(view, t.getLocalizedMessage());
                    }
                    Log.d("OkHttp", "t.getLocalizedMessage() " + t.getLocalizedMessage());
                }
            }
        });
    }
}