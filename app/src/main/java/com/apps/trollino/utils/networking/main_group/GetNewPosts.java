package com.apps.trollino.utils.networking.main_group;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.RecyclerView;

import com.apps.trollino.R;
import com.apps.trollino.adapters.PostListAdapter;
import com.apps.trollino.data.model.PostsModel;
import com.apps.trollino.data.networking.ApiService;
import com.apps.trollino.db_room.category.CategoryStoreProvider;
import com.apps.trollino.db_room.posts.PostStoreProvider;
import com.apps.trollino.utils.SnackBarMessageCustom;
import com.apps.trollino.utils.data.Const;
import com.apps.trollino.utils.data.PrefUtils;
import com.apps.trollino.utils.networking_helper.ErrorMessageFromApi;
import com.apps.trollino.utils.networking_helper.ShimmerHide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.snackbar.Snackbar;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.apps.trollino.utils.data.Const.TAG_LOG;

public class GetNewPosts {
    private static int page;
    private static int totalPage;
    private static boolean isGetNewListThis;
    private static RecyclerView recyclerView;
    @SuppressLint("StaticFieldLeak")
    private static Context cont;

    public static void makeGetNewPosts(Context context, PrefUtils prefUtils, PostListAdapter adapter, RecyclerView recycler,
                                       ShimmerFrameLayout shimmer, SwipyRefreshLayout refreshLayout,
                                       View bottomNavigation, boolean isGetNewList, ProgressBar progressBar) {
        isGetNewListThis = isGetNewList;
        recyclerView = recycler;
        cont = context;
        page = isGetNewList ? 0 : prefUtils.getCurrentPage();
        String cookie = prefUtils.getCookie();

        ApiService.getInstance(context).getNewPosts(cookie, page, new Callback<PostsModel>() {
            @Override
            public void onResponse(Call<PostsModel> call, Response<PostsModel> response) {
                if (response.isSuccessful()) {
                    if(isGetNewList) {
                        PostStoreProvider.getInstance(context).removeDataFromDBbyCategoryName(Const.CATEGORY_FRESH);
                        CategoryStoreProvider.getInstance(context).updatePositionInCategory(Const.CATEGORY_FRESH, 0);
                    }

                    List<PostsModel.PostDetails> newPostList = response.body().getPostDetailsList();
                    savePostListToDB(newPostList, adapter);

                    totalPage = response.body().getPagerModel().getTotalPages() - 1;
                    saveCurrentPage(prefUtils);
                } else {
                    String errorMessage = ErrorMessageFromApi.errorMessageFromApi(response.errorBody());
                    SnackBarMessageCustom.showSnackBarOnTheTopByBottomNavigation(bottomNavigation, errorMessage);
                }
                if (shimmer != null) {
                    ShimmerHide.shimmerHide(recycler, shimmer);
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
                    snackbar.setAnchorView(bottomNavigation);
                    snackbar.show();
                } else {
                    SnackBarMessageCustom.showSnackBarOnTheTopByBottomNavigation(bottomNavigation, t.getLocalizedMessage());
                }
                hideUpdateProgressView(shimmer, refreshLayout, progressBar);
                Log.d(TAG_LOG, "t.getLocalizedMessage() " + t.getLocalizedMessage());
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

    private static void savePostListToDB(List<PostsModel.PostDetails> postListFromApi, PostListAdapter adapter) {
        List<PostsModel.PostDetails> postListFromDB = PostStoreProvider.getInstance(cont)
                .getPostByPostName(Const.CATEGORY_FRESH);

        List<PostsModel.PostDetails> newPostList = new ArrayList<>();
        for(PostsModel.PostDetails post : postListFromApi) {
            if (! postListFromDB.contains(post)) {
                newPostList.add(post);
            }
        }
        if (newPostList.size() > 0) {
            PostStoreProvider.getInstance(cont).addFreshPost(newPostList);
        }

        updatePostListAndNotifyRecyclerAdapter(adapter);
    }

    private static void updatePostListAndNotifyRecyclerAdapter(PostListAdapter adapter) {
        adapter.addItems(PostStoreProvider.getInstance(cont).getPostByPostName(Const.CATEGORY_FRESH));
        adapter.notifyDataSetChanged();
        recyclerView.suppressLayout(false);

        if (isGetNewListThis) {
            recyclerView.getLayoutManager().scrollToPosition(0);
        } else {
            recyclerView.getLayoutManager().scrollToPosition(CategoryStoreProvider.getInstance(cont)
                    .getCategoryById(Const.CATEGORY_FRESH).getPostInCategory());
        }
    }
}
