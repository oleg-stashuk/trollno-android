package com.apps.trollino.utils.networking;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.apps.trollino.data.model.TiktokModel;
import com.apps.trollino.data.networking.ApiServiceTiktok;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.apps.trollino.utils.Const.COUNT_TRY_REQUEST;

public class GetTikTok {
    private static Context cont;

    public static void getTikTok(Context context, ImageView tikTokImageView, String tiktokUrl) {
        cont = context;

        ApiServiceTiktok.getInstance().getTiktok(tiktokUrl, new Callback<TiktokModel>() {
            int countTry = 0;

            @Override
            public void onResponse(Call<TiktokModel> call, Response<TiktokModel> response) {
                if (response.isSuccessful()) {
                    TiktokModel tiktokModel = response.body();

                    Picasso
                            .get()
                            .load(tiktokModel.getImage())
                            .into(tikTokImageView);
                } else {
                    showToast(response.errorBody().toString());
                    Log.d("OkHttp", "response.errorBody() " + response.errorBody());
                }

            }

            @Override
            public void onFailure(Call<TiktokModel> call, Throwable t) {
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
