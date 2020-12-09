package com.apps.trollino.utils.networking.authorisation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.apps.trollino.data.model.ResponseLoginModel;
import com.apps.trollino.data.networking.ApiService;
import com.apps.trollino.ui.main_group.ProfileActivity;
import com.apps.trollino.utils.data.PrefUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.apps.trollino.utils.Const.COUNT_TRY_REQUEST;

public class PostUserLogin {
    private static Context cont;

    public static void postUserLogin(Context context, String login, String password, PrefUtils prefUtils) {
        cont = context;

        ApiService.getInstance(context).postLogin(login, password, new Callback<ResponseLoginModel>() {
            int countTry = 0;

            @Override
            public void onResponse(Call<ResponseLoginModel> call, Response<ResponseLoginModel> response) {
                if (response.isSuccessful()) {
                    ResponseLoginModel loginModel = response.body();
                    ResponseLoginModel.User user = loginModel.getCurrentUser();

                    prefUtils.saveUserUid(user.getUid());
                    prefUtils.saveToken(loginModel.getToken());;
                    prefUtils.saveLogoutToken(loginModel.getLogoutToken());
                    prefUtils.saveIsUserAuthorization(true);

                    context.startActivity(new Intent(context, ProfileActivity.class));
                    ((Activity) context).finish();
                } else {
                    if (response.code() != 200) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            String userMessage = jsonObject.getString("message");
                            showToast(userMessage);
                            Log.d("OkHttp", "response.errorBody() " + userMessage);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseLoginModel> call, Throwable t) {
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