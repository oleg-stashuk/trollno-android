package com.app.trollno.utils.networking.authorisation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.app.trollno.R;
import com.app.trollno.data.networking.ApiService;
import com.app.trollno.ui.authorisation.LoginActivity;
import com.app.trollno.utils.SnackBarMessageCustom;
import com.app.trollno.utils.data.PrefUtils;
import com.app.trollno.utils.networking_helper.ErrorMessageFromApi;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.trollno.utils.data.Const.TAG_LOG;

public class PostLostPassword {

    public static void postLostPassword(Context context, PrefUtils prefUtils, String email, View view) {

        ApiService.getInstance(context).postLostPassword(email, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()) {
                    prefUtils.saveIsUserAuthorization(false);
                    prefUtils.saveCookie("");
                    prefUtils.saveToken("");
                    prefUtils.saveLogoutToken("");
                    prefUtils.savePassword("");

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    alertDialog.setMessage(context.getString(R.string.password_was_send) + email)
                            .setPositiveButton(android.R.string.yes, (dialogInterface, i) -> {
                                context.startActivity(new Intent(context, LoginActivity.class));
                                ((Activity) context).finish();
                                dialogInterface.dismiss();
                            })
                            .create()
                            .show();

                } else {
                    String errorMessage = ErrorMessageFromApi.errorMessageFromApi(response.errorBody());
                    String messageFromApi = context.getResources().getString(R.string.msg_wrong_email_from_api);
                    if (response.code() == 400 && errorMessage.contains(messageFromApi)) {
                        SnackBarMessageCustom.showSnackBar(view, context.getResources().getString(R.string.msg_wrong_email));
                    } else {
                        SnackBarMessageCustom.showSnackBar(view, errorMessage);
                    }
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
