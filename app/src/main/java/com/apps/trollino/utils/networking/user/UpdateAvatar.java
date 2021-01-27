package com.apps.trollino.utils.networking.user;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.apps.trollino.R;
import com.apps.trollino.data.model.profile.RequestUpdateAvatarModel;
import com.apps.trollino.data.model.profile.UserProfileModel;
import com.apps.trollino.data.networking.ApiService;
import com.apps.trollino.utils.SnackBarMessageCustom;
import com.apps.trollino.utils.data.PrefUtils;
import com.apps.trollino.utils.dialogs.GuestDialog;
import com.apps.trollino.utils.networking_helper.ErrorMessageFromApi;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.apps.trollino.utils.data.Const.COUNT_TRY_REQUEST;
import static com.apps.trollino.utils.data.Const.LOG_TAG;

public class UpdateAvatar {

    public static void updateAvatar(Context context, PrefUtils prefUtils, RequestUpdateAvatarModel avatarUidImage, Dialog dialog, ImageView imageView, View view) {
        String cookie = prefUtils.getCookie();
        String token = prefUtils.getToken();
        int userUid = Integer.parseInt(prefUtils.getUserUid());

            ApiService.getInstance(context).updateAvatar(cookie, token, avatarUidImage, userUid, new Callback<UserProfileModel>() {
            int countTry = 0;

            @Override
            public void onResponse(Call<UserProfileModel> call, Response<UserProfileModel> response) {
                if(response.isSuccessful()) {
                    UserProfileModel userModel = response.body();
                    SnackBarMessageCustom.showSnackBar(view, "Аватарка изменена");

                    List<UserProfileModel.UserImage> userImageList = userModel.getUserImageList();
                    for(UserProfileModel.UserImage userImage: userImageList) {
                         Picasso
                             .get()
                             .load(userImage.getImageUrl())
                             .into(imageView);
                         dialog.cancel();
                    }


                } else if(response.code() == 403) {
                    GuestDialog dialog = new GuestDialog();
                    dialog.showDialog(context);
                } else {
                    String errorMessage = ErrorMessageFromApi.errorMessageFromApi(response.errorBody());
                    SnackBarMessageCustom.showSnackBar(view, errorMessage);
                }
            }

            @Override
            public void onFailure(Call<UserProfileModel> call, Throwable t) {
                t.getStackTrace();
                if (countTry <= COUNT_TRY_REQUEST) {
                    call.clone().enqueue(this);
                    countTry++;
                } else {
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
                    Log.d(LOG_TAG, "t.getLocalizedMessage() " + t.getLocalizedMessage());
                }
            }
        });
    }
}
