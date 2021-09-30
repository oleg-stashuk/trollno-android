package com.app.trollno.utils.networking.authorisation;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;

import com.app.trollno.R;
import com.app.trollno.data.model.login.ResponseLoginModel;
import com.app.trollno.data.networking.ApiService;
import com.app.trollno.utils.OpenActivityHelper;
import com.app.trollno.utils.SnackBarMessageCustom;
import com.app.trollno.utils.data.PrefUtils;
import com.app.trollno.utils.networking_helper.ErrorMessageFromApi;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.trollno.utils.data.Const.TAG_LOG;

public class PostUserLogin {

    public static void postUserLogin(Context context, String login, String password, PrefUtils prefUtils, View view) {

        ApiService.getInstance(context).postLogin(login, password, new Callback<ResponseLoginModel>() {
            @Override
            public void onResponse(Call<ResponseLoginModel> call, Response<ResponseLoginModel> response) {
                if (response.isSuccessful()) {
                    ResponseLoginModel loginModel = response.body();
                    ResponseLoginModel.User user = loginModel.getCurrentUser();

                    prefUtils.saveUserUid(user.getUid());
                    prefUtils.saveToken(loginModel.getToken());;
                    prefUtils.saveLogoutToken(loginModel.getLogoutToken());
                    prefUtils.saveIsUserAuthorization(true);
                    prefUtils.savePassword(password);
                    prefUtils.saveIsUserLoginByFacebook(false);

                    new Thread(() -> GetUserProfile.getUserProfileSettings(context, prefUtils)).start();

                    SnackBarMessageCustom.showSnackBar(view, context.getResources().getString(R.string.msg_login));
                    context.startActivity(OpenActivityHelper.openActivity(context,prefUtils));
                    ((Activity) context).finish();

                } else {
                    String errorMessage = ErrorMessageFromApi.errorMessageFromApi(response.errorBody());
                    String messageFromApi = context.getResources().getString(R.string.msg_wrong_login_or_password_from_api);
                    if (response.code() == 400 && errorMessage.equals(messageFromApi)) {
                        SnackBarMessageCustom.showSnackBar(view, context.getResources().getString(R.string.msg_wrong_login_or_password));
                    } else {
                        SnackBarMessageCustom.showSnackBar(view, errorMessage);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseLoginModel> call, Throwable t) {
                t.getStackTrace();
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
                Log.d(TAG_LOG, "t.getLocalizedMessage() " + t.getLocalizedMessage());
            }
        });
    }
}
