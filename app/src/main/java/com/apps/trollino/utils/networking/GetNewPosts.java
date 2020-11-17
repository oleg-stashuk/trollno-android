package com.apps.trollino.utils.networking;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.trollino.adapters.PostListAdapter;
import com.apps.trollino.adapters.base.BaseRecyclerAdapter;
import com.apps.trollino.data.model.PostsModel;
import com.apps.trollino.data.networking.ApiService;
import com.apps.trollino.ui.base.BaseActivity;
import com.apps.trollino.ui.main_group.PostActivity;
import com.apps.trollino.utils.PrefUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.apps.trollino.utils.Const.COUNT_TRY_REQUEST;

public class GetNewPosts {
    private static Context cont;
    private static int page;


    public static void makeGetNewPosts(Context context, RecyclerView recyclerView, PrefUtils prefUtils) {
        cont = context;
        page = prefUtils.getNewPostCurrentPage();

        ApiService.getInstance().getNewPosts("", page, new Callback<PostsModel>() {
            int countTry = 0;

            @Override
            public void onResponse(Call<PostsModel> call, Response<PostsModel> response) {
                if (response.isSuccessful()) {
                    Log.d("OkHttp", "response.body(): " + response.body().getPostDetailsList().toString());

                    PostsModel post = response.body();
                    List<PostsModel.PostDetails> newPostList = post.getPostDetailsList();
                    Log.d("OkHttp", "page: " + post.getPager().toString());

                    makeNewPostsRecyclerView(recyclerView, newPostList);
                    if(page != post.getPager().getTotalPages() - 1) {
                        prefUtils.saveNewPostCurrentPage(page += 1);
                    } else {
                        prefUtils.saveNewPostCurrentPage(post.getPager().getTotalPages() - 1);
                    }
                } else {
                    Log.d("OkHttp", "response.errorBody() " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<PostsModel> call, Throwable t) {
                t.getStackTrace();
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

    private static void showToast(String message) {
        Toast.makeText(cont, message, Toast.LENGTH_SHORT).show();
    }

    private static void makeNewPostsRecyclerView(RecyclerView recyclerView, List<PostsModel.PostDetails> newPostList) {
        recyclerView.setLayoutManager(new GridLayoutManager(cont, 2));
        recyclerView.setAdapter(new PostListAdapter((BaseActivity) cont, newPostList, newPostsItemListener));
    }

    // Обработка нажатия на элемент списка
    private static final PostListAdapter.OnItemClick<PostsModel.PostDetails> newPostsItemListener = new BaseRecyclerAdapter.OnItemClick<PostsModel.PostDetails>() {
        @Override
        public void onItemClick(PostsModel.PostDetails item, int position) {
            Log.d("OkHttp", "Pressed " + item.getPostId() + " category " + item.getCategoryName());
            cont.startActivity(new Intent(cont, PostActivity.class));
            ((Activity) cont).finish();
        }
    };

}
