package com.app.trollno.utils.networking.main_group;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.RecyclerView;

import com.app.trollno.R;
import com.app.trollno.adapters.PostListAdapter;
import com.app.trollno.data.model.PagerModel;
import com.app.trollno.data.model.PostsModel;
import com.app.trollno.data.networking.ApiService;
import com.app.trollno.utils.SnackBarMessageCustom;
import com.app.trollno.utils.data.PostListBySearchFromApi;
import com.app.trollno.utils.data.PrefUtils;
import com.app.trollno.utils.networking_helper.ErrorMessageFromApi;
import com.google.android.material.snackbar.Snackbar;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.trollno.utils.data.Const.TAG_LOG;

public class GetPostBySearch {
    private static int page;
    private static int totalPage;
    private static int totalPosts;
    private static RecyclerView recyclerView;
    private static boolean isGetNewListThis;
    private static PrefUtils prefUt;

    public static void getPostBySearch(Context context, PrefUtils prefUtils,
                                       RecyclerView recycler, SwipyRefreshLayout refreshLayout,
                                       String searchText, View nothingSearch,
                                       PostListAdapter adapter, boolean isGetNewList, ProgressBar progressBar) {
        recyclerView = recycler;
        prefUt = prefUtils;
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
                    totalPosts = postsModel.getPagerModel().getTotalItems();
                    totalPage = postsModel.getPagerModel().getTotalPages() -1;
                    saveCurrentPage(prefUtils);

                } else {
                    String errorMessage = ErrorMessageFromApi.errorMessageFromApi(response.errorBody());
                    SnackBarMessageCustom.showSnackBar(recycler, errorMessage);
                }

                hideUpdateProgressView(refreshLayout, progressBar);
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
                hideUpdateProgressView(refreshLayout, progressBar);
            }
        });

    }

    private static void hideUpdateProgressView(SwipyRefreshLayout refreshLayout, ProgressBar progressBar) {
        if(refreshLayout != null) {
            refreshLayout.setRefreshing(false);
        }
        progressBar.setVisibility(View.GONE);
    }

    private static void saveCurrentPage(PrefUtils prefUtils) {
        if(page < totalPage) {
            prefUtils.saveCurrentPage(totalPage++);
        } else {
            prefUtils.saveCurrentPage(totalPage);
        }
    }

    private static void updatePostListAndNotifyRecyclerAdapter(List<PostsModel.PostDetails> newPostList, PostListAdapter adapter) {
//        int currentListSize = PostListBySearchFromApi.getInstance().getPostListBySearch().size();
        PostListBySearchFromApi.getInstance().savePostBySearchInList(newPostList);
//        int newListSize = PostListBySearchFromApi.getInstance().getPostListBySearch().size();

        if (isGetNewListThis) {
            recyclerView.getLayoutManager().scrollToPosition(0);
        }
        recyclerView.suppressLayout(false);
        adapter.notifyDataSetChanged();

        int currentAdapterPosition =  prefUt.getCurrentAdapterPositionPosts();
        if(currentAdapterPosition > 0 && totalPosts - 1 > currentAdapterPosition){
            prefUt.saveCurrentAdapterPositionAnswers(currentAdapterPosition + 1);
        }
    }
}
