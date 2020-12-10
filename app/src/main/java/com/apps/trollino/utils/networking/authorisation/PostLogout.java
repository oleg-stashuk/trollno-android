package com.apps.trollino.utils.networking.authorisation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.apps.trollino.data.networking.ApiService;
import com.apps.trollino.ui.authorisation.LoginActivity;
import com.apps.trollino.utils.data.PrefUtils;
import com.apps.trollino.utils.networking_helper.ErrorMessageFromApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.apps.trollino.utils.Const.COUNT_TRY_REQUEST;

public class PostLogout {
    private static Context cont;

    public static void postLogout(Context context, PrefUtils prefUtils) {
        cont = context;
        String cookie = prefUtils.getCookie();
        String token = prefUtils.getToken();
        String logoutToken = prefUtils.getLogoutToken();
        Log.d("OkHttp", "!!!! cookie " + cookie);
        Log.d("OkHttp", "!!!! token " + token);
        Log.d("OkHttp", "!!!! logoutToken " + logoutToken);

        ApiService.getInstance(context).postLogout(cookie, token, logoutToken, new Callback<Void>() {
            int countTry = 0;

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()) {
                    Log.d("OkHttp", "response.isSuccessful()");
                } else if(response.code() == 204) {
                    Log.d("OkHttp", "response.isSuccessful() " + response.code());
                    prefUtils.saveIsUserAuthorization(false);
                    context.startActivity(new Intent(context, LoginActivity.class));
                    ((Activity) context).finish();
                }else {
                    String errorMessage = ErrorMessageFromApi.errorMessageFromApi(response.errorBody());
                    showToast(errorMessage);

                    // Этот блок потом удалить
                    prefUtils.saveIsUserAuthorization(false);
                    context.startActivity(new Intent(context, LoginActivity.class));
                    ((Activity) context).finish();
                    // Этот блок потом удалить
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
