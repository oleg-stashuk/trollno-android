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
import com.apps.trollino.utils.dialogs.GuestDialog;
import com.apps.trollino.utils.networking_helper.ErrorMessageFromApi;
import com.apps.trollino.utils.networking_helper.ShimmerHide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.apps.trollino.utils.data.Const.COUNT_TRY_REQUEST;
import static com.apps.trollino.utils.data.Const.LOG_TAG;

public class GetFavoriteList {
    private static int page;
    private static int totalPage;
    private static RecyclerView recyclerView;
    private static boolean isGetNewListThis;
    private static Context cont;

    public static void getFavoritePosts(Context context, PrefUtils prefUtils, RecyclerView recycler,
                                        ShimmerFrameLayout shimmer, ProgressBar progressBar, View noFavoriteListView, View bottomNavigation,
                                        FavoriteAdapter adapter, boolean isGetNewList) {
        cont = context;
        recyclerView = recycler;
        isGetNewListThis = isGetNewList;
        page = isGetNewList ? 0 : prefUtils.getCurrentPage();
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
                } else if(response.code() == 403) {
                    recycler.setVisibility(View.GONE);
                    noFavoriteListView.setVisibility(View.GONE);
                    GuestDialog dialog = new GuestDialog();
                    dialog.showDialog(context);
                } else {
                    String errorMessage = ErrorMessageFromApi.errorMessageFromApi(response.errorBody());
                    SnackBarMessageCustom.showSnackBarOnTheTopByBottomNavigation(recycler, errorMessage);
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
                        Snackbar snackbar  = Snackbar
                                .make(bottomNavigation, noInternetMessage, Snackbar.LENGTH_INDEFINITE)
                                .setMaxInlineActionWidth(3)
                                .setAction(R.string.refresh_button, v -> {
                                    call.clone().enqueue(this);
                                });
                        snackbar.setAnchorView(bottomNavigation);
                        snackbar.show();
                    } else {
                        SnackBarMessageCustom.showSnackBarOnTheTopByBottomNavigation(bottomNavigation, t.getLocalizedMessage());
                    }
                    progressBar.setVisibility(View.GONE);
                    Log.d(LOG_TAG, "t.getLocalizedMessage() " + t.getLocalizedMessage());
                }
            }
        });
    }

    private static void saveCurrentPage(PrefUtils prefUtils) {
        if(page < totalPage) {
            prefUtils.saveCurrentPage(page + 1);
        } else {
            prefUtils.saveCurrentPage(totalPage);
        }
    }

    private static void updatePostListAndNotifyRecyclerAdapter(List<PostsModel.PostDetails> favoritePostList, FavoriteAdapter adapter) {
        int currentListSize = FavoritePostListFromApi.getInstance().getFavoritePostLis().size();
        FavoritePostListFromApi.getInstance().saveFavoritePostInList(favoritePostList);
        int newListSize = FavoritePostListFromApi.getInstance().getFavoritePostLis().size();

        if(newListSize == currentListSize && page == totalPage && ! isGetNewListThis) {
            SnackBarMessageCustom.showSnackBar(recyclerView, cont.getResources().getString(R.string.msg_show_all_favorite));
        }

        adapter.notifyDataSetChanged();
    }
}
