package com.apps.trollino.utils.networking.main_group;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.apps.trollino.R;
import com.apps.trollino.adapters.DiscussPostsAdapter;
import com.apps.trollino.data.model.PostsModel;
import com.apps.trollino.data.networking.ApiService;
import com.apps.trollino.utils.SnackBarMessageCustom;
import com.apps.trollino.utils.data.DataListFromApi;
import com.apps.trollino.utils.data.PostListByCategoryFromApi;
import com.apps.trollino.utils.data.PrefUtils;
import com.apps.trollino.utils.networking_helper.ErrorMessageFromApi;
import com.apps.trollino.utils.networking_helper.ShimmerHide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.snackbar.Snackbar;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.apps.trollino.utils.data.Const.TAG_LOG;

public class GetMostDiscusPosts {
    private static int page;
    private static int totalPage;
    private static boolean isGetNewListThis;
    private static Context cont;

    public static void makeGetNewPosts(Context context, PrefUtils prefUtils, DiscussPostsAdapter adapter,
                                       RecyclerView recycler, ShimmerFrameLayout shimmer, SwipyRefreshLayout refreshLayout,
                                       View bottomNavigation, boolean isGetNewList) {
        page = isGetNewList ? 0 : prefUtils.getCurrentPage();
        String cookie = prefUtils.getCookie();
        isGetNewListThis = isGetNewList;
        cont = context;

        ApiService.getInstance(context).getMostDiscusPosts(cookie, page, new Callback<PostsModel>() {
            @Override
            public void onResponse(Call<PostsModel> call, Response<PostsModel> response) {
                if (response.isSuccessful()) {
                    if(isGetNewList) {
                        DataListFromApi.getInstance().removeAllDataFromList(prefUtils);
                        PostListByCategoryFromApi.getInstance().removeAllDataFromList(prefUtils);
                    }

                    PostsModel post = response.body();
                    List<PostsModel.PostDetails> newPostList = post.getPostDetailsList();

                    totalPage = post.getPagerModel().getTotalPages() - 1;
                    saveCurrentPage(prefUtils);
                    updatePostListAndNotifyRecyclerAdapter(newPostList, adapter, bottomNavigation);
                    if (shimmer != null) {
                        ShimmerHide.shimmerHide(recycler, shimmer);
                    }
                } else {
                    String errorMessage = ErrorMessageFromApi.errorMessageFromApi(response.errorBody());
                    SnackBarMessageCustom.showSnackBarOnTheTopByBottomNavigation(bottomNavigation, errorMessage);
                }
                hideUpdateProgressView(shimmer, refreshLayout);
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
                    snackbar.setAnchorView(bottomNavigation);
                    snackbar.show();
                } else {
                    SnackBarMessageCustom.showSnackBarOnTheTopByBottomNavigation(bottomNavigation, t.getLocalizedMessage());
                }
                hideUpdateProgressView(shimmer, refreshLayout);
                Log.d(TAG_LOG, "t.getLocalizedMessage() " + t.getLocalizedMessage());
            }
        });
    }

    private static void hideUpdateProgressView(ShimmerFrameLayout shimmer, SwipyRefreshLayout refreshLayout) {
        if(shimmer != null) {
            shimmer.setVisibility(View.GONE);
        }
        if(refreshLayout != null) {
            refreshLayout.setRefreshing(false);
        }
    }

    private static void saveCurrentPage(PrefUtils prefUtils) {
        if(page < totalPage) {
            prefUtils.saveCurrentPage(page + 1);
        } else {
            prefUtils.saveCurrentPage(totalPage);
        }
    }

    private static void updatePostListAndNotifyRecyclerAdapter(List<PostsModel.PostDetails> newPostList, DiscussPostsAdapter adapter, View bottomNavigation) {
        int currentListSize = DataListFromApi.getInstance().getDiscussPostsList().size();
        DataListFromApi.getInstance().saveDiscussDataInList(newPostList);
        int newListSize = DataListFromApi.getInstance().getDiscussPostsList().size();

        if (newListSize == 0 && isGetNewListThis) {
            SnackBarMessageCustom.showSnackBarOnTheTopByBottomNavigation(bottomNavigation, cont.getResources().getString(R.string.msg_nothing_in_category));
        } else if(newListSize == currentListSize && page == totalPage && ! isGetNewListThis) {
            SnackBarMessageCustom.showSnackBarOnTheTopByBottomNavigation(bottomNavigation, cont.getResources().getString(R.string.msg_have_not_new_post));
        }

        adapter.notifyDataSetChanged();
    }
}
