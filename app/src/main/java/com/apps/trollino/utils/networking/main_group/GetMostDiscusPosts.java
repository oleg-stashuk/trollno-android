package com.apps.trollino.utils.networking.main_group;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.apps.trollino.adapters.DiscussPostsAdapter;
import com.apps.trollino.data.model.PagerModel;
import com.apps.trollino.data.model.PostsModel;
import com.apps.trollino.data.networking.ApiService;
import com.apps.trollino.utils.data.DataListFromApi;
import com.apps.trollino.utils.data.PrefUtils;
import com.apps.trollino.utils.networking_helper.ErrorMessageFromApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.apps.trollino.utils.Const.COUNT_TRY_REQUEST;

public class GetMostDiscusPosts {
    private static Context cont;
    private static int page;

    public static void makeGetNewPosts(Context context, PrefUtils prefUtils, DiscussPostsAdapter adapter,
                                       ProgressBar progressBarBottom, ProgressBar progressBarTop, boolean scrollOnTop) {
        cont = context;
        page = scrollOnTop ? 0 : prefUtils.getCurrentPage();
        String cookie = prefUtils.getCookie();

        ApiService.getInstance(context).getMostDiscusPosts(cookie, page, new Callback<PostsModel>() {
            int countTry = 0;

            @Override
            public void onResponse(Call<PostsModel> call, Response<PostsModel> response) {
                if (response.isSuccessful()) {
                    PostsModel post = response.body();
                    List<PostsModel.PostDetails> newPostList = post.getPostDetailsList();
                    PagerModel pagerModel = post.getPagerModel();

                    String lastIdInListFromApi = newPostList.isEmpty() ? "null" : newPostList.get(newPostList.size()-1).getPostId();
                    boolean isLastPage = pagerModel.getCurrentPage() == pagerModel.getTotalPages()-1;

                    List<PostsModel.PostDetails> savedPostList = DataListFromApi.getInstance().getDiscussPostsList();
                    String firstIdInSavedList = savedPostList.isEmpty() ? "null" : savedPostList.get(0).getPostId();
                    String lastIdInSavedList = savedPostList.isEmpty() ? "null" : savedPostList.get(newPostList.size() - 1).getPostId();

                    if(newPostList.isEmpty()) {
                        showToast("Новых постов пока нет!!!!!!!!!!!!!!!!!!!!!!");
                    } else if (newPostList.get(0).getPostId().equals(firstIdInSavedList) && scrollOnTop) {
                        showToast("Новых постов пока нет");
                    } else if(lastIdInListFromApi.equals(lastIdInSavedList) && isLastPage) {
                        showToast("Новых постов пока нет");
                    } else {
                        if(scrollOnTop) {
                            DataListFromApi.getInstance().removeAllDataFromList(prefUtils);
                        }
                        saveCurrentPage(post.getPagerModel().getTotalPages(), prefUtils);
                        updatePostListAndNotifyRecyclerAdapter(newPostList, adapter);
                    }
                } else {
                    String errorMessage = ErrorMessageFromApi.errorMessageFromApi(response.errorBody());
                    showToast(errorMessage);
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
                    showToast(t.getLocalizedMessage());
                    progressBarTop.setVisibility(View.GONE);
                    progressBarBottom.setVisibility(View.GONE);
                    Log.d("OkHttp", "t.getLocalizedMessage() " + t.getLocalizedMessage());
                }
            }
        });
    }

    private static void showToast(String message) {
        Toast.makeText(cont, message, Toast.LENGTH_SHORT).show();
    }

    private static void saveCurrentPage(int totalPage, PrefUtils prefUtils) {
        if(page < totalPage - 1) {
            prefUtils.saveCurrentPage(page + 1);
        } else {
            prefUtils.saveCurrentPage(totalPage - 1);
        }
    }

    private static void updatePostListAndNotifyRecyclerAdapter(List<PostsModel.PostDetails> newPostList, DiscussPostsAdapter adapter) {
        DataListFromApi.getInstance().saveDiscussDataInList(newPostList);
        adapter.notifyDataSetChanged();
    }
}
