package com.apps.trollino.utils.networking.authorisation;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apps.trollino.R;
import com.apps.trollino.data.model.profile.UserProfileModel;
import com.apps.trollino.data.networking.ApiService;
import com.apps.trollino.service.MyFirebaseMessagingService;
import com.apps.trollino.utils.SnackBarMessageCustom;
import com.apps.trollino.utils.data.PrefUtils;
import com.apps.trollino.utils.dialogs.GuestDialog;
import com.apps.trollino.utils.networking_helper.ErrorMessageFromApi;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.apps.trollino.utils.data.Const.TAG_LOG;

public class GetUserProfile {

    public static void getUserProfile(Context context, PrefUtils prefUtils, ImageView imageView,
                                      View nameView, View emailView, View bottomNavigation,
                                      LinearLayout linearLayout, ShimmerFrameLayout shimmer) {
        String cookie = prefUtils.getCookie();
        String userUid = prefUtils.getUserUid();

        ApiService.getInstance(context).getUserProfileData(cookie, userUid, new Callback<UserProfileModel>() {
            @Override
            public void onResponse(Call<UserProfileModel> call, Response<UserProfileModel> response) {
                if(response.isSuccessful()) {
                    UserProfileModel user = response.body();

                    String name = user.getShowNameModelsList().size() > 0 ?
                            user.getShowNameModelsList().get(0).getValue() :
                            user.getNameList().get(0).getValue();

                    if (nameView instanceof TextView) {
                        TextView nameTextView = (TextView) nameView;
                        nameTextView.setText(name);
                    } else if(nameView instanceof EditText) {
                        EditText nameEditText = (EditText) nameView;
                        nameEditText.setText(name);
                    }

                    if (emailView instanceof TextView) {
                        TextView emailTextView = (TextView) emailView;
                        emailTextView.setText(user.getMailList().get(0).getValue());
                    } else if (emailView instanceof EditText){
                        EditText emailEditText = (EditText) emailView;
                        emailEditText.setText(user.getMailList().get(0).getValue());
                    }

                    String imageUrl = user.getUserImageList().size() > 0 ?
                            user.getUserImageList().get(0).getImageUrl() : "";
                    Picasso
                            .get()
                            .load(imageUrl)
                            .into(imageView);


                    linearLayout.setVisibility(View.VISIBLE);
                    shimmer.setVisibility(View.GONE);

                } else if(response.code() == 403) {
                    GuestDialog dialog = new GuestDialog();
                    dialog.showDialog(context);
                } else {
                    String errorMessage = ErrorMessageFromApi.errorMessageFromApi(response.errorBody());
                    SnackBarMessageCustom.showSnackBarOnTheTopByBottomNavigation(bottomNavigation, errorMessage);
                }
            }

            @Override
            public void onFailure(Call<UserProfileModel> call, Throwable t) {
                t.getStackTrace();
                boolean isHaveNotInternet = t.getLocalizedMessage().contains(context.getString(R.string.internet_error_from_api));
                String noInternetMessage = context.getResources().getString(R.string.internet_error_message);
                if (isHaveNotInternet) {
                    Snackbar snackbar  = Snackbar
                            .make(bottomNavigation, noInternetMessage, Snackbar.LENGTH_INDEFINITE)
                            .setMaxInlineActionWidth(3)
                            .setAction(R.string.refresh_button, v -> {
                                call.clone().enqueue(this);
                            });
                    snackbar.setAnchorView(bottomNavigation);
                    snackbar.show();
                } else {
                    SnackBarMessageCustom.showSnackBarOnTheTopByBottomNavigation(bottomNavigation, t.getLocalizedMessage());
                }
                Log.d(TAG_LOG, "t.getLocalizedMessage() " + t.getLocalizedMessage());
            }
        });
    }

    public static void getUserProfileSettings(Context context, PrefUtils prefUtils) {
        String cookie = prefUtils.getCookie();
        String userUid = prefUtils.getUserUid();

        ApiService.getInstance(context).getUserProfileData(cookie, userUid, new Callback<UserProfileModel>() {
            @Override
            public void onResponse(Call<UserProfileModel> call, Response<UserProfileModel> response) {
                if(response.isSuccessful()) {
                    UserProfileModel user = response.body();

                    try {
                        List<UserProfileModel.UserBooleanData> showReadPostList = user.getUserShowReadPostList();
                        UserProfileModel.UserBooleanData dataReadPost = showReadPostList.get(0);
                        prefUtils.saveIsShowReadPost(dataReadPost.isValue());
                    } catch (IndexOutOfBoundsException e) {
                        prefUtils.saveIsShowReadPost(false);

                    }

                    UserProfileModel.UserBooleanData dataSendPush = null;
                    try {
                        List<UserProfileModel.UserBooleanData> sendPushNewAnswerList = user.getUserSendPushNewAnswerList();
                        dataSendPush = sendPushNewAnswerList.get(0);
                        prefUtils.saveIsSendPushAboutAnswerToComment(dataSendPush.isValue());
                    } catch (IndexOutOfBoundsException e) {
                        prefUtils.saveIsSendPushAboutAnswerToComment(false);
                    }

                    if(dataSendPush.isValue()) {
                        // Get and save firebase token to Api in user account
                        MyFirebaseMessagingService firebaseService = new MyFirebaseMessagingService();
                        firebaseService.getFireBaseToken(context, prefUtils);
                    } else {
                        MyFirebaseMessagingService fireBaseService = new MyFirebaseMessagingService();
                        fireBaseService.onDeletedFireBaseToken(context, prefUtils);
                    }
                }
            }

            @Override
            public void onFailure(Call<UserProfileModel> call, Throwable t) {
                t.getStackTrace();
                Log.d(TAG_LOG, "t.getLocalizedMessage() " + t.getLocalizedMessage());
            }
        });
    }
}
