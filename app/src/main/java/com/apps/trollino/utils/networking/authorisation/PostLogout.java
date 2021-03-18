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
import com.apps.trollino.utils.data.CleanSavedDataHelper;
import com.apps.trollino.utils.data.PrefUtils;
import com.apps.trollino.utils.networking_helper.ErrorMessageFromApi;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.apps.trollino.utils.data.Const.TAG_LOG;

public class PostLogout {

    public static void postLogout(Context context, PrefUtils prefUtils, View bottomNavigation) {
        String cookie = prefUtils.getCookie();
        String token = prefUtils.getToken();
        String logoutToken = prefUtils.getLogoutToken();

        ApiService.getInstance(context).postLogout(cookie, token, logoutToken, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()) {
                    if(response.code() == 204) {
                        Log.d(TAG_LOG, "postLogout isSuccessful");
                    }
                } else {
                    String errorMessage = ErrorMessageFromApi.errorMessageFromApi(response.errorBody());
                    SnackBarMessageCustom.showSnackBarOnTheTopByBottomNavigation(bottomNavigation, errorMessage);
                }
                SnackBarMessageCustom.showSnackBarOnTheTopByBottomNavigation(bottomNavigation, context.getResources().getString(R.string.msg_logout));
                CleanSavedDataHelper.cleanAllDataIfUserRemoveOrLogout(prefUtils);
                context.startActivity(new Intent(context, LoginActivity.class));
                ((Activity) context).finish();

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.getStackTrace();
                boolean isHaveNotInternet = t.getLocalizedMessage().contains(context.getString(R.string.internet_error_from_api));
                String noInternetMessage = context.getResources().getString(R.string.internet_error_message);
                if (isHaveNotInternet) {
                    Snackbar snackbar  = Snackbar
                            .make(bottomNavigation, noInternetMessage, Snackbar.LENGTH_INDEFINITE)
                            .setMaxInlineActionWidth(3)
                            .setAction(R.string.refresh_button, v -> {
                                call.clone().enqueue(this);
                            });
                    snackbar.setAnchorView(bottomNavigation);
                    snackbar.show();
                } else {
                    SnackBarMessageCustom.showSnackBarOnTheTopByBottomNavigation(bottomNavigation, t.getLocalizedMessage());
                }
                Log.d(TAG_LOG, "t.getLocalizedMessage() " + t.getLocalizedMessage());
            }
        });
    }
}
