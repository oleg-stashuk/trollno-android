package com.apps.trollino.utils.networking.single_post;

import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
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
import com.apps.trollino.utils.networking_helper.ErrorMessageFromApi;
import com.apps.trollino.utils.networking_helper.ShimmerHide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.apps.trollino.utils.data.Const.TAG_LOG;

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
                prefUt.savePrevPostId(String.valueOf(post.getIdPost()));
            }

            List<ItemPostModel.IdNeighboringPost> prevPostList = model.getPrevPost().getCategory();
            for(ItemPostModel.IdNeighboringPost post : prevPostList) {
                prefUt.saveNextPostId(String.valueOf(post.getIdPost()));
            }

        } else {
            List<ItemPostModel.IdNeighboringPost> nextPostList = model.getNextPost().getPubl();
            for(ItemPostModel.IdNeighboringPost post : nextPostList) {
                prefUt.savePrevPostId(String.valueOf(post.getIdPost()));
            }

            List<ItemPostModel.IdNeighboringPost> prevPostList = model.getPrevPost().getPubl();
            for(ItemPostModel.IdNeighboringPost post : prevPostList) {
                prefUt.saveNextPostId(String.valueOf(post.getIdPost()));
            }
        }
    }

    private static void makePartOfPostRecyclerView(RecyclerView recyclerView, List<ItemPostModel.MediaBlock> mediaBlock) {
        recyclerView.setLayoutManager(new LinearLayoutManager(cont));
        recyclerView.setAdapter(new OnePostElementAdapter((BaseActivity) cont, mediaBlock, prefUt));
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
            Log.d(TAG_LOG, e.getLocalizedMessage());
        }
    }
}


