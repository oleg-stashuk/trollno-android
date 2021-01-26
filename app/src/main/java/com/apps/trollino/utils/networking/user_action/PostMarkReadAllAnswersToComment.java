package com.apps.trollino.utils.networking.user_action;

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

public class PostMarkReadAllAnswersToComment {
    public static void PostMarkReadAllAnswersToComment(Context context, PrefUtils prefUtils, String commentId) {
        String cooke = prefUtils.getCookie();
        String token = prefUtils.getToken();

        ApiService.getInstance(context).markReadAllAnswersToComment(cooke, token, commentId, new Callback<Void>() {
            int countTry = 0;

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()) {
                    Log.d(LOG_TAG, "PostMarkReadAllAnswersToComment isSuccessful ");
                } else {
                    String errorMessage = ErrorMessageFromApi.errorMessageFromApi(response.errorBody());
                    Log.d(LOG_TAG, "errorMessage " + errorMessage);
                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.getStackTrace();
                if (countTry <= COUNT_TRY_REQUEST) {
                    call.clone().enqueue(this);
                    countTry++;
                } else {
                    Log.d(LOG_TAG, "t.getLocalizedMessage() " + t.getLocalizedMessage());
                }
            }
        });

    }
}
