package com.apps.trollino.utils.networking;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.trollino.R;
import com.apps.trollino.data.model.ItemPostModel;
import com.apps.trollino.data.networking.ApiService;
import com.apps.trollino.utils.data.PrefUtils;
import com.bumptech.glide.Glide;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.apps.trollino.utils.Const.COUNT_TRY_REQUEST;

public class GetItemPost {
    private static Context cont;

    public static void getItemPost(Context context, PrefUtils prefUtils, String postId, TextView titleTextView, TextView countComment, ImageView image, TextView bodyPost) {
        cont = context;
        String cookie = prefUtils.getCookie();

        ApiService.getInstance().getItemPost(cookie, postId, new Callback<ItemPostModel>() {
            int countTry = 0;

            @Override
            public void onResponse(Call<ItemPostModel> call, Response<ItemPostModel> response) {
                if(response.isSuccessful()) {
                    Log.d("OkHttp", "response isSuccessful");
                    ItemPostModel model = response.body();

                    List<ItemPostModel.TitlePost> titleModel = model.getTitle();
                    for(ItemPostModel.TitlePost title : titleModel) {
                        titleTextView.setText(title.getTitle());
                    }

                    List<ItemPostModel.BannerPost> bannerModel = model.getBanner();
                    for(ItemPostModel.BannerPost banner : bannerModel) {
                        String imageUrl = banner.getUrlBanner();
                        if (imageUrl.isEmpty()) {
                            image.setVisibility(View.GONE);
                        } else {
                            image.setVisibility(View.VISIBLE);
                            Glide
                                    .with(context)
                                    .load(imageUrl)
                                    .centerCrop()
                                    .placeholder(R.color.colorGreyBackgroundVideo)
                                    .fallback(R.color.colorGreyBackgroundVideo)
                                    .into(image);
                        }
                    }

                    List<ItemPostModel.BodyPost> bodyModel = model.getBody();
                    for(ItemPostModel.BodyPost body : bodyModel) {
                        String text = body.getTextPostBody();
                        if (text.isEmpty()) {
                            bodyPost.setVisibility(View.GONE);
                        } else {
                            bodyPost.setVisibility(View.VISIBLE);
                            bodyPost.setText(text);
                        }
                    }


                    List<ItemPostModel.CommentPost> commentModel = model.getComment();
                    for(ItemPostModel.CommentPost comment : commentModel) {
                        if(comment.getCommentCont() > 0) {
                            countComment.setVisibility(View.VISIBLE);
                            countComment.setText(String.valueOf(comment.getCommentCont()));
                        } else {
                            countComment.setVisibility(View.GONE);
                        }
                    }



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


    private static void showToast(String message) {
        Toast.makeText(cont, message, Toast.LENGTH_SHORT).show();
    }
}
