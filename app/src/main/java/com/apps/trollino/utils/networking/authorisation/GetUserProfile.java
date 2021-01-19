package com.apps.trollino.utils.networking.authorisation;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.apps.trollino.R;
import com.apps.trollino.data.model.UserProfileModel;
import com.apps.trollino.data.networking.ApiService;
import com.apps.trollino.utils.SnackBarMessageCustom;
import com.apps.trollino.utils.data.PrefUtils;
import com.apps.trollino.utils.networking_helper.ErrorMessageFromApi;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.apps.trollino.utils.Const.COUNT_TRY_REQUEST;

public class GetUserProfile {

    public static void getUserProfile(Context context, PrefUtils prefUtils, ImageView imageView,
                                      View nameView, View emailView, View view) {
        String cookie = prefUtils.getCookie();
        String userUid = prefUtils.getUserUid();

        ApiService.getInstance(context).getUserProfileData(cookie, userUid, new Callback<UserProfileModel>() {
            int countTry = 0;

            @Override
            public void onResponse(Call<UserProfileModel> call, Response<UserProfileModel> response) {
                if(response.isSuccessful()) {
                    UserProfileModel user = response.body();

                    List<UserProfileModel.UserData> nameList = user.getNameList();
                    for(UserProfileModel.UserData name : nameList) {
                        if (nameView instanceof TextView) {
                            TextView nameTextView = (TextView) nameView;
                            nameTextView.setText(name.getValue());
                        } else if(nameView instanceof EditText) {
                            EditText nameEditText = (EditText) nameView;
                            nameEditText.setText(name.getValue());
                        }
                    }

                    List<UserProfileModel.UserData> emailList = user.getMailList();
                    for(UserProfileModel.UserData email : emailList) {
                        if (emailView instanceof TextView) {
                            TextView emailTextView = (TextView) emailView;
                            emailTextView.setText(email.getValue());
                        } else if (emailView instanceof EditText){
                            EditText emailEditText = (EditText) emailView;
                            emailEditText.setText(email.getValue());
                        }
                    }

                    List<UserProfileModel.UserImage> imageList = user.getUserImageList();
                    for(UserProfileModel.UserImage image : imageList) {
                        String imageUrl = image.getImageUrl();
                        Picasso
                                .get()
                                .load(imageUrl)
                                .into(imageView);
                    }

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
                    Log.d("OkHttp", "t.getLocalizedMessage() " + t.getLocalizedMessage());
                }
            }
        });
    }
}
