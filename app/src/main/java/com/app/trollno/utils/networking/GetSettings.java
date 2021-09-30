package com.app.trollno.utils.networking;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.app.trollno.R;
import com.app.trollno.data.model.SettingsModel;
import com.app.trollno.data.model.profile.AvatarImageModel;
import com.app.trollno.data.networking.ApiService;
import com.app.trollno.utils.SnackBarMessageCustom;
import com.app.trollno.utils.data.PrefUtils;
import com.app.trollno.utils.dialogs.AvatarsDialog;
import com.app.trollno.utils.networking_helper.ErrorMessageFromApi;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.trollno.utils.data.Const.TAG_LOG;

public class GetSettings {

    public static void getSettings(Context context, PrefUtils prefUtils, ImageView imageView, View view) {
        String cookie = prefUtils.getCookie();

        ApiService.getInstance(context).getSettings(cookie, new Callback<SettingsModel>() {
            @Override
            public void onResponse(Call<SettingsModel> call, Response<SettingsModel> response) {
                if(response.isSuccessful()) {
                    SettingsModel settingsModel = response.body();

                    prefUtils.saveYoutubeId(settingsModel.getYoutubeKeyList().get(0).getValue());
                    prefUtils.saveAdMobId(settingsModel.getAdMobIdList().get(0).getValue());
                    prefUtils.saveBannerId(settingsModel.getBannerIdList().get(0).getValue());

                    if (prefUtils.getCurrentActivity().equals("")) {
                        prefUtils.saveCountBetweenAds( settingsModel.getAdvertisingList().get(0).getValue());
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
