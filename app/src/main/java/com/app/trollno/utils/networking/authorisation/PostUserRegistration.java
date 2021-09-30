package com.app.trollno.utils.networking.authorisation;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.app.trollno.R;
import com.app.trollno.data.model.login.RegistrationResponseModel;
import com.app.trollno.data.networking.ApiService;
import com.app.trollno.utils.SnackBarMessageCustom;
import com.app.trollno.utils.data.PrefUtils;
import com.app.trollno.utils.networking_helper.ErrorMessageFromApi;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.trollno.utils.data.Const.TAG_LOG;

public class PostUserRegistration {

    public static void postRegistration(Context context, String login, String mail, String pass, PrefUtils prefUtils, View view) {
        ApiService.getInstance(context).postRegistration(login, mail, pass, login, new Callback<RegistrationResponseModel>() {
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
