package com.apps.trollino.utils.networking;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.apps.trollino.R;
import com.apps.trollino.data.model.TiktokModel;
import com.apps.trollino.data.networking.tiktok.ApiServiceTiktok;
import com.apps.trollino.utils.SnackBarMessageCustom;
import com.apps.trollino.utils.networking_helper.ErrorMessageFromApi;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.apps.trollino.utils.data.Const.TAG_LOG;

public class GetTikTok {

    public static void getTikTok(Context context, ImageView tikTokImageView, String tiktokUrl, View view) {

        ApiServiceTiktok.getInstance().getTiktok(tiktokUrl, new Callback<TiktokModel>() {
            @Override
            public void onResponse(Call<TiktokModel> call, Response<TiktokModel> response) {
                if (response.isSuccessful()) {
                    TiktokModel tiktokModel = response.body();

                    Picasso
                            .get()
                            .load(tiktokModel.getImage())
                            .into(tikTokImageView);

//                    tikTokImageView.setOnClickListener(v -> {
//                        WebViewDialog webViewDialog = new WebViewDialog();
//                        String videoUrl = tiktokModel.getVideo();
////                        videoUrl = "https://m.youtube.com/watch?v=dn5BqH_kK4Y";
////                        videoUrl = "http://newsblog.app.km.ua/tiktok-test.html";
////                        videoUrl = "https://www.tiktok.com/@sergiividov/video/6882380633742511361?lang=ru";
////                        videoUrl = "https://www.instagram.com/p/CIGvbXCn1Cm/?igshid=9xcw3tb3ylcu";
//
//
//                        webViewDialog.showWebDialog(context, videoUrl);
//                        Log.d("OkHttp_1", "tiktok video in get from API " + tiktokModel.getVideo());
//                    });

                } else {
                    String errorMessage = ErrorMessageFromApi.errorMessageFromApi(response.errorBody());
                    SnackBarMessageCustom.showSnackBar(view, errorMessage);
                }

            }

            @Override
            public void onFailure(Call<TiktokModel> call, Throwable t) {
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
