package com.app.trollno.utils.networking.comment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.app.trollno.R;
import com.app.trollno.data.networking.ApiService;
import com.app.trollno.ui.main_group.CommentToPostActivity;
import com.app.trollno.utils.SnackBarMessageCustom;
import com.app.trollno.utils.data.CommentListFromApi;
import com.app.trollno.utils.data.PrefUtils;
import com.app.trollno.utils.dialogs.GuestDialog;
import com.app.trollno.utils.networking_helper.ErrorMessageFromApi;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.trollno.utils.data.Const.COUNT_TRY_REQUEST;
import static com.app.trollno.utils.data.Const.TAG_LOG;

public class PostLikeToComment {

    public static void postLikeToComment(Context context, PrefUtils prefUtils, String commentId, View view) {
        String cookie = prefUtils.getCookie();
        String token = prefUtils.getToken();

        ApiService.getInstance(context).postLikeToComment(cookie, token, commentId, new Callback<Void>() {
            int countTry = 0;

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()) {
                    CommentListFromApi.getInstance().removeAllDataFromList(prefUtils);
                    Intent intent = new Intent(context, CommentToPostActivity.class);
                    ((Activity) context).finish();
                    context.startActivity(intent);
                } else if(response.code() == 403) {
                    GuestDialog dialog = new GuestDialog();
                    dialog.showDialog(context);
                } else {
                    String errorMassage = ErrorMessageFromApi.errorMessageFromApi(response.errorBody());
                    SnackBarMessageCustom.showSnackBar(view, errorMassage);
                }
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
                    Log.d(TAG_LOG, "t.getLocalizedMessage() " + t.getLocalizedMessage());
                }
            }
        });
    }
}
