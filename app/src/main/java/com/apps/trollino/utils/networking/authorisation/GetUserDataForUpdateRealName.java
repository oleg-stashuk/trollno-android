package com.apps.trollino.utils.networking.authorisation;

import android.content.Context;
import android.util.Log;

import com.apps.trollino.R;
import com.apps.trollino.data.model.profile.UserProfileModel;
import com.apps.trollino.data.networking.ApiService;
import com.apps.trollino.databinding.ActivityEditRealNameBinding;
import com.apps.trollino.utils.SnackBarMessageCustom;
import com.apps.trollino.utils.data.PrefUtils;
import com.apps.trollino.utils.dialogs.GuestDialog;
import com.apps.trollino.utils.networking_helper.ErrorMessageFromApi;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.apps.trollino.utils.data.Const.TAG_LOG;

public class GetUserDataForUpdateRealName {

    public static void getUserDataForUpdateRealName(Context context, PrefUtils prefUtils, ActivityEditRealNameBinding binding) {
        String cookie = prefUtils.getCookie();
        String userUid = prefUtils.getUserUid();

        ApiService.getInstance(context).getUserProfileData(cookie, userUid, new Callback<UserProfileModel>() {
            @Override
            public void onResponse(Call<UserProfileModel> call, Response<UserProfileModel> response) {
                if(response.isSuccessful()) {
                    UserProfileModel user = response.body();
                    binding.edtRealName.setText(user.getShowNameModelsList().get(0).getValue());
                } else if(response.code() == 403) {
                    GuestDialog dialog = new GuestDialog();
                    dialog.showDialog(context);
                } else {
                    String errorMessage = ErrorMessageFromApi.errorMessageFromApi(response.errorBody());
                    SnackBarMessageCustom.showSnackBarOnTheTopByBottomNavigation(binding.layout, errorMessage);
                }
            }

            @Override
            public void onFailure(Call<UserProfileModel> call, Throwable t) {
                t.getStackTrace();
                boolean isHaveNotInternet = t.getLocalizedMessage().contains(context.getString(R.string.internet_error_from_api));
                String noInternetMessage = context.getResources().getString(R.string.internet_error_message);
                if (isHaveNotInternet) {
                    Snackbar snackbar  = Snackbar
                            .make(binding.layout, noInternetMessage, Snackbar.LENGTH_INDEFINITE)
                            .setMaxInlineActionWidth(3)
                            .setAction(R.string.refresh_button, v -> {
                                call.clone().enqueue(this);
                            });
                    snackbar.setAnchorView(binding.layout);
                    snackbar.show();
                } else {
                    SnackBarMessageCustom.showSnackBarOnTheTopByBottomNavigation(binding.layout, t.getLocalizedMessage());
                }
                Log.d(TAG_LOG, "t.getLocalizedMessage() " + t.getLocalizedMessage());
            }
        });
    }
}
