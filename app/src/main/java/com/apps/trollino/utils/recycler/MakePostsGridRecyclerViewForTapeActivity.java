package com.apps.trollino.utils.recycler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.trollino.adapters.PostListAdapter;
import com.apps.trollino.data.model.PostsModel;
import com.apps.trollino.ui.base.BaseActivity;
import com.apps.trollino.ui.main_group.PostActivity;
import com.apps.trollino.utils.RecyclerScrollListener;
import com.apps.trollino.utils.data.PostListByCategoryFromApi;
import com.apps.trollino.utils.data.PrefUtils;
import com.apps.trollino.utils.networking.GetPostsByCategory;

public class MakePostsGridRecyclerViewForTapeActivity extends RecyclerView.OnScrollListener{
    private static Context cont;

    public static void makePostsGridRecyclerViewForTapeActivity(Context context, RecyclerView recyclerView, ProgressBar progressBar, PrefUtils prefUtils) {
        cont = context;

        PostListAdapter adapter = new PostListAdapter((BaseActivity) cont, PostListByCategoryFromApi.getInstance().getPostListByCategory(), newPostsItemListener);
        recyclerView.setLayoutManager(new GridLayoutManager(cont, 2));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        if(PostListByCategoryFromApi.getInstance().getPostListByCategory().isEmpty()) {
            new Thread(() -> {
                GetPostsByCategory.getPostsByCategory(cont, prefUtils, adapter, progressBar);
            }).start();
        }

        recyclerView.addOnScrollListener(new RecyclerScrollListener() {
            @Override
            public void onScrolledToEnd() {
                progressBar.setVisibility(View.VISIBLE);
                Handler handler = new Handler();
                handler.postDelayed(() -> new Thread(() -> {
                    GetPostsByCategory.getPostsByCategory(cont, prefUtils, adapter, progressBar);
                }).start(), 1000);
            }
        });
    }

    // Обработка нажатия на элемент списка
    private static final PostListAdapter.OnItemClick<PostsModel.PostDetails> newPostsItemListener = (item, position) -> {
        Log.d("OkHttp", "Pressed " + item.getPostId() + " category " + item.getCategoryName());
        cont.startActivity(new Intent(cont, PostActivity.class));
        ((Activity) cont).finish();
    };
}
