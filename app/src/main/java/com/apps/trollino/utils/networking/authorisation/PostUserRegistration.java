package com.apps.trollino.utils.networking.authorisation;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.apps.trollino.R;
import com.apps.trollino.data.model.login.RegistrationResponseModel;
import com.apps.trollino.data.networking.ApiService;
import com.apps.trollino.utils.SnackBarMessageCustom;
import com.apps.trollino.utils.data.PrefUtils;
import com.apps.trollino.utils.networking_helper.ErrorMessageFromApi;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.apps.trollino.utils.data.Const.COUNT_TRY_REQUEST;
import static com.apps.trollino.utils.data.Const.TAG_LOG;

public class PostUserRegistration {

    public static void postRegistration(Context context, String login, String mail, String pass, PrefUtils prefUtils, View view) {
        List<String> loginList = new ArrayList<>();
        loginList.add(login);

        List<String> mailList = new ArrayList<>();
        mailList.add(mail);

        List<String> passList = new ArrayList<>();
        passList.add(pass);

        ApiService.getInstance(context).postRegistration(loginList, mailList, passList, new Callback<RegistrationResponseModel>() {
            int countTry = 0;

            @Override
            public void onResponse(Call<RegistrationResponseModel> call, Response<RegistrationResponseModel> response) {
                if(response.isSuccessful()) {
                    PostUserLogin.postUserLogin(context, login, pass, prefUtils, view);
                } else {
                    String errorMessage = ErrorMessageFromApi.errorMessageFromApi(response.errorBody());
                    String messageFromApi = context.getResources().getString(R.string.msg_register_to_existing_email_from_api);
                    if (response.code() == 422 && errorMessage.contains(messageFromApi)) {
                        SnackBarMessageCustom.showSnackBar(view, context.getResources().getString(R.string.msg_register_to_existing_email));
                    } else {
                        SnackBarMessageCustom.showSnackBar(view, errorMessage);
                    }
                }
            }

            @Override
            public void onFailure(Call<RegistrationResponseModel> call, Throwable t) {
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
                    Log.d(TAG_LOG, "t.getLocalizedMessage() " + t.getLocalizedMessage());
                }
            }
        });
    }
}
