package com.apps.trollino.utils.networking.authorisation;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.apps.trollino.data.model.RegistrationResponseModel;
import com.apps.trollino.data.networking.ApiService;
import com.apps.trollino.utils.data.PrefUtils;
import com.apps.trollino.utils.networking_helper.ErrorMessageFromApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.apps.trollino.utils.Const.COUNT_TRY_REQUEST;

public class PostUserRegistration {
    private static Context cont;

    public static void postRegistration(Context context, String login, String mail, String pass, PrefUtils prefUtils) {
        cont = context;

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
                    PostUserLogin.postUserLogin(context, login, pass, prefUtils);
                } else {
                    String errorMessage = ErrorMessageFromApi.errorMessageFromApi(response.errorBody());
                    showToast(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<RegistrationResponseModel> call, Throwable t) {
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
