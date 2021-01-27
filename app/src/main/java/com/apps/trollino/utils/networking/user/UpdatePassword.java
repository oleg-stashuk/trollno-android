package com.apps.trollino.utils.networking.user;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;

import com.apps.trollino.R;
import com.apps.trollino.data.model.profile.RequestUpdateUserPassword;
import com.apps.trollino.data.model.profile.UserProfileModel;
import com.apps.trollino.data.networking.ApiService;
import com.apps.trollino.utils.SnackBarMessageCustom;
import com.apps.trollino.utils.data.PrefUtils;
import com.apps.trollino.utils.dialogs.GuestDialog;
import com.apps.trollino.utils.networking.authorisation.PostUserLogin;
import com.apps.trollino.utils.networking_helper.ErrorMessageFromApi;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.apps.trollino.utils.Const.COUNT_TRY_REQUEST;
import static com.apps.trollino.utils.Const.LOG_TAG;

public class UpdatePassword {
    private static Context cont;

    public static void updatePassword(Context context, PrefUtils prefUtils, String existingPassword, String newPassword, View view) {
        cont = context;
        String cookie = prefUtils.getCookie();
        String token = prefUtils.getToken();
        int userUid = Integer.parseInt(prefUtils.getUserUid());

        List<RequestUpdateUserPassword.UpdatePasswordModel> updatePasswordModelList = new ArrayList<>();
        updatePasswordModelList.add(new RequestUpdateUserPassword.UpdatePasswordModel(existingPassword, newPassword));
        RequestUpdateUserPassword passwordModel = new RequestUpdateUserPassword(updatePasswordModelList);

        ApiService.getInstance(context).updatePassword(cookie, token, userUid, passwordModel, new Callback<UserProfileModel>() {
            int countTry = 0;

            @Override
            public void onResponse(Call<UserProfileModel> call, Response<UserProfileModel> response) {
                if(response.isSuccessful()) {
                    String userName = "";
                    List<UserProfileModel.UserData> userProfileModelList= response.body().getNameList();
                    for(UserProfileModel.UserData name : userProfileModelList) {
                        userName = name.getValue();
                    }

                    showSuccessDialog(newPassword, userName, prefUtils, view);
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

    private static void showSuccessDialog(String newPassword, String userName, PrefUtils prefUtils, View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(cont);
        builder
                .setMessage("Пароль успешно изменен на " + newPassword)
                .setPositiveButton(cont.getString(android.R.string.yes), (dialog, which) -> {
                    PostUserLogin.postUserLogin(cont, userName, newPassword, prefUtils, view);
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
