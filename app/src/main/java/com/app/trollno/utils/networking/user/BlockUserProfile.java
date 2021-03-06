package com.app.trollno.utils.networking.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.app.trollno.R;
import com.app.trollno.data.model.profile.RequestBlockUserModel;
import com.app.trollno.data.networking.ApiService;
import com.app.trollno.ui.authorisation.LoginActivity;
import com.app.trollno.utils.SnackBarMessageCustom;
import com.app.trollno.utils.data.CleanSavedDataHelper;
import com.app.trollno.utils.data.PrefUtils;
import com.app.trollno.utils.dialogs.GuestDialog;
import com.app.trollno.utils.networking_helper.ErrorMessageFromApi;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.trollno.utils.data.Const.TAG_LOG;

public class BlockUserProfile {
    public static void blockUserProfile(Context context, PrefUtils prefUtils, RequestBlockUserModel blockUserModel, View view) {
        String cookie = prefUtils.getCookie();
        String token = prefUtils.getToken();
        int uidUser = Integer.parseInt(prefUtils.getUserUid());

        ApiService.getInstance(context).blockUser(cookie, token, blockUserModel, uidUser, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()) {
                    CleanSavedDataHelper.cleanAllDataIfUserRemoveOrLogout(prefUtils);
                    SnackBarMessageCustom.showSnackBar(view, context.getResources().getString(R.string.msg_account_remove));
                    context.startActivity(new Intent(context, LoginActivity.class));
                    ((Activity) context).finish();
                } else if(response.code() == 403) {
                    GuestDialog dialog = new GuestDialog();
                    dialog.showDialog(context);
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
