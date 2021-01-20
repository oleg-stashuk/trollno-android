package com.apps.trollino.utils.networking.single_post;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.trollino.R;
import com.apps.trollino.adapters.OnePostElementAdapter;
import com.apps.trollino.data.model.CategoryModel;
import com.apps.trollino.data.model.single_post.ItemPostModel;
import com.apps.trollino.data.networking.ApiService;
import com.apps.trollino.ui.base.BaseActivity;
import com.apps.trollino.utils.SnackBarMessageCustom;
import com.apps.trollino.utils.data.PrefUtils;
import com.apps.trollino.utils.networking.user_action.GetCommentListToUserActivity;
import com.apps.trollino.utils.networking_helper.ErrorMessageFromApi;
import com.apps.trollino.utils.networking_helper.ShimmerHide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.logging.LogRecord;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.apps.trollino.utils.Const.COUNT_TRY_REQUEST;

public class GetItemPost {
    private static Context cont;
    private static ItemPostModel model;
    private static PrefUtils prefUt;

    public static void getItemPost(Context context, PrefUtils prefUtils, RecyclerView recyclerView, Menu menu,
                                   TextView categoryTextView, String postId, TextView titleTextView,
                                   TextView countCommentTextView, Button commentButton,
                                   TextView bodyPostTextView, boolean isPostFromCategory, View view,
                                   LinearLayout layout, ShimmerFrameLayout shimmerLayout) {
        cont = context;
        prefUt = prefUtils;
        String cookie = prefUtils.getCookie();

        ApiService.getInstance(context).getItemPost(cookie, postId, new Callback<ItemPostModel>() {
            int countTry = 0;

            @Override
            public void onResponse(Call<ItemPostModel> call, Response<ItemPostModel> response) {
                if(response.isSuccessful()) {
                    model = response.body();
                    if (prefUtils.getIsUserAuthorization() && ! model.isReadByUser()) {
                        new Thread(() -> PostMarkPostAsRead.postMarkPostAsRead(context, prefUtils, postId)).start();
                    }

                    setPostCategory(categoryTextView);
                    setPostTitle(titleTextView);
                    setPostHeadText(bodyPostTextView);
                    setCommentCount(countCommentTextView, commentButton);
                    saveNextAndPrevPostId(isPostFromCategory);

                    List<ItemPostModel.MediaBlock> mediaBlock = model.getMediaBlock();
                    makePartOfPostRecyclerView(recyclerView, mediaBlock);

                    boolean isFavorite = model.isFavorite();
                    changeImageFavoriteButton(menu, isFavorite);
                    prefUtils.saveIsFavorite(isFavorite);

                    ShimmerHide.shimmerHide(layout, shimmerLayout);
                } else {
                    String errorMessage = ErrorMessageFromApi.errorMessageFromApi(response.errorBody());
                    SnackBarMessageCustom.showSnackBar(view, errorMessage);
                }
            }

            @Override
            public void onFailure(Call<ItemPostModel> call, Throwable t) {
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
                    Log.d("OkHttp", "t.getLocalizedMessage() " + t.getLocalizedMessage());
                }
            }
        });

    }

    private static void setPostCategory(TextView categoryTextView) {
        List<CategoryModel> categoryList = prefUt.getCategoryList();
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

    private static void saveNextAndPrevPostId(boolean isPostFromCategory) {
        if(isPostFromCategory) {
            List<ItemPostModel.IdNeighboringPost> nextPostList = model.getNextPost().getCategory();
            for(ItemPostModel.IdNeighboringPost post : nextPostList) {
                prefUt.saveNextPostId(String.valueOf(post.getIdPost()));
                Log.d("OkHttp", "next in category: " + post.getIdPost());
            }

            List<ItemPostModel.IdNeighboringPost> prevPostList = model.getPrevPost().getCategory();
            for(ItemPostModel.IdNeighboringPost post : prevPostList) {
                prefUt.savePrevPostId(String.valueOf(post.getIdPost()));
                Log.d("OkHttp", "prev in category: " + post.getIdPost());
            }

        } else {
            List<ItemPostModel.IdNeighboringPost> nextPostList = model.getNextPost().getPubl();
            for(ItemPostModel.IdNeighboringPost post : nextPostList) {
                prefUt.saveNextPostId(String.valueOf(post.getIdPost()));
                Log.d("OkHttp", "next in other: " + post.getIdPost());
            }

            List<ItemPostModel.IdNeighboringPost> prevPostList = model.getPrevPost().getPubl();
            for(ItemPostModel.IdNeighboringPost post : prevPostList) {
                prefUt.savePrevPostId(String.valueOf(post.getIdPost()));
                Log.d("OkHttp", "prev in other: " + post.getIdPost());
            }
        }
    }

    private static void makePartOfPostRecyclerView(RecyclerView recyclerView, List<ItemPostModel.MediaBlock> mediaBlock) {
        recyclerView.setLayoutManager(new LinearLayoutManager(cont));
        recyclerView.setAdapter(new OnePostElementAdapter((BaseActivity) cont, prefUt.getCountBetweenAds(), mediaBlock));
        recyclerView.setFocusable(false);
    }

    // Смена картинки для кнопки favorite из menu в ToolBar
    private static void changeImageFavoriteButton(Menu menu, boolean isFavoritePost) {
        try {
            if (isFavoritePost) {
                menu.getItem(1).setIcon(ContextCompat.getDrawable(cont, R.drawable.ic_favorite_button));
            } else {
                menu.getItem(1).setIcon(ContextCompat.getDrawable(cont, R.drawable.ic_favorite_border_button));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("OkHttp_1", e.getLocalizedMessage());
        }
    }
}
