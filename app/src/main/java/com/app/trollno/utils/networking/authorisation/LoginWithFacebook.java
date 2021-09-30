package com.app.trollno.utils.networking.authorisation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

import com.app.trollno.R;
import com.app.trollno.data.model.login.ResponseLoginModel;
import com.app.trollno.data.networking.ApiService;
import com.app.trollno.ui.authorisation.EditRealNameActivity;
import com.app.trollno.utils.OpenActivityHelper;
import com.app.trollno.utils.SnackBarMessageCustom;
import com.app.trollno.utils.data.PrefUtils;
import com.app.trollno.utils.networking_helper.ErrorMessageFromApi;
import com.facebook.login.LoginManager;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.trollno.utils.data.Const.TAG_LOG;

public class LoginWithFacebook {

    public static void loginWithFacebook(Context context, PrefUtils prefUtils, String facebookToken, View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(context.getResources().getString(R.string.msg_wait_login_with_facebook)).setCancelable(false);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        ApiService.getInstance(context).loginWithFacebook(facebookToken, new Callback<ResponseLoginModel>() {
            @Override
            public void onResponse(Call<ResponseLoginModel> call, Response<ResponseLoginModel> response) {
                if(response.isSuccessful()) {
                    ResponseLoginModel loginModel = response.body();
                    ResponseLoginModel.User user = loginModel.getCurrentUser();

                    prefUtils.saveUserUid(user.getUid());
                    prefUtils.saveToken(loginModel.getToken());;
                    prefUtils.saveLogoutToken(loginModel.getLogoutToken());
                    prefUtils.saveIsUserAuthorization(true);
                    prefUtils.savePassword("");
                    prefUtils.saveIsUserLoginByFacebook(true);

                    new Thread(() -> GetUserProfile.getUserProfileSettings(context, prefUtils)).start();
                    if (prefUtils.isUserEnterRealName()) {
                        context.startActivity(OpenActivityHelper.openActivity(context,prefUtils));
                    } else {
                        context.startActivity(new Intent(context, EditRealNameActivity.class));
                    }
                    ((Activity) context).finish();
                } else {
                    LoginManager.getInstance().logOut();
                    String errorMessage = ErrorMessageFromApi.errorMessageFromApi(response.errorBody());
                    SnackBarMessageCustom.showSnackBar(view, errorMessage);
                }
                alertDialog.dismiss();
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
                alertDialog.dismiss();
            }

        });
    }
}
