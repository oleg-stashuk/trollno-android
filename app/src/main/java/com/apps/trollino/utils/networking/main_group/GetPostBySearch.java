package com.apps.trollino.utils.networking.main_group;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.apps.trollino.utils.Const.COUNT_TRY_REQUEST;

public class GetPostBySearch {
    private static int page;

    public static void getPostBySearch(Context context, PrefUtils prefUtils, String searchText, View nothingSearch, ProgressBar progressBar, PostListAdapter adapter) {
        String cookie = prefUtils.getCookie();
        page = prefUtils.getNewPostCurrentPage();

        ApiService.getInstance(context).getSearchPosts(cookie, searchText, page, new Callback<PostsModel>() {
            int countTry = 0;

            @Override
            public void onResponse(Call<PostsModel> call, Response<PostsModel> response) {
                if(response.isSuccessful()) {
                    PostsModel postsModel = response.body();

                    PagerModel pagerModel = postsModel.getPagerModel();
                    if(pagerModel.getTotalItems() == 0) {
                        nothingSearch.setVisibility(View.VISIBLE);
                    } else {
                        nothingSearch.setVisibility(View.GONE);
                        List<PostsModel.PostDetails> newPostList = postsModel.getPostDetailsList();

                        saveCurrentPage(postsModel.getPagerModel().getTotalPages(), prefUtils);
                        updatePostListAndNotifyRecyclerAdapter(newPostList, adapter);
                    }

                } else {
                    String errorMessage = ErrorMessageFromApi.errorMessageFromApi(response.errorBody());
                    SnackBarMessageCustom.showSnackBar(progressBar, errorMessage);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<PostsModel> call, Throwable t) {
                t.getStackTrace();
                progressBar.setVisibility(View.GONE);
                if (countTry <= COUNT_TRY_REQUEST) {
                    call.clone().enqueue(this);
                    countTry++;
                } else {
                    boolean isHaveNotInternet = t.getLocalizedMessage().contains(context.getString(R.string.internet_error_from_api));
                    String noInternetMessage = context.getResources().getString(R.string.internet_error_message);
                    if (isHaveNotInternet) {
                        Snackbar
                                .make(progressBar, noInternetMessage, Snackbar.LENGTH_INDEFINITE)
                                .setMaxInlineActionWidth(3)
                                .setAction(R.string.refresh_button, v -> {
                                    call.clone().enqueue(this);
                                })
                                .show();
                    } else {
                        SnackBarMessageCustom.showSnackBar(progressBar, t.getLocalizedMessage());
                    }
                    Log.d("OkHttp", "t.getLocalizedMessage() " + t.getLocalizedMessage());
                }
            }
        });

    }

    private static void saveCurrentPage(int totalPage, PrefUtils prefUtils) {
        if(page < totalPage - 1) {
            prefUtils.saveNewPostCurrentPage(totalPage++);
        } else {
            prefUtils.saveNewPostCurrentPage(totalPage--);
        }
    }

    private static void updatePostListAndNotifyRecyclerAdapter(List<PostsModel.PostDetails> newPostList, PostListAdapter adapter) {
        PostListBySearchFromApi.getInstance().savePostByCategoryInList(newPostList);
        adapter.notifyDataSetChanged();
    }
}
