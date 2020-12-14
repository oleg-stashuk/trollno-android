package com.apps.trollino.utils.networking.user;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.apps.trollino.data.model.RequestUpdateUserPassword;
import com.apps.trollino.data.model.UserProfileModel;
import com.apps.trollino.data.networking.ApiService;
import com.apps.trollino.utils.data.PrefUtils;
import com.apps.trollino.utils.networking.authorisation.PostUserLogin;
import com.apps.trollino.utils.networking_helper.ErrorMessageFromApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.apps.trollino.utils.Const.COUNT_TRY_REQUEST;

public class UpdatePassword {
    private static Context cont;

    public static void updatePassword(Context context, PrefUtils prefUtils, String existingPassword, String newPassword) {
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

                    showSuccessDialog(newPassword, userName, prefUtils);
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

    private static void showSuccessDialog(String newPassword, String userName, PrefUtils prefUtils) {
        AlertDialog.Builder builder = new AlertDialog.Builder(cont);
        builder
                .setMessage("Пароль успешно изменен на " + newPassword)
                .setPositiveButton(cont.getString(android.R.string.yes), (dialog, which) -> {
                    PostUserLogin.postUserLogin(cont, userName, newPassword, prefUtils);
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private static void showToast(String message) {
        Toast.makeText(cont, message, Toast.LENGTH_SHORT).show();
    }
}