package com.apps.trollino.utils.networking.authorisation;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.trollino.data.model.UserProfileModel;
import com.apps.trollino.data.networking.ApiService;
import com.apps.trollino.utils.data.PrefUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.apps.trollino.utils.Const.COUNT_TRY_REQUEST;

public class GetUserProfile {
    private static Context cont;

    public static void getUserProfile(Context context, PrefUtils prefUtils,
                                      ImageView imageView, View nameView, View emailView) {
        String cookie = prefUtils.getCookie();
        String userUid = prefUtils.getUserUid();
        cont = context;

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
                    if (response.code() != 200) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            String userMessage = jsonObject.getString("message");
                            showToast(userMessage);
                            Log.d("OkHttp", "response.errorBody() " + response.code() + " " + userMessage);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
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
