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
import com.apps.trollino.utils.data.DataListFromApi;
import com.apps.trollino.utils.data.PostListByCategoryFromApi;
import com.apps.trollino.utils.data.PrefUtils;
import com.apps.trollino.utils.networking_helper.ErrorMessageFromApi;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.apps.trollino.utils.Const.COUNT_TRY_REQUEST;

public class GetPostsByCategory {
    private static int page;

    public static void getPostsByCategory(Context context, PrefUtils prefUtils, PostListAdapter adapter,
                                          ProgressBar progressBarBottom, ProgressBar progressBarTop, boolean scrollOnTop) {
        page = scrollOnTop ? 0 : prefUtils.getCurrentPage();
        String cookie = prefUtils.getCookie();
        String categoryId = prefUtils.getSelectedCategoryId();

        ApiService.getInstance(context).getPostsByCategory(cookie, categoryId, page, new Callback<PostsModel>() {
            int countTry = 0;

            @Override
            public void onResponse(Call<PostsModel> call, Response<PostsModel> response) {
                if(response.isSuccessful()) {
                    PostsModel post = response.body();
                    List<PostsModel.PostDetails> newPostList = post.getPostDetailsList();
                    PagerModel pagerModel = post.getPagerModel();

                    String lastIdInListFromApi = newPostList.isEmpty() ? "null" : newPostList.get(newPostList.size()-1).getPostId();
                    boolean isLastPage = pagerModel.getCurrentPage() == pagerModel.getTotalPages()-1;

                    List<PostsModel.PostDetails> savedPostList = PostListByCategoryFromApi.getInstance().getPostListByCategory();
                    String firstIdInSavedList = savedPostList.isEmpty() ? "null" : savedPostList.get(0).getPostId();
                    String lastIdInSavedList = savedPostList.isEmpty() ? "null" : savedPostList.get(newPostList.size() - 1).getPostId();

                    if(newPostList.isEmpty()) {
                        SnackBarMessageCustom.showSnackBar(progressBarTop, "Новых постов пока нет!!!!!!!!!!!!!!!!!!!!!!");
                    } else if (newPostList.get(0).getPostId().equals(firstIdInSavedList) && scrollOnTop) {
                        SnackBarMessageCustom.showSnackBar(progressBarTop, "Новых постов пока нет");
                    } else if(lastIdInListFromApi.equals(lastIdInSavedList) && isLastPage) {
                        SnackBarMessageCustom.showSnackBar(progressBarTop, "Новых постов пока нет");
                    } else {
                        if(scrollOnTop) {
                            DataListFromApi.getInstance().removeAllDataFromList(prefUtils);
                        }
                        saveCurrentPage(post.getPagerModel().getTotalPages(), prefUtils);
                        updatePostListAndNotifyRecyclerAdapter(newPostList, adapter);
                    }
                } else {
                    String errorMessage = ErrorMessageFromApi.errorMessageFromApi(response.errorBody());
                    SnackBarMessageCustom.showSnackBar(progressBarTop, errorMessage);
                }
                progressBarTop.setVisibility(View.GONE);
                progressBarBottom.setVisibility(View.GONE);
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
                                .make(progressBarTop, noInternetMessage, Snackbar.LENGTH_INDEFINITE)
                                .setMaxInlineActionWidth(3)
                                .setAction(R.string.refresh_button, v -> {
                                    call.clone().enqueue(this);
                                })
                                .show();
                    } else {
                        SnackBarMessageCustom.showSnackBar(progressBarTop, t.getLocalizedMessage());
                    }
                    progressBarTop.setVisibility(View.GONE);
                    progressBarBottom.setVisibility(View.GONE);
                    Log.d("OkHttp", "t.getLocalizedMessage() " + t.getLocalizedMessage());
                }
            }
        });
    }

    private static void saveCurrentPage(int totalPage, PrefUtils prefUtils) {
        if(page < totalPage - 1) {
            prefUtils.saveNewPostCurrentPage(page + 1);
        } else {
            prefUtils.saveNewPostCurrentPage(totalPage - 1);
        }
    }

    private static void updatePostListAndNotifyRecyclerAdapter(List<PostsModel.PostDetails> newPostList, PostListAdapter adapter) {
        PostListByCategoryFromApi.getInstance().savePostByCategoryInList(newPostList);
        adapter.notifyDataSetChanged();
    }

}
