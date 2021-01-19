package com.apps.trollino.utils.networking.single_post;

import android.content.Context;
import android.util.Log;

import com.apps.trollino.data.model.single_post.ItemPostModel;
import com.apps.trollino.data.networking.ApiService;
import com.apps.trollino.utils.data.PrefUtils;
import com.apps.trollino.utils.networking_helper.ErrorMessageFromApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.apps.trollino.utils.Const.COUNT_TRY_REQUEST;

public class PostMarkPostAsRead {

    public static void postMarkPostAsRead(Context context, PrefUtils prefUtils, String postId) {
        String cookie = prefUtils.getCookie();
        String token = prefUtils.getToken();

        ApiService.getInstance(context).markPostAsRead(cookie, token, postId, new Callback<ItemPostModel>() {
            int countTry = 0;

            @Override
            public void onResponse(Call<ItemPostModel> call, Response<ItemPostModel> response) {
                if(response.isSuccessful()) {
                    Log.d("OkHttp", "!!!!!!!!!!!!!!!!!! POST ADD IN READ");
                } else {
                    String errorMessage = ErrorMessageFromApi.errorMessageFromApi(response.errorBody());
                    Log.d("OkHttp", "!!!!!!!!!!!!!!!!!! " + response.code() + " "  + errorMessage);
            }
            }

            @Override
            public void onFailure(Call<ItemPostModel> call, Throwable t) {
                t.getStackTrace();
                if (countTry <= COUNT_TRY_REQUEST) {
                    call.clone().enqueue(this);
                    countTry++;
                    Log.d("OkHttp", "t.getLocalizedMessage() " + t.getLocalizedMessage());
                }
            }
        });
    }
}
