package com.apps.trollino.utils.networking;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.trollino.data.model.ItemPostModel;
import com.apps.trollino.data.networking.ApiService;
import com.apps.trollino.utils.ImageViewDialog;
import com.apps.trollino.utils.data.PrefUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.apps.trollino.utils.Const.COUNT_TRY_REQUEST;

public class GetItemPost {
    private static Context cont;
    private static ItemPostModel model;

    public static void getItemPost(Context context, PrefUtils prefUtils, String postId, TextView titleTextView, TextView countCommentTextView, ImageView imageView, TextView bodyPostTextView) {
        cont = context;
        String cookie = prefUtils.getCookie();

        ApiService.getInstance().getItemPost(cookie, postId, new Callback<ItemPostModel>() {
            int countTry = 0;

            @Override
            public void onResponse(Call<ItemPostModel> call, Response<ItemPostModel> response) {
                if(response.isSuccessful()) {
                    Log.d("OkHttp", "response isSuccessful");
                    model = response.body();

                    setPostTitle(titleTextView);
                    setPostHeadBanner(imageView);
                    setPostHeadText(bodyPostTextView);
                    setCommentCount(countCommentTextView);

                } else {
                    showToast(response.errorBody().toString());
                    Log.d("OkHttp", "response.errorBody() " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ItemPostModel> call, Throwable t) {
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

    private static void setPostTitle(TextView titleTextView) {
        List<ItemPostModel.TitlePost> titleModel = model.getTitle();
        for(ItemPostModel.TitlePost title : titleModel) {
            titleTextView.setText(title.getTitle());
        }
    }

    private static void setPostHeadBanner(ImageView imageView) {
        List<ItemPostModel.BannerPost> bannerModel = model.getBanner();
        for(ItemPostModel.BannerPost banner : bannerModel) {
            String imageUrl = banner.getUrlBanner();
            if (imageUrl.isEmpty()) {
                imageView.setVisibility(View.GONE);
            } else {
                imageView.setVisibility(View.VISIBLE);
                Picasso
                        .get()
                        .load(imageUrl)
                        .into(imageView);
            }

            imageView.setOnClickListener(v -> {
                ImageViewDialog dialog = new ImageViewDialog();
                dialog.showDialog(cont, "", imageUrl);
            });
        }
    }


    private static void setPostHeadText(TextView bodyPostTextView) {
        List<ItemPostModel.BodyPost> bodyModel = model.getBody();
        for(ItemPostModel.BodyPost body : bodyModel) {
            String text = body.getTextPostBody();
            if (text.isEmpty()) {
                bodyPostTextView.setVisibility(View.GONE);
            } else {
                bodyPostTextView.setVisibility(View.VISIBLE);
                bodyPostTextView.setText(text);
            }
        }
    }

    private static void setCommentCount(TextView countCommentTextView) {
        List<ItemPostModel.CommentPost> commentModel = model.getComment();
        for(ItemPostModel.CommentPost comment : commentModel) {
            if(comment.getCommentCont() > 0) {
                countCommentTextView.setVisibility(View.VISIBLE);
                countCommentTextView.setText(String.valueOf(comment.getCommentCont()));
            } else {
                countCommentTextView.setVisibility(View.GONE);
            }
        }
    }


    private static void showToast(String message) {
        Toast.makeText(cont, message, Toast.LENGTH_SHORT).show();
    }
}
