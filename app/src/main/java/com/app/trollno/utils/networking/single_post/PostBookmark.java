package com.app.trollno.utils.networking.single_post;

import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.app.trollno.R;
import com.app.trollno.data.model.single_post.ResponseBookmarkModel;
import com.app.trollno.data.networking.ApiService;
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

public class PostBookmark {

    public static void addPostToFavorite(Context context, PrefUtils prefUtils, String postId, Menu menu, View view) {
        String cookie = prefUtils.getCookie();
        String token = prefUtils.getToken();

        ApiService.getInstance(context).addPostToFavorite(cookie, token, postId, new Callback<ResponseBookmarkModel>() {
            int countTry = 0;

            @Override
            public void onResponse(Call<ResponseBookmarkModel> call, Response<ResponseBookmarkModel> response) {
                if(response.isSuccessful()) {
                    menu.getItem(1).setIcon(ContextCompat.getDrawable(context, R.drawable.ic_favorite_button));
                    prefUtils.saveIsFavorite(true);
                    SnackBarMessageCustom.showSnackBar(view , context.getResources().getString(R.string.msg_post_add_to_favorite));
                } else if(response.code() == 403) {
                    GuestDialog dialog = new GuestDialog();
                    dialog.showDialog(context);
                } else {
                    String errorMessage = ErrorMessageFromApi.errorMessageFromApi(response.errorBody());
                    SnackBarMessageCustom.showSnackBar(view,errorMessage);
                }
            }

            @Override
            public void onFailure(Call<ResponseBookmarkModel> call, Throwable t) {
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
