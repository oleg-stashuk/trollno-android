package com.apps.trollino.utils.networking;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.apps.trollino.data.model.SettingsModel;
import com.apps.trollino.data.networking.ApiService;
import com.apps.trollino.utils.data.PrefUtils;
import com.apps.trollino.utils.networking_helper.ErrorMessageFromApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.apps.trollino.utils.Const.COUNT_TRY_REQUEST;

public class GetSettings {
    private static Context cont;

    public static void getSettings(Context context, PrefUtils prefUtils) {
        cont = context;
        String cookie = prefUtils.getCookie();

        ApiService.getInstance(context).getSettings(cookie, new Callback<SettingsModel>() {
            int countTry = 0;

            @Override
            public void onResponse(Call<SettingsModel> call, Response<SettingsModel> response) {
                if(response.isSuccessful()) {
                    Log.d("OkHttp", "response.isSuccessful() ");

                    SettingsModel settingsModel = response.body();

                    List<SettingsModel.AdvertisingModel> advertisingList = settingsModel.getAdvertisingList();
                    for(SettingsModel.AdvertisingModel advertising : advertisingList) {
                        Log.d("OkHttp", "advertising " + advertising.getValue());
                    }

                    Log.d("OkHttp", " ");
                    List<SettingsModel.AvatarImageModel> avatarImageList = settingsModel.getAvatarImageList();
                    for(SettingsModel.AvatarImageModel avatarImage : avatarImageList) {
                        Log.d("OkHttp", "avatarImage " + avatarImage.getAvatarId() + " -> " + avatarImage.getAvatarUrl());
                    }

                } else {
                    String errorMessage = ErrorMessageFromApi.errorMessageFromApi(response.errorBody());
                    showToast(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<SettingsModel> call, Throwable t) {
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
