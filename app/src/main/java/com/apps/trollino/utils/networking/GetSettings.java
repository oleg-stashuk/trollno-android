package com.apps.trollino.utils.networking;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.apps.trollino.R;
import com.apps.trollino.data.model.SettingsModel;
import com.apps.trollino.data.model.profile.AvatarImageModel;
import com.apps.trollino.data.networking.ApiService;
import com.apps.trollino.utils.SnackBarMessageCustom;
import com.apps.trollino.utils.data.PrefUtils;
import com.apps.trollino.utils.dialogs.AvatarsDialog;
import com.apps.trollino.utils.networking_helper.ErrorMessageFromApi;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.apps.trollino.utils.data.Const.TAG_LOG;

public class GetSettings {

    public static void getSettings(Context context, PrefUtils prefUtils, ImageView imageView, View view) {
        String cookie = prefUtils.getCookie();

        ApiService.getInstance(context).getSettings(cookie, new Callback<SettingsModel>() {
            int countTry = 0;

            @Override
            public void onResponse(Call<SettingsModel> call, Response<SettingsModel> response) {
                if(response.isSuccessful()) {
                    SettingsModel settingsModel = response.body();

                    List<SettingsModel.KeyModel> adMobIdList = settingsModel.getAdMobIdList();
                    for(SettingsModel.KeyModel adMobId : adMobIdList) {
                        prefUtils.saveAdMobId(adMobId.getValue());
                    }

                    List<SettingsModel.KeyModel> adBannerList = settingsModel.getBannerIdList();
                    for(SettingsModel.KeyModel adBanner : adBannerList) {
                        prefUtils.saveBannerId(adBanner.getValue());
                    }

                    if (prefUtils.getCurrentActivity().equals("")) {
                        List<SettingsModel.AdvertisingModel> advertisingList = settingsModel.getAdvertisingList();
                        for(SettingsModel.AdvertisingModel advertising : advertisingList) {
                            prefUtils.saveCountBetweenAds(advertising.getValue());
                        }
                    } else {
                        List<AvatarImageModel> avatarImageList = settingsModel.getAvatarImageList();
                        AvatarsDialog dialog = new AvatarsDialog();
                        dialog.showDialog(context, prefUtils, avatarImageList, imageView, view);
                    }

                } else {
                    String errorMessage = ErrorMessageFromApi.errorMessageFromApi(response.errorBody());
                    SnackBarMessageCustom.showSnackBar(view, errorMessage);
                }
            }

            @Override
            public void onFailure(Call<SettingsModel> call, Throwable t) {
                t.getStackTrace();
                boolean isHaveNotInternet = t.getLocalizedMessage().contains(context.getString(R.string.internet_error_from_api));
                String noInternetMessage = context.getResources().getString(R.string.internet_error_message);
                if (isHaveNotInternet) {
                    Snackbar
                            .make(view, noInternetMessage, Snackbar.LENGTH_INDEFINITE)
                            .setMaxInlineActionWidth(3)
                            .setAction(R.string.refresh_button, v -> {
                                call.clone().enqueue(this);
                            })
                            .show();
                } else {
                    SnackBarMessageCustom.showSnackBar(view, t.getLocalizedMessage());
                }
                Log.d(TAG_LOG, "t.getLocalizedMessage() " + t.getLocalizedMessage());
            }
        });
    }
}
