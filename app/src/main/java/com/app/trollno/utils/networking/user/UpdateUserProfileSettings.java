package com.app.trollno.utils.networking.user;

import android.content.Context;
import android.util.Log;

import com.app.trollno.data.model.profile.RequestUpdateSentPushNewAnswers;
import com.app.trollno.data.model.profile.RequestUpdateShowReadPosts;
import com.app.trollno.data.model.profile.UserProfileModel;
import com.app.trollno.data.networking.ApiService;
import com.app.trollno.utils.data.PrefUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.trollno.utils.data.Const.TAG_LOG;

public class UpdateUserProfileSettings {

    public static void updateSendPushNewAnswers(Context context, PrefUtils prefUtils, boolean isSendPush) {
        String cookie = prefUtils.getCookie();
        String token = prefUtils.getToken();
        int userId = Integer.parseInt(prefUtils.getUserUid());

        List<UserProfileModel.UserBooleanData> dataList = new ArrayList<>();
        dataList.add(new UserProfileModel.UserBooleanData(isSendPush));
        RequestUpdateSentPushNewAnswers updateData = new RequestUpdateSentPushNewAnswers(dataList);

        ApiService.getInstance(context).updateSendPushNewAnswers(cookie, token, userId, updateData, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d(TAG_LOG, "!!!!!!!!! " + response.code());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.getStackTrace();
                Log.d(TAG_LOG, "t.getLocalizedMessage() " + t.getLocalizedMessage());
            }
        });
    }

    public static void updateShowReadPosts(Context context, PrefUtils prefUtils, boolean isSendPush) {
        String cookie = prefUtils.getCookie();
        String token = prefUtils.getToken();
        int userId = Integer.parseInt(prefUtils.getUserUid());

        List<UserProfileModel.UserBooleanData> dataList = new ArrayList<>();
        dataList.add(new UserProfileModel.UserBooleanData(isSendPush));
        RequestUpdateShowReadPosts updateData = new RequestUpdateShowReadPosts(dataList);

        ApiService.getInstance(context).updateShowReadPosts(cookie, token, userId, updateData, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d(TAG_LOG, "!!!!!!!!! " + response.code());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.getStackTrace();
                Log.d(TAG_LOG, "t.getLocalizedMessage() " + t.getLocalizedMessage());
            }
        });
    }
}
