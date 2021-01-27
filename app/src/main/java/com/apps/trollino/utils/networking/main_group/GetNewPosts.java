package com.apps.trollino.utils.networking.main_group;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.RecyclerView;

import com.apps.trollino.R;
import com.apps.trollino.adapters.PostListAdapter;
import com.apps.trollino.data.model.PostsModel;
import com.apps.trollino.data.networking.ApiService;
import com.apps.trollino.utils.SnackBarMessageCustom;
import com.apps.trollino.utils.data.DataListFromApi;
import com.apps.trollino.utils.data.PrefUtils;
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

public class GetNewPosts {
    private static int page;
    private static int totalPage;
    private static RecyclerView recyclerView;
    private static boolean isGetNewListThis;

    public static void makeGetNewPosts(Context context, PrefUtils prefUtils, PostListAdapter adapter, ProgressBar progressBar,
                                       RecyclerView recycler, ShimmerFrameLayout shimmer, boolean isGetNewList) {
        recyclerView = recycler;
        isGetNewListThis = isGetNewList;
        page = isGetNewList ? 0 : prefUtils.getNewPostCurrentPage();
        if(isGetNewList) {
            DataListFromApi.getInstance().removeAllDataFromList(prefUtils);
        }
        String cookie = prefUtils.getCookie();

        ApiService.getInstance(context).getNewPosts(cookie, page, new Callback<PostsModel>() {
            int countTry = 0;

            @Override
            public void onResponse(Call<PostsModel> call, Response<PostsModel> response) {
                if (response.isSuccessful()) {
                    PostsModel post = response.body();
                    List<PostsModel.PostDetails> newPostList = post.getPostDetailsList();

                    totalPage = post.getPagerModel().getTotalPages() - 1;
                    saveCurrentPage(prefUtils);
                    updatePostListAndNotifyRecyclerAdapter(newPostList, adapter);

                } else {
                    String errorMessage = ErrorMessageFromApi.errorMessageFromApi(response.errorBody());
                    SnackBarMessageCustom.showSnackBar(progressBar, errorMessage);
                }
                progressBar.setVisibility(View.GONE);
                ShimmerHide.shimmerHide(recycler, shimmer);
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
                                .make(progressBar, noInternetMessage, Snackbar.LENGTH_INDEFINITE)
                                .setMaxInlineActionWidth(3)
                                .setAction(R.string.refresh_button, v -> {
                                    call.clone().enqueue(this);
                                })
                                .show();
                    } else {
                        SnackBarMessageCustom.showSnackBar(progressBar, t.getLocalizedMessage());
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

    private static void updatePostListAndNotifyRecyclerAdapter(List<PostsModel.PostDetails> newPostList, PostListAdapter adapter) {
        int currentListSize = DataListFromApi.getInstance().getNewPostsList().size();
        DataListFromApi.getInstance().saveDataInList(newPostList);
        int newListSize = DataListFromApi.getInstance().getNewPostsList().size();

        if (newListSize == 0 && isGetNewListThis) {
            SnackBarMessageCustom.showSnackBar(recyclerView, "В этой категории пока ничего нет");
        } else if(newListSize == currentListSize && page == totalPage && ! isGetNewListThis) {
            SnackBarMessageCustom.showSnackBar(recyclerView, "Новых постов пока нет");
        }

        adapter.notifyDataSetChanged();
    }
}
