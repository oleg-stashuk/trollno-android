package com.app.trollno.utils.networking.single_post;

import android.content.Context;
import android.util.Log;

import com.app.trollno.data.networking.ApiService;
import com.app.trollno.utils.data.PrefUtils;
import com.app.trollno.utils.dialogs.GuestDialog;
import com.app.trollno.utils.networking_helper.ErrorMessageFromApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.trollno.utils.data.Const.COUNT_TRY_REQUEST;
import static com.app.trollno.utils.data.Const.TAG_LOG;

public class PostMarkPostAsRead {

    public static void postMarkPostAsRead(Context context, PrefUtils prefUtils, String postId) {
        String cookie = prefUtils.getCookie();
        String token = prefUtils.getToken();

        ApiService.getInstance(context).markPostAsRead(cookie, token, postId, new Callback<Void>() {
            int countTry = 0;

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()) {
                    Log.d(TAG_LOG, "!!!!!!!!!!!!!!!!!! POST ADD IN READ");
                } else if(response.code() == 403) {
                    GuestDialog dialog = new GuestDialog();
                    dialog.showDialog(context);
                } else {
                    String errorMessage = ErrorMessageFromApi.errorMessageFromApi(response.errorBody());
                    Log.d(TAG_LOG, "!!!!!!!!!!!!!!!!!! " + response.code() + " "  + errorMessage);
            }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.getStackTrace();
                if (countTry <= COUNT_TRY_REQUEST) {
                    call.clone().enqueue(this);
                    countTry++;
                }
                Log.d(TAG_LOG, "!!!!!! t.getLocalizedMessage() " + t.getLocalizedMessage());
            }
        });
    }
}
