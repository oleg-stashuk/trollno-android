package com.apps.trollino.utils.networking.single_post;

import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.apps.trollino.R;
import com.apps.trollino.data.networking.ApiService;
import com.apps.trollino.utils.data.PrefUtils;
import com.apps.trollino.utils.networking_helper.ErrorMessageFromApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.apps.trollino.utils.Const.COUNT_TRY_REQUEST;

public class PostUnbookmark {
    private static Context cont;

    public static void removePostFromFavorite(Context context, PrefUtils prefUtils, String postId, Menu menu) {
        cont = context;
        String cookie = prefUtils.getCookie();
        String token = prefUtils.getToken();
        Log.d("OkHttp", "!!!!!!!!!! postId " + postId);

        ApiService.getInstance(context).removePostFromFavorite(cookie, token, postId, new Callback<Void>() {
            int countTry = 0;

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()) {
                    Log.d("OkHttp", "!!!!!!!!!! isSuccessful() removePostFromFavorite");
                    menu.getItem(1).setIcon(ContextCompat.getDrawable(cont, R.drawable.ic_favorite_border_button));
                    prefUtils.saveIsFavorite(false);
                } else {
                    String errorMessage = ErrorMessageFromApi.errorMessageFromApi(response.errorBody());
                    showToast(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
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
