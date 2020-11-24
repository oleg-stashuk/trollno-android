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
import com.apps.trollino.utils.data.PrefUtils;
import com.apps.trollino.utils.RecyclerScrollListener;
import com.apps.trollino.utils.data.DataListFromApi;

import static com.apps.trollino.ui.main_group.PostActivity.POST_CATEGORY_KEY;
import static com.apps.trollino.ui.main_group.PostActivity.POST_FAVORITE_VALUE;
import static com.apps.trollino.ui.main_group.PostActivity.POST_ID_KEY;
import static com.apps.trollino.utils.OpenPostActivityHelper.openPostActivity;
import static com.apps.trollino.utils.networking.GetNewPosts.makeGetNewPosts;
public class MakeGridRecyclerViewForTapeActivity extends RecyclerView.OnScrollListener{
    private static Context cont;

    public static void makeNewPostsRecyclerView(Context context, RecyclerView recyclerView, ProgressBar progressBar, PrefUtils prefUtils) {
        cont = context;
        DataListFromApi.getInstance().removeAllDataFromList(prefUtils);

        PostListAdapter adapter = new PostListAdapter((BaseActivity) cont, DataListFromApi.getInstance().getNewPostsList(), newPostsItemListener);
        recyclerView.setLayoutManager(new GridLayoutManager(cont, 2));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        if(DataListFromApi.getInstance().getNewPostsList().isEmpty()) {
            new Thread(() -> {
                makeGetNewPosts(cont, prefUtils, adapter, progressBar);
            }).start();
        }

        recyclerView.addOnScrollListener(new RecyclerScrollListener() {
            @Override
            public void onScrolledToEnd() {
                progressBar.setVisibility(View.VISIBLE);
                Handler handler = new Handler();
                handler.postDelayed(() -> new Thread(() -> {
                    makeGetNewPosts(cont, prefUtils, adapter, progressBar);
                }).start(), 1000);
            }
        });
    }

    // Обработка нажатия на элемент списка
    private static final PostListAdapter.OnItemClick<PostsModel.PostDetails> newPostsItemListener = (item, position) -> {
        openPostActivity(cont, item);
    };
}
