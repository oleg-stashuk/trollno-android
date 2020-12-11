package com.apps.trollino.utils.networking.user;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.apps.trollino.data.model.RequestUpdateAvatarModel;
import com.apps.trollino.data.model.UserProfileModel;
import com.apps.trollino.data.networking.ApiService;
import com.apps.trollino.utils.data.PrefUtils;
import com.apps.trollino.utils.networking_helper.ErrorMessageFromApi;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.apps.trollino.utils.Const.COUNT_TRY_REQUEST;

public class UpdateAvatar {
    private static Context cont;

    public static void updateAvatar(Context context, PrefUtils prefUtils, RequestUpdateAvatarModel avatarUidImage, Dialog dialog, ImageView imageView) {
        cont = context;
        String cookie = prefUtils.getCookie();
        String token = prefUtils.getToken();
        int userUid = Integer.parseInt(prefUtils.getUserUid());

            ApiService.getInstance(context).updateAvatar(cookie, token, avatarUidImage, userUid, new Callback<UserProfileModel>() {
            int countTry = 0;

            @Override
            public void onResponse(Call<UserProfileModel> call, Response<UserProfileModel> response) {
                if(response.isSuccessful()) {
                    UserProfileModel userModel = response.body();

                    List<UserProfileModel.UserImage> userImageList = userModel.getUserImageList();
                    for(UserProfileModel.UserImage userImage: userImageList) {
                         Picasso
                             .get()
                             .load(userImage.getImageUrl())
                             .into(imageView);
                         dialog.cancel();
                    }


                } else {
                    String errorMessage = ErrorMessageFromApi.errorMessageFromApi(response.errorBody());
                    showToast(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<UserProfileModel> call, Throwable t) {
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
