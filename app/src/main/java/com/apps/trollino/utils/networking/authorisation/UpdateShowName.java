package com.apps.trollino.utils.networking.authorisation;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;

import com.apps.trollino.R;
import com.apps.trollino.data.networking.ApiService;
import com.apps.trollino.utils.OpenActivityHelper;
import com.apps.trollino.utils.SnackBarMessageCustom;
import com.apps.trollino.utils.data.PrefUtils;
import com.apps.trollino.utils.networking_helper.ErrorMessageFromApi;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.apps.trollino.utils.data.Const.TAG_LOG;

public class UpdateShowName {

    public static void updateShowName(Context context, PrefUtils prefUtils, String showName, View view) {
        String cookie = prefUtils.getCookie();
        String token = prefUtils.getToken();
        int userId = Integer.parseInt(prefUtils.getUserUid());

        ApiService.getInstance(context).updateShowName(cookie, token, userId, showName, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()) {
                    context.startActivity(OpenActivityHelper.openActivity(context,prefUtils));
                    ((Activity) context).finish();
                } else {
                    String errorMessage = ErrorMessageFromApi.errorMessageFromApi(response.errorBody());
                    SnackBarMessageCustom.showSnackBar(view, errorMessage);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
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
