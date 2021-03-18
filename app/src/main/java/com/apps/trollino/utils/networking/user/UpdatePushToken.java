package com.apps.trollino.utils.networking.user;

import android.content.Context;
import android.util.Log;

import com.apps.trollino.data.model.profile.RequestPushNotificationToken;
import com.apps.trollino.data.networking.ApiService;
import com.apps.trollino.utils.data.PrefUtils;
import com.apps.trollino.utils.networking_helper.ErrorMessageFromApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.apps.trollino.utils.data.Const.TAG_LOG;

public class UpdatePushToken {

    public static void sendRegistrationToServer(Context context, PrefUtils prefUtils, String pushToken) {
        String cookie = prefUtils.getCookie();
        String token = prefUtils.getToken();
        int userUid = Integer.parseInt(prefUtils.getUserUid());

        List<RequestPushNotificationToken.PushToken> tokenList = new ArrayList<>();
        tokenList.add(new RequestPushNotificationToken.PushToken(pushToken));
        RequestPushNotificationToken notificationToken = new RequestPushNotificationToken(tokenList);

        ApiService.getInstance(context).updatePushNotificationToken(cookie, token, userUid, notificationToken, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()) {
                    Log.d(TAG_LOG, "push token save Successful");
                } else {
                    String errorMessage = ErrorMessageFromApi.errorMessageFromApi(response.errorBody());
                    Log.d(TAG_LOG, errorMessage);
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
