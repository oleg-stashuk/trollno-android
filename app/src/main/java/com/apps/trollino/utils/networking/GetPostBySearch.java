package com.apps.trollino.utils.networking;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.apps.trollino.adapters.PostListAdapter;
import com.apps.trollino.data.model.PostsModel;
import com.apps.trollino.data.networking.ApiService;
import com.apps.trollino.utils.data.PostListBySearchFromApi;
import com.apps.trollino.utils.data.PrefUtils;
import com.apps.trollino.utils.networking_helper.ErrorMessageFromApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.apps.trollino.utils.Const.COUNT_TRY_REQUEST;

public class GetPostBySearch {
    private static Context cont;
    private static int page;

    public static void getPostBySearch(Context context, PrefUtils prefUtils, String searchText, View nothingSearch, ProgressBar progressBar, PostListAdapter adapter) {
        cont = context;
        String cookie = prefUtils.getCookie();
        page = prefUtils.getNewPostCurrentPage();

        ApiService.getInstance(context).getSearchPosts(cookie, searchText, page, new Callback<PostsModel>() {
            int countTry = 0;

            @Override
            public void onResponse(Call<PostsModel> call, Response<PostsModel> response) {
                if(response.isSuccessful()) {
                    PostsModel postsModel = response.body();

                    PostsModel.Pager pager = postsModel.getPager();
                    if(pager.getTotalItems() == 0) {
                        nothingSearch.setVisibility(View.VISIBLE);
                    } else {
                        nothingSearch.setVisibility(View.GONE);
                        List<PostsModel.PostDetails> newPostList = postsModel.getPostDetailsList();

                        saveCurrentPage(postsModel.getPager().getTotalPages(), prefUtils);
                        updatePostListAndNotifyRecyclerAdapter(newPostList, adapter);
                    }

                } else {
                    String errorMessage = ErrorMessageFromApi.errorMessageFromApi(response.errorBody());
                    showToast(errorMessage);
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
                    showToast(t.getLocalizedMessage());
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

    private static void showToast(String message) {
        Toast.makeText(cont, message, Toast.LENGTH_SHORT).show();
    }
}
