package com.apps.trollino.utils.networking;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.trollino.R;
import com.apps.trollino.data.model.CategoryModel;
import com.apps.trollino.data.model.ItemPostModel;
import com.apps.trollino.data.networking.ApiService;
import com.apps.trollino.utils.ImageViewDialog;
import com.apps.trollino.utils.data.CategoryListFromApi;
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

    public static void getItemPost(Context context, PrefUtils prefUtils, TextView categoryTextView, String postId, TextView titleTextView,
                                   TextView countCommentTextView, Button commentButton, ImageView imageView,
                                   TextView bodyPostTextView, boolean isPostFromCategory) {
        cont = context;
        String cookie = prefUtils.getCookie();

        ApiService.getInstance().getItemPost(cookie, postId, new Callback<ItemPostModel>() {
            int countTry = 0;

            @Override
            public void onResponse(Call<ItemPostModel> call, Response<ItemPostModel> response) {
                if(response.isSuccessful()) {
                    Log.d("OkHttp", "response isSuccessful");
                    model = response.body();

                    setPostCategory(categoryTextView);
                    setPostTitle(titleTextView);
                    setPostHeadBanner(imageView);
                    setPostHeadText(bodyPostTextView);
                    setCommentCount(countCommentTextView, commentButton);
                    saveNextAndPrevPostId(isPostFromCategory, prefUtils);

                    List<ItemPostModel.MediaBlock> mediaBlock = model.getMediaBlock();
                    Log.d("OkHttp", "mediaBlock size in API " + mediaBlock.size());

                    for(ItemPostModel.MediaBlock item : mediaBlock) {
                        Log.d("OkHttp", "________________________");
                        Log.d("OkHttp", "Id in API " + item.getIdMediaBlock());
                        ItemPostModel.EntityMediaBlock entity = item.getEntity();
                        Log.d("OkHttp", "title in API " + entity.getTitle());
                        Log.d("OkHttp", "Instagram in API " + entity.getInstagram());
                        Log.d("OkHttp", "Youtube in API " + entity.getYoutube());
                        Log.d("OkHttp", "Tiktok in API " + entity.getTiktok());
                        Log.d("OkHttp", "Desc in API " + entity.getDesc());

                        ItemPostModel.ImageBlock image = entity.getImage();
                        Log.d("OkHttp", "UrlImage in API " + image.getUrlImage());
                        Log.d("OkHttp", "ResourceTitle in API " + image.getResourceTitle());
                        Log.d("OkHttp", "Resource in API " + image.getResource());
                        Log.d("OkHttp", "________________________");
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

    private static void setPostCategory(TextView categoryTextView) {
        List<CategoryModel> categoryList = CategoryListFromApi.getInstance().getCategoryList();
        List<ItemPostModel.CategoryPost> categoryIdList = model.getCategory();
        String categoryId = "";
        for(ItemPostModel.CategoryPost id : categoryIdList) {
            categoryId = String.valueOf(id.getIdCategory());
        }

        for(CategoryModel category : categoryList) {
            if (categoryId.equals(category.getIdCategory())){
                categoryTextView.setText(category.getNameCategory());
            }
        }
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

    private static void setCommentCount(TextView countCommentTextView, Button commentButton) {
        List<ItemPostModel.CommentPost> commentModel = model.getComment();
        for(ItemPostModel.CommentPost comment : commentModel) {
            if(comment.getCommentCont() > 0) {
                countCommentTextView.setVisibility(View.VISIBLE);
                countCommentTextView.setText(String.valueOf(comment.getCommentCont()));
                commentButton.setText(R.string.read_the_comment);
            } else {
                countCommentTextView.setVisibility(View.GONE);
                commentButton.setText(R.string.write_the_comment);
            }
        }
    }

    private static void saveNextAndPrevPostId(boolean isPostFromCategory, PrefUtils prefUtils) {
        if(isPostFromCategory) {
            List<ItemPostModel.IdNeighboringPost> nextPostList = model.getNextPost().getCategory();
            for(ItemPostModel.IdNeighboringPost post : nextPostList) {
                prefUtils.saveNextPostId(String.valueOf(post.getIdPost()));
                Log.d("OkHttp", "next in category: " + post.getIdPost());
            }

            List<ItemPostModel.IdNeighboringPost> prevPostList = model.getPrevPost().getCategory();
            for(ItemPostModel.IdNeighboringPost post : prevPostList) {
                prefUtils.savePrevPostId(String.valueOf(post.getIdPost()));
                Log.d("OkHttp", "prev in category: " + post.getIdPost());
            }

        } else {
            List<ItemPostModel.IdNeighboringPost> nextPostList = model.getNextPost().getPubl();
            for(ItemPostModel.IdNeighboringPost post : nextPostList) {
                prefUtils.saveNextPostId(String.valueOf(post.getIdPost()));
                Log.d("OkHttp", "next in other: " + post.getIdPost());
            }

            List<ItemPostModel.IdNeighboringPost> prevPostList = model.getPrevPost().getPubl();
            for(ItemPostModel.IdNeighboringPost post : prevPostList) {
                prefUtils.savePrevPostId(String.valueOf(post.getIdPost()));
                Log.d("OkHttp", "prev in other: " + post.getIdPost());
            }
        }
    }

    private static void showToast(String message) {
        Toast.makeText(cont, message, Toast.LENGTH_SHORT).show();
    }
}
