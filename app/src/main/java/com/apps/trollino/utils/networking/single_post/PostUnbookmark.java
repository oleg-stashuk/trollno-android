package com.apps.trollino.utils.networking.single_post;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.apps.trollino.R;
import com.apps.trollino.data.networking.ApiService;
import com.apps.trollino.ui.main_group.FavoriteActivity;
import com.apps.trollino.utils.SnackBarMessageCustom;
import com.apps.trollino.utils.data.PrefUtils;
import com.apps.trollino.utils.networking_helper.ErrorMessageFromApi;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.apps.trollino.utils.Const.COUNT_TRY_REQUEST;

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
                    prefUtils.saveIsFavorite(false);
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
                    Log.d("OkHttp", "t.getLocalizedMessage() " + t.getLocalizedMessage());
                }
            }
        });
    }

}
