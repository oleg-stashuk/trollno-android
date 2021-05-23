package com.apps.trollino.utils.networking.main_group;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.apps.trollino.R;
import com.apps.trollino.adapters.DiscussPostAdapter;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.apps.trollino.utils.data.Const.CATEGORY_DISCUSSED;
import static com.apps.trollino.utils.data.Const.TAG_LOG;

public class GetMostDiscusPosts {

    public static void makeGetNewPosts(Context context, PrefUtils prefUtils, DiscussPostAdapter adapter,
                                       RecyclerView recycler, ShimmerFrameLayout shimmer,
                                       SwipyRefreshLayout refreshLayout) {
        String cookie = prefUtils.getCookie();

        ApiService.getInstance(context).getMostDiscusPosts(cookie, 0, new Callback<PostsModel>() {
            @Override
            public void onResponse(Call<PostsModel> call, Response<PostsModel> response) {
                if (response.isSuccessful()) {
                    PostStoreProvider.getInstance(context).removeDataFromDBbyCategoryName(CATEGORY_DISCUSSED);
                    CategoryStoreProvider.getInstance(context).updatePositionInCategory(CATEGORY_DISCUSSED, 0);

                    List<PostsModel.PostDetails> newPostList = response.body().getPostDetailsList();
                    PostStoreProvider.getInstance(context).add(newPostList, CATEGORY_DISCUSSED);

                    adapter.addItems(newPostList);
                    adapter.notifyDataSetChanged();
                    recycler.getLayoutManager().scrollToPosition(0);
                    recycler.suppressLayout(false);

                    if (shimmer != null) {
                        ShimmerHide.shimmerHide(recycler, shimmer);
                    }
                } else {
                    String errorMessage = ErrorMessageFromApi.errorMessageFromApi(response.errorBody());
                    SnackBarMessageCustom.showSnackBarOnTheTopByBottomNavigation(recycler, errorMessage);
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
                            .make(recycler, noInternetMessage, Snackbar.LENGTH_INDEFINITE)
                            .setMaxInlineActionWidth(3)
                            .setAction(R.string.refresh_button, v -> {
                                call.clone().enqueue(this);
                            });
                    snackbar.setAnchorView(recycler);
                    snackbar.show();
                } else {
                    SnackBarMessageCustom.showSnackBarOnTheTopByBottomNavigation(recycler, t.getLocalizedMessage());
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
}
