package com.apps.trollino.utils.networking.main_group;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.apps.trollino.data.model.PostsModel;
import com.apps.trollino.data.networking.ApiService;
import com.apps.trollino.utils.data.PrefUtils;
import com.apps.trollino.utils.networking_helper.ErrorMessageFromApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.apps.trollino.utils.Const.COUNT_TRY_REQUEST;

public class GetFavoriteList {
    private static Context cont;
    private static int page;

    public static void makeGetNewPosts(Context context, PrefUtils prefUtils) {
//    public static void makeGetNewPosts(Context context, PrefUtils prefUtils, PostListAdapter adapter, ProgressBar progressBar) {
        cont = context;
        page = prefUtils.getNewPostCurrentPage();
        String cookie = prefUtils.getCookie();

        ApiService.getInstance(context).getFavoritePostList(cookie, page, new Callback<PostsModel>() {
            int countTry = 0;

            @Override
            public void onResponse(Call<PostsModel> call, Response<PostsModel> response) {
                if (response.isSuccessful()) {
                    PostsModel post = response.body();
                    List<PostsModel.PostDetails> newPostList = post.getPostDetailsList();
                    Log.d("OkHttp", "isSuccessful");

//                    saveCurrentPage(post.getPagerModel().getTotalPages(), prefUtils);
//                    updatePostListAndNotifyRecyclerAdapter(newPostList, adapter);
                } else {
                    String errorMessage = ErrorMessageFromApi.errorMessageFromApi(response.errorBody());
                    showToast(errorMessage);
                }
//                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<PostsModel> call, Throwable t) {
                t.getStackTrace();
                if (countTry <= COUNT_TRY_REQUEST) {
                    call.clone().enqueue(this);
                    countTry++;
                } else {
                    showToast(t.getLocalizedMessage());
//                    progressBar.setVisibility(View.GONE);
                    Log.d("OkHttp", "t.getLocalizedMessage() " + t.getLocalizedMessage());
                }
            }
        });
    }

    private static void showToast(String message) {
        Toast.makeText(cont, message, Toast.LENGTH_SHORT).show();
    }

//    private static void saveCurrentPage(int totalPage, PrefUtils prefUtils) {
//        if(page < totalPage - 1) {
//            prefUtils.saveNewPostCurrentPage(page + 1);
//        } else {
//            prefUtils.saveNewPostCurrentPage(totalPage - 1);
//        }
//    }
//
//    private static void updatePostListAndNotifyRecyclerAdapter(List<PostsModel.PostDetails> newPostList, PostListAdapter adapter) {
//        DataListFromApi.getInstance().saveDataInList(newPostList);
//        adapter.notifyDataSetChanged();
//    }
}
