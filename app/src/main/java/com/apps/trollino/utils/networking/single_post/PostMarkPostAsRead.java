package com.apps.trollino.utils.networking.single_post;

import android.content.Context;
import android.util.Log;

import com.apps.trollino.data.networking.ApiService;
import com.apps.trollino.utils.data.PrefUtils;
import com.apps.trollino.utils.networking_helper.ErrorMessageFromApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.apps.trollino.utils.Const.COUNT_TRY_REQUEST;
import static com.apps.trollino.utils.Const.LOG_TAG;

public class PostMarkPostAsRead {

    public static void postMarkPostAsRead(Context context, PrefUtils prefUtils, String postId) {
        String cookie = prefUtils.getCookie();
        String token = prefUtils.getToken();

        ApiService.getInstance(context).markPostAsRead(cookie, token, postId, new Callback<Void>() {
            int countTry = 0;

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()) {
                    Log.d(LOG_TAG, "!!!!!!!!!!!!!!!!!! POST ADD IN READ");
                } else {
                    String errorMessage = ErrorMessageFromApi.errorMessageFromApi(response.errorBody());
                    Log.d(LOG_TAG, "!!!!!!!!!!!!!!!!!! " + response.code() + " "  + errorMessage);
            }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.getStackTrace();
                if (countTry <= COUNT_TRY_REQUEST) {
                    call.clone().enqueue(this);
                    countTry++;
                }
                Log.d(LOG_TAG, "!!!!!! t.getLocalizedMessage() " + t.getLocalizedMessage());
            }
        });
    }
}
