package com.app.trollno.utils.networking.user;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.app.trollno.R;
import com.app.trollno.data.model.profile.RequestUpdateAvatarModel;
import com.app.trollno.data.model.profile.UserProfileModel;
import com.app.trollno.data.networking.ApiService;
import com.app.trollno.utils.SnackBarMessageCustom;
import com.app.trollno.utils.data.PrefUtils;
import com.app.trollno.utils.dialogs.GuestDialog;
import com.app.trollno.utils.networking_helper.ErrorMessageFromApi;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.trollno.utils.data.Const.TAG_LOG;

public class UpdateAvatar {

    public static void updateAvatar(Context context, PrefUtils prefUtils, RequestUpdateAvatarModel avatarUidImage, Dialog dialog, ImageView imageView, View view) {
        String cookie = prefUtils.getCookie();
        String token = prefUtils.getToken();
        int userUid = Integer.parseInt(prefUtils.getUserUid());

            ApiService.getInstance(context).updateAvatar(cookie, token, avatarUidImage, userUid, new Callback<UserProfileModel>() {
            @Override
            public void onResponse(Call<UserProfileModel> call, Response<UserProfileModel> response) {
                if(response.isSuccessful()) {
                    UserProfileModel userModel = response.body();
                    SnackBarMessageCustom.showSnackBar(view, context.getResources().getString(R.string.txt_change_avatar_image));

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
