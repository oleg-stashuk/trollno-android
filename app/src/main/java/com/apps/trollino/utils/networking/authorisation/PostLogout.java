package com.apps.trollino.utils.networking.authorisation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.apps.trollino.R;
import com.apps.trollino.data.networking.ApiService;
import com.apps.trollino.ui.authorisation.LoginActivity;
import com.apps.trollino.utils.SnackBarMessageCustom;
import com.apps.trollino.utils.data.PrefUtils;
import com.apps.trollino.utils.networking_helper.ErrorMessageFromApi;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.apps.trollino.utils.Const.COUNT_TRY_REQUEST;

public class PostLogout {

    public static void postLogout(Context context, PrefUtils prefUtils, View view) {
        String cookie = prefUtils.getCookie();
        String token = prefUtils.getToken();
        String logoutToken = prefUtils.getLogoutToken();

        ApiService.getInstance(context).postLogout(cookie, token, logoutToken, new Callback<Void>() {
            int countTry = 0;

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()) {
                    if(response.code() == 204) {
                        Log.d("OkHttp", "response.code() == 204");
                    }
                } else {
                    String errorMessage = ErrorMessageFromApi.errorMessageFromApi(response.errorBody());
                    SnackBarMessageCustom.showSnackBar(view, errorMessage);
                }

                prefUtils.saveIsUserAuthorization(false);
                prefUtils.saveCookie("");
                prefUtils.saveToken("");
                prefUtils.saveLogoutToken("");
                prefUtils.savePassword("");
                context.startActivity(new Intent(context, LoginActivity.class));
                ((Activity) context).finish();

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
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