/*
public class GetItemPost {
    private static Context cont;
    private static ItemPostModel model;
    private static PrefUtils prefUt;

    private static IncludeContentScrollingPostBinding bindingInRequest;

    public static void getItemPost(Context context, PrefUtils prefUtils, Menu menu,
                                   IncludeContentScrollingPostBinding binding, String postId) {
        bindingInRequest = binding;
        cont = context;
        prefUt = prefUtils;
        String cookie = prefUtils.getCookie();

        ApiService.getInstance(context).getItemPost(cookie, postId, new Callback<ItemPostModel>() {
            @Override
            public void onResponse(Call<ItemPostModel> call, Response<ItemPostModel> response) {
                if(response.isSuccessful()) {
                    model = response.body();
                    if (prefUtils.getIsUserAuthorization() && ! model.isReadByUser()) {
                        new Thread(() -> PostMarkPostAsRead.postMarkPostAsRead(context, prefUtils, postId)).start();
                    }

                    setPostCategory();
                    bindingInRequest.titlePostActivity.setText(model.getTitle().get(0).getTitle());
                    setPostHeadText();
                    setCommentCount();
                    saveNextAndPrevPostId();
                    makePartOfPostRecyclerView();

                    changeImageFavoriteButton(menu);
                    prefUtils.saveIsFavorite(model.isFavorite());

//                    ShimmerHide.shimmerHide(binding.postLayout, binding.includeShimmerPost);
                } else {
                    String errorMessage = ErrorMessageFromApi.errorMessageFromApi(response.errorBody());
                    SnackBarMessageCustom.showSnackBar(binding.nestedScrollInPost, errorMessage);
                }
            }

            @Override
            public void onFailure(Call<ItemPostModel> call, Throwable t) {
                t.getStackTrace();
                boolean isHaveNotInternet = t.getLocalizedMessage().contains(context.getString(R.string.internet_error_from_api));
                String noInternetMessage = context.getResources().getString(R.string.internet_error_message);
                if (isHaveNotInternet) {
                    Snackbar
                            .make(binding.nestedScrollInPost, noInternetMessage, Snackbar.LENGTH_INDEFINITE)
                            .setMaxInlineActionWidth(3)
                            .setAction(R.string.refresh_button, v -> {
                                call.clone().enqueue(this);
                            })
                            .show();
                } else {
                    SnackBarMessageCustom.showSnackBar(binding.nestedScrollInPost, t.getLocalizedMessage());
                }
                Log.d(TAG_LOG, "t.getLocalizedMessage() " + t.getLocalizedMessage());
            }
        });

    }

    private static void setPostCategory() {
        String categoryId = "";
        for(ItemPostModel.CategoryPost id : model.getCategory()) {
            categoryId = String.valueOf(id.getIdCategory());
        }

        for(CategoryModel category : prefUt.getCategoryList()) {
            if (categoryId.equals(category.getIdCategory())){
                bindingInRequest.categoryPostActivity.setText(category.getNameCategory());
            }
        }
    }

    private static void setPostHeadText() {
        String text = model.getBody().get(0).getTextPostBody();
        if (text.isEmpty()) {
            bindingInRequest.bodyPostActivity.setVisibility(View.GONE);
        } else {
            bindingInRequest.bodyPostActivity.setVisibility(View.VISIBLE);
            bindingInRequest.bodyPostActivity.setText(text);
        }
    }

    private static void setCommentCount() {
        int commentCount = model.getComment().get(0).getCommentCont();
        if(commentCount > 0) {
            bindingInRequest.commentCountPostActivity.setVisibility(View.VISIBLE);
            bindingInRequest.commentCountPostActivity.setText(String.valueOf(commentCount));
            bindingInRequest.addCommentButtonPostActivity.setText(R.string.read_the_comment);
        } else {
            bindingInRequest.commentCountPostActivity.setVisibility(View.GONE);
            bindingInRequest.addCommentButtonPostActivity.setText(R.string.write_the_comment);
        }
    }

    private static void saveNextAndPrevPostId() {
        int prevPost;
        int nextPost;
        if(prefUt.IsPostFromCategoryList()) {
            prevPost = model.getNextPost().getCategory().get(0).getIdPost();
            nextPost = model.getPrevPost().getCategory().get(0).getIdPost();
        } else {
            prevPost = model.getNextPost().getPubl().get(0).getIdPost();
            nextPost = model.getPrevPost().getPubl().get(0).getIdPost();
        }
        prefUt.savePrevPostId(String.valueOf(prevPost));
        prefUt.saveNextPostId(String.valueOf(nextPost));
        if (prevPost == 0) {
            bindingInRequest.prevPost.setBackgroundColor(R.drawable.button_background_white);
            bindingInRequest.prevPost.setClickable(false);
        } else {
            bindingInRequest.prevPost.setBackgroundColor(R.drawable.button_background);
            bindingInRequest.prevPost.setClickable(true);
        }
        if (nextPost == 0) {
            bindingInRequest.nextPost.setBackgroundColor(R.drawable.button_background_white);
            bindingInRequest.nextPost.setClickable(false);
        } else {
            bindingInRequest.nextPost.setBackgroundColor(R.drawable.button_background);
            bindingInRequest.nextPost.setClickable(true);
        }
    }

    private static void makePartOfPostRecyclerView() {
        List<ItemPostModel.MediaBlock> mediaBlock = model.getMediaBlock();
        RecyclerView recyclerView = bindingInRequest.recyclerPostActivity;
        recyclerView.setLayoutManager(new LinearLayoutManager(cont));
        recyclerView.setAdapter(new OnePostElementAdapter((BaseActivity) cont, mediaBlock, prefUt));
        recyclerView.setFocusable(false);
    }

    // Смена картинки для кнопки favorite из menu в ToolBar
    private static void changeImageFavoriteButton(Menu menu) {
        try {
            if (model.isFavorite()) {
                menu.getItem(1).setIcon(ContextCompat.getDrawable(cont, R.drawable.ic_favorite_button));
            } else {
                menu.getItem(1).setIcon(ContextCompat.getDrawable(cont, R.drawable.ic_favorite_border_button));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG_LOG, e.getLocalizedMessage());
        }
    }
}

*/