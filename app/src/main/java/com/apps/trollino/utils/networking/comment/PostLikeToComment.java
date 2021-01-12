package com.apps.trollino.utils.networking.comment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.apps.trollino.data.networking.ApiService;
import com.apps.trollino.ui.main_group.CommentToPostActivity;
import com.apps.trollino.utils.data.CommentListFromApi;
import com.apps.trollino.utils.data.PrefUtils;
import com.apps.trollino.utils.networking_helper.ErrorMessageFromApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.apps.trollino.utils.Const.COUNT_TRY_REQUEST;

public class PostLikeToComment {
    private static Context cont;

    public static void postLikeToComment(Context context, PrefUtils prefUtils, String commentId) {
        cont = context;
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
                } else {
                    String errorMassage = ErrorMessageFromApi.errorMessageFromApi(response.errorBody());
                    showToast(errorMassage);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.getStackTrace();
                if (countTry <= COUNT_TRY_REQUEST) {
                    call.clone().enqueue(this);
                    countTry++;
                } else {
                    showToast(t.getLocalizedMessage());
                    Log.d("OkHttp", "t.getLocalizedMessage() " + t.getLocalizedMessage());
                }
            }
        });
    }

    private static void showToast(String message) {
        Toast.makeText(cont, message, Toast.LENGTH_SHORT).show();
    }
}
