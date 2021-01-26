package com.apps.trollino.utils.networking.main_group;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.RecyclerView;

import com.apps.trollino.R;
import com.apps.trollino.adapters.FavoriteAdapter;
import com.apps.trollino.data.model.PostsModel;
import com.apps.trollino.data.networking.ApiService;
import com.apps.trollino.utils.SnackBarMessageCustom;
import com.apps.trollino.utils.data.FavoritePostListFromApi;
import com.apps.trollino.utils.data.PrefUtils;
import com.apps.trollino.utils.networking_helper.ErrorMessageFromApi;
import com.apps.trollino.utils.networking_helper.ShimmerHide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.apps.trollino.utils.Const.COUNT_TRY_REQUEST;
import static com.apps.trollino.utils.Const.LOG_TAG;

public class GetFavoriteList {
    private static int page;
    private static int totalPage;
    private static RecyclerView recyclerView;
    private static boolean isGetNewListThis;

    public static void getFavoritePosts(Context context, PrefUtils prefUtils, RecyclerView recycler,
                                        ShimmerFrameLayout shimmer, ProgressBar progressBar, View noFavoriteListView,
                                        FavoriteAdapter adapter, boolean isGetNewList) {
        recyclerView = recycler;
        isGetNewListThis = isGetNewList;
        page = isGetNewList ? 0 : prefUtils.getNewPostCurrentPage();
        if(isGetNewList) {
            FavoritePostListFromApi.getInstance().removeAllDataFromList(prefUtils);
        }
        String cookie = prefUtils.getCookie();

        ApiService.getInstance(context).getFavoritePostList(cookie, page, new Callback<PostsModel>() {
            int countTry = 0;

            @Override
            public void onResponse(Call<PostsModel> call, Response<PostsModel> response) {
                if (response.isSuccessful()) {
                    PostsModel post = response.body();
                    List<PostsModel.PostDetails> favoritePostList = post.getPostDetailsList();

                    if (favoritePostList.isEmpty()) {
                        recycler.setVisibility(View.GONE);
                        ShimmerHide.shimmerHide(noFavoriteListView, shimmer);
                    } else {
                        noFavoriteListView.setVisibility(View.GONE);
                        ShimmerHide.shimmerHide(recycler, shimmer);
                    }

                    totalPage = post.getPagerModel().getTotalPages() - 1;
                    saveCurrentPage(prefUtils);
                    updatePostListAndNotifyRecyclerAdapter(favoritePostList, adapter);
                } else {
                    String errorMessage = ErrorMessageFromApi.errorMessageFromApi(response.errorBody());
                    SnackBarMessageCustom.showSnackBar(recycler, errorMessage);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<PostsModel> call, Throwable t) {
                t.getStackTrace();
                if (countTry <= COUNT_TRY_REQUEST) {
                    call.clone().enqueue(this);
                    countTry++;
                } else {
                    boolean isHaveNotInternet = t.getLocalizedMessage().contains(context.getString(R.string.internet_error_from_api));
                    String noInternetMessage = context.getResources().getString(R.string.internet_error_message);
                    if (isHaveNotInternet) {
                        Snackbar
                                .make(recycler, noInternetMessage, Snackbar.LENGTH_INDEFINITE)
                                .setMaxInlineActionWidth(3)
                                .setAction(R.string.refresh_button, v -> {
                                    call.clone().enqueue(this);
                                })
                                .show();
                    } else {
                        SnackBarMessageCustom.showSnackBar(recycler, t.getLocalizedMessage());
                    }
                    progressBar.setVisibility(View.GONE);
                    Log.d(LOG_TAG, "t.getLocalizedMessage() " + t.getLocalizedMessage());
                }
            }
        });
    }

    private static void saveCurrentPage(PrefUtils prefUtils) {
        if(page < totalPage) {
            prefUtils.saveNewPostCurrentPage(page + 1);
        } else {
            prefUtils.saveNewPostCurrentPage(totalPage);
        }
    }

    private static void updatePostListAndNotifyRecyclerAdapter(List<PostsModel.PostDetails> favoritePostList, FavoriteAdapter adapter) {
        int currentListSize = FavoritePostListFromApi.getInstance().getFavoritePostLis().size();
        FavoritePostListFromApi.getInstance().saveFavoritePostInList(favoritePostList);
        int newListSize = FavoritePostListFromApi.getInstance().getFavoritePostLis().size();

        if(newListSize == currentListSize && page == totalPage && ! isGetNewListThis) {
            SnackBarMessageCustom.showSnackBar(recyclerView, "Уже показаны все посты, которые добавлены в Избранное");
        }

        adapter.notifyDataSetChanged();
    }
}
