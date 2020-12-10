package com.apps.trollino.utils.networking;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.apps.trollino.adapters.DiscussPostsAdapter;
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

    public static void makeGetNewPosts(Context context, PrefUtils prefUtils, DiscussPostsAdapter adapter, ProgressBar progressBar) {
        cont = context;
        String cookie = prefUtils.getCookie();

        ApiService.getInstance(context).getMostDiscusPosts(cookie, 0, new Callback<PostsModel>() {
            int countTry = 0;

            @Override
            public void onResponse(Call<PostsModel> call, Response<PostsModel> response) {
                if (response.isSuccessful()) {
                    PostsModel post = response.body();
                    List<PostsModel.PostDetails> postList = post.getPostDetailsList();
                    updatePostListAndNotifyRecyclerAdapter(postList, adapter);
                } else {
                    String errorMessage = ErrorMessageFromApi.errorMessageFromApi(response.errorBody());
                    showToast(errorMessage);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<PostsModel> call, Throwable t) {
                t.getStackTrace();
                if (countTry <= COUNT_TRY_REQUEST) {
                    call.clone().enqueue(this);
                    countTry++;
                } else {
                    showToast(t.getLocalizedMessage());
                    progressBar.setVisibility(View.GONE);
                    Log.d("OkHttp", "t.getLocalizedMessage() " + t.getLocalizedMessage());
                }
            }
        });
    }

    private static void showToast(String message) {
        Toast.makeText(cont, message, Toast.LENGTH_SHORT).show();
    }

    private static void updatePostListAndNotifyRecyclerAdapter(List<PostsModel.PostDetails> newPostList, DiscussPostsAdapter adapter) {
        DataListFromApi.getInstance().saveDiscussDataInList(newPostList);
        adapter.notifyDataSetChanged();
    }
}
