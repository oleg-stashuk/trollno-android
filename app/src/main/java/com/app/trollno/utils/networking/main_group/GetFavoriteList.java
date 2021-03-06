package com.app.trollno.utils.networking.main_group;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.RecyclerView;

import com.app.trollno.R;
import com.app.trollno.adapters.FavoriteAdapter;
import com.app.trollno.data.model.PostsModel;
import com.app.trollno.data.networking.ApiService;
import com.app.trollno.utils.SnackBarMessageCustom;
import com.app.trollno.utils.data.FavoritePostListFromApi;
import com.app.trollno.utils.data.PrefUtils;
import com.app.trollno.utils.dialogs.GuestDialog;
import com.app.trollno.utils.networking_helper.ErrorMessageFromApi;
import com.app.trollno.utils.networking_helper.ShimmerHide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.snackbar.Snackbar;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.trollno.utils.data.Const.TAG_LOG;

public class GetFavoriteList {
    private static int page;
    private static int totalPage;
    private static int totalFavorites;
    private static RecyclerView recyclerView;
    private static boolean isGetNewListThis;
    private static PrefUtils prefUt;

    public static void getFavoritePosts(Context context, PrefUtils prefUtils, RecyclerView recycler,
                                        ShimmerFrameLayout shimmer, SwipyRefreshLayout refreshLayout,
                                        View noFavoriteListView, View bottomNavigation,
                                        FavoriteAdapter adapter, boolean isGetNewList, ProgressBar progressBar) {
        prefUt = prefUtils;
        recyclerView = recycler;
        isGetNewListThis = isGetNewList;
        page = isGetNewList ? 0 : prefUtils.getCurrentPage();
        if(isGetNewList) {
            FavoritePostListFromApi.getInstance().removeAllDataFromList(prefUtils);
        }
        String cookie = prefUtils.getCookie();

        ApiService.getInstance(context).getFavoritePostList(cookie, page, new Callback<PostsModel>() {
            @Override
            public void onResponse(Call<PostsModel> call, Response<PostsModel> response) {
                if (response.isSuccessful()) {
                    PostsModel post = response.body();
                    List<PostsModel.PostDetails> favoritePostList = post.getPostDetailsList();

                    if (favoritePostList.isEmpty()) {
                        recycler.setVisibility(View.GONE);
                        if (shimmer != null) {
                            ShimmerHide.shimmerHide(noFavoriteListView, shimmer);
                        }
                    } else {
                        noFavoriteListView.setVisibility(View.GONE);
                        if (shimmer != null) {
                            ShimmerHide.shimmerHide(recycler, shimmer);
                        }
                    }

                    totalFavorites = post.getPagerModel().getTotalItems();
                    totalPage = post.getPagerModel().getTotalPages() - 1;
                    saveCurrentPage(prefUtils);
                    updatePostListAndNotifyRecyclerAdapter(favoritePostList, adapter);
                } else if(response.code() == 403) {
                    recycler.setVisibility(View.GONE);
                    noFavoriteListView.setVisibility(View.GONE);
                    GuestDialog dialog = new GuestDialog();
                    dialog.showDialog(context);
                } else {
                    String errorMessage = ErrorMessageFromApi.errorMessageFromApi(response.errorBody());
                    SnackBarMessageCustom.showSnackBarOnTheTopByBottomNavigation(recycler, errorMessage);
                }

                hideUpdateProgressView(shimmer, refreshLayout, progressBar);
            }

            @Override
            public void onFailure(Call<PostsModel> call, Throwable t) {
                t.getStackTrace();
                boolean isHaveNotInternet = t.getLocalizedMessage().contains(context.getString(R.string.internet_error_from_api));
                String noInternetMessage = context.getResources().getString(R.string.internet_error_message);
                if (isHaveNotInternet) {
                    Snackbar snackbar  = Snackbar
                            .make(bottomNavigation, noInternetMessage, Snackbar.LENGTH_INDEFINITE)
                            .setMaxInlineActionWidth(3)
                            .setAction(R.string.refresh_button, v -> {
                                call.clone().enqueue(this);
                            });
                    snackbar.show();
                } else {
                    SnackBarMessageCustom.showSnackBarOnTheTopByBottomNavigation(bottomNavigation, t.getLocalizedMessage());
                }
                Log.d(TAG_LOG, "t.getLocalizedMessage() " + t.getLocalizedMessage());
                hideUpdateProgressView(shimmer, refreshLayout, progressBar);
            }
        });
    }

    private static void hideUpdateProgressView(ShimmerFrameLayout shimmer, SwipyRefreshLayout refreshLayout, ProgressBar progressBar) {
        if(shimmer != null) {
            shimmer.setVisibility(View.GONE);
        }
        if(refreshLayout != null) {
            refreshLayout.setRefreshing(false);
        }
        progressBar.setVisibility(View.GONE);
    }

    private static void saveCurrentPage(PrefUtils prefUtils) {
        if(page < totalPage) {
            prefUtils.saveCurrentPage(page + 1);
        } else {
            prefUtils.saveCurrentPage(totalPage);
        }
    }

    private static void updatePostListAndNotifyRecyclerAdapter(List<PostsModel.PostDetails> favoritePostList, FavoriteAdapter adapter) {
        int currentListSize = FavoritePostListFromApi.getInstance().getFavoritePostList().size();
        FavoritePostListFromApi.getInstance().saveFavoritePostInList(favoritePostList);
        int newListSize = FavoritePostListFromApi.getInstance().getFavoritePostList().size();

        if(newListSize != currentListSize && page != totalPage && isGetNewListThis) {
            adapter.notifyDataSetChanged();
            recyclerView.getLayoutManager().scrollToPosition(0);
            recyclerView.suppressLayout(false);
        }

        int currentAdapterPosition =  prefUt.getCurrentAdapterPositionFavorite();
        if(currentAdapterPosition > 0 && totalFavorites - 1 > currentAdapterPosition){
            prefUt.saveCurrentAdapterPositionAnswers(currentAdapterPosition + 1);
        }
    }
}
