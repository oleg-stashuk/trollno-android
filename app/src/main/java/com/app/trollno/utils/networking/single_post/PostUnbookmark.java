package com.app.trollno.utils.networking.single_post;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.app.trollno.R;
import com.app.trollno.data.networking.ApiService;
import com.app.trollno.ui.main_group.FavoriteActivity;
import com.app.trollno.utils.SnackBarMessageCustom;
import com.app.trollno.utils.data.PrefUtils;
import com.app.trollno.utils.dialogs.GuestDialog;
import com.app.trollno.utils.networking_helper.ErrorMessageFromApi;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.trollno.utils.data.Const.COUNT_TRY_REQUEST;
import static com.app.trollno.utils.data.Const.TAG_LOG;

public class PostUnbookmark {

    public static void removePostFromFavorite(Context context, PrefUtils prefUtils, String postId, Menu menu, View view) {
        String cookie = prefUtils.getCookie();
        String token = prefUtils.getToken();

        ApiService.getInstance(context).removePostFromFavorite(cookie, token, postId, new Callback<Void>() {
            int countTry = 0;

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()) {
                    if (menu != null) {
                        menu.getItem(1).setIcon(ContextCompat.getDrawable(context, R.drawable.ic_favorite_border_button));
                    } else {
                        context.startActivity(new Intent(context, FavoriteActivity.class));
                        ((Activity) context).finish();
                    }
                    SnackBarMessageCustom.showSnackBar(view , context.getResources().getString(R.string.msg_post_remove_from_favorite));
                    prefUtils.saveIsFavorite(false);
                } else if(response.code() == 403) {
                    GuestDialog dialog = new GuestDialog();
                    dialog.showDialog(context);
                } else {
                    String errorMessage = ErrorMessageFromApi.errorMessageFromApi(response.errorBody());
                    SnackBarMessageCustom.showSnackBar(view ,errorMessage);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
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
