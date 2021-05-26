package com.apps.trollino.utils.networking.main_group;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.RecyclerView;

import com.apps.trollino.R;
import com.apps.trollino.adapters.base.PostByCategoryAdapter;
import com.apps.trollino.data.model.PostsModel;
import com.apps.trollino.data.networking.ApiService;
import com.apps.trollino.db_room.category.CategoryStoreProvider;
import com.apps.trollino.db_room.posts.PostStoreProvider;
import com.apps.trollino.utils.SnackBarMessageCustom;
import com.apps.trollino.utils.data.PrefUtils;
import com.apps.trollino.utils.networking_helper.ErrorMessageFromApi;
import com.apps.trollino.utils.networking_helper.ShimmerHide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.snackbar.Snackbar;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.apps.trollino.utils.data.Const.TAG_LOG;

public class GetPostsByCategory {
    private static boolean isGetNewListThis;
    private static RecyclerView recyclerView;
    @SuppressLint("StaticFieldLeak")
    private static Context cont;
    private static String categoryIdThis;
    private static String categoryName;

    public static void getPostsByCategory(Context context, PrefUtils prefUtils, PostByCategoryAdapter adapter,
                                          RecyclerView recycler, ShimmerFrameLayout shimmer,
                                          SwipyRefreshLayout refreshLayout, boolean isGetNewList,
                                          ProgressBar progressBar, View bottomLine, String categoryId) {

        String cookie = prefUtils.getCookie();
        categoryIdThis = categoryId;
        categoryName = CategoryStoreProvider.getInstance(context).getCategoryById(categoryId).getNameCategory();
        int page = CategoryStoreProvider.getInstance(context).getNextPage(categoryId, isGetNewList);
        isGetNewListThis = isGetNewList;
        recyclerView = recycler;
        cont = context;

        ApiService.getInstance(context).getPostsByCategory(cookie, categoryId, page, new Callback<PostsModel>() {
            @Override
            public void onResponse(Call<PostsModel> call, Response<PostsModel> response) {
                if(response.isSuccessful()) {
                    if(isGetNewList) {
                        PostStoreProvider.getInstance(context).removeDataFromDBbyCategoryName(categoryName);
                        CategoryStoreProvider.getInstance(context).updatePositionInCategory(categoryId, 0);
                    }

                    List<PostsModel.PostDetails> newPostList = response.body().getPostDetailsList();
                    PostStoreProvider.getInstance(context).checkNewPostListAndSaveUnique(newPostList, categoryName);

                    updatePostListAndNotifyRecyclerAdapter(adapter);

                    int currentPage = response.body().getPagerModel().getCurrentPage();
                    int totalPageForDB = response.body().getPagerModel().getTotalPages();
                    int totalItem = response.body().getPagerModel().getTotalItems();
                    CategoryStoreProvider.getInstance(context).updatePagesInCategory(categoryId,
                            currentPage, totalPageForDB, totalItem);

                } else {
                    String errorMessage = ErrorMessageFromApi.errorMessageFromApi(response.errorBody());
                    SnackBarMessageCustom.showSnackBarOnTheTopByBottomNavigation(bottomLine, errorMessage);
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
                try {
                    if (isHaveNotInternet) {
                        Snackbar snackbar  = Snackbar
                                .make(bottomLine, noInternetMessage, Snackbar.LENGTH_INDEFINITE)
                                .setMaxInlineActionWidth(3)
                                .setAction(R.string.refresh_button, v -> {
                                    call.clone().enqueue(this);
                                });
                        snackbar.setAnchorView(bottomLine);
                        snackbar.show();
                    } else {
                        SnackBarMessageCustom.showSnackBarOnTheTopByBottomNavigation(bottomLine, t.getLocalizedMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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

    private static void updatePostListAndNotifyRecyclerAdapter(PostByCategoryAdapter adapter) {
        adapter.addItems(PostStoreProvider.getInstance(cont).getPostByCategoryName(categoryName));
        adapter.notifyDataSetChanged();
        recyclerView.suppressLayout(false);

        int savedPosition = CategoryStoreProvider.getInstance(cont).getCategoryById(categoryIdThis).getPostInCategory();
        int listSize = PostStoreProvider.getInstance(cont).getPostByCategoryName(categoryName).size();

        Objects.requireNonNull(recyclerView.getLayoutManager()).scrollToPosition(isGetNewListThis ? 0 :
                listSize - 1 > savedPosition ? savedPosition + 1 : savedPosition);
    }
}
