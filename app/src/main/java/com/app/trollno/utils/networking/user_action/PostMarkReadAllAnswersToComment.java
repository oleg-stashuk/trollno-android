package com.app.trollno.utils.networking.user_action;

import android.content.Context;
import android.util.Log;

import com.app.trollno.data.networking.ApiService;
import com.app.trollno.utils.data.PrefUtils;
import com.app.trollno.utils.dialogs.GuestDialog;
import com.app.trollno.utils.networking_helper.ErrorMessageFromApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.trollno.utils.data.Const.TAG_LOG;

public class PostMarkReadAllAnswersToComment {
    public static void PostMarkReadAllAnswersToComment(Context context, PrefUtils prefUtils) {
        String cooke = prefUtils.getCookie();
        String token = prefUtils.getToken();
        String commentId = prefUtils.getCommentIdForActivity();

        ApiService.getInstance(context).markReadAllAnswersToComment(cooke, token, commentId, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()) {
                    Log.d(TAG_LOG, "!!!!!!!!!!!! PostMarkReadAllAnswersToComment isSuccessful ");
                } else if(response.code() == 403) {
                    GuestDialog dialog = new GuestDialog();
                    dialog.showDialog(context);
                } else {
                    String errorMessage = ErrorMessageFromApi.errorMessageFromApi(response.errorBody());
                    Log.d(TAG_LOG, "errorMessage " + errorMessage);
                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.getStackTrace();
                Log.d(TAG_LOG, "t.getLocalizedMessage() " + t.getLocalizedMessage());
            }
        });

    }
}
