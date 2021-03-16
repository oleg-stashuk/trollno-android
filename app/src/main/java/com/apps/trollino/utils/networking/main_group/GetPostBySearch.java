package com.apps.trollino.utils.networking.main_group;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.apps.trollino.R;
import com.apps.trollino.adapters.PostListAdapter;
import com.apps.trollino.data.model.PagerModel;
import com.apps.trollino.data.model.PostsModel;
import com.apps.trollino.data.networking.ApiService;
import com.apps.trollino.utils.SnackBarMessageCustom;
import com.apps.trollino.utils.data.PostListBySearchFromApi;
import com.apps.trollino.utils.data.PrefUtils;
import com.apps.trollino.utils.networking_helper.ErrorMessageFromApi;
import com.google.android.material.snackbar.Snackbar;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.apps.trollino.utils.data.Const.TAG_LOG;

public class GetPostBySearch {
    private static int page;
    private static int totalPage;
    private static RecyclerView recyclerView;
    private static boolean isGetNewListThis;

    public static void getPostBySearch(Context context, PrefUtils prefUtils,
                                       RecyclerView recycler, SwipyRefreshLayout refreshLayout,
                                       String searchText, View nothingSearch,
                                       PostListAdapter adapter, boolean isGetNewList) {
        recyclerView = recycler;
        isGetNewListThis = isGetNewList;
        page = isGetNewList ? 0 : prefUtils.getCurrentPage();
        if(isGetNewList) {
            PostListBySearchFromApi.getInstance().removeAllDataFromList(prefUtils);
        }
        String cookie = prefUtils.getCookie();

        ApiService.getInstance(context).getSearchPosts(cookie, searchText, page, new Callback<PostsModel>() {
            @Override
            public void onResponse(Call<PostsModel> call, Response<PostsModel> response) {
                if(response.isSuccessful()) {
                    PostsModel postsModel = response.body();

                    PagerModel pagerModel = postsModel.getPagerModel();
                    if(pagerModel.getTotalItems() == 0) {
                        recycler.setVisibility(View.GONE);
                    } else {
                        nothingSearch.setVisibility(View.GONE);
                        List<PostsModel.PostDetails> newPostList = postsModel.getPostDetailsList();
                        updatePostListAndNotifyRecyclerAdapter(newPostList, adapter);
                    }
                    totalPage = postsModel.getPagerModel().getTotalPages() -1;
                    saveCurrentPage(prefUtils);

                } else {
                    String errorMessage = ErrorMessageFromApi.errorMessageFromApi(response.errorBody());
                    SnackBarMessageCustom.showSnackBar(recycler, errorMessage);
                }

                hideUpdateProgressView(refreshLayout);
            }

            @Override
            public void onFailure(Call<PostsModel> call, Throwable t) {
                t.getStackTrace();
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
                Log.d(TAG_LOG, "t.getLocalizedMessage() " + t.getLocalizedMessage());
                hideUpdateProgressView(refreshLayout);
            }
        });

    }

    private static void hideUpdateProgressView(SwipyRefreshLayout refreshLayout) {
        if(refreshLayout != null) {
            refreshLayout.setRefreshing(false);
        }
    }

    private static void saveCurrentPage(PrefUtils prefUtils) {
        if(page < totalPage) {
            prefUtils.saveCurrentPage(totalPage++);
        } else {
            prefUtils.saveCurrentPage(totalPage);
        }
    }

    private static void updatePostListAndNotifyRecyclerAdapter(List<PostsModel.PostDetails> newPostList, PostListAdapter adapter) {
        int currentListSize = PostListBySearchFromApi.getInstance().getPostListBySearch().size();
        PostListBySearchFromApi.getInstance().savePostBySearchInList(newPostList);
        int newListSize = PostListBySearchFromApi.getInstance().getPostListBySearch().size();

        if(newListSize == currentListSize && page == totalPage && ! isGetNewListThis) {
            SnackBarMessageCustom.showSnackBar(recyclerView, "Показаны все результаты поиска");
        }

        adapter.notifyDataSetChanged();
    }
}
