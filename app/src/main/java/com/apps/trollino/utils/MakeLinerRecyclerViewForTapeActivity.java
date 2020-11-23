package com.apps.trollino.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.trollino.adapters.DiscussPostsAdapter;
import com.apps.trollino.data.model.PostsModel;
import com.apps.trollino.ui.base.BaseActivity;
import com.apps.trollino.ui.main_group.PostActivity;
import com.apps.trollino.utils.networking.GetMostDiscusPosts;

public class MakeLinerRecyclerViewForTapeActivity{
    private static Context cont;

    public static void makeLinerRecyclerViewForTapeActivity(Context context, RecyclerView recyclerView, ProgressBar progressBar, PrefUtils prefUtils) {
        cont = context;

        DataListFromApi.getInstance().removeAllDataFromList(prefUtils);

        DiscussPostsAdapter adapter = new DiscussPostsAdapter((BaseActivity) context, DataListFromApi.getInstance().getNewPostsList(), newsVideoItemListener);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        new Thread(() -> {
            GetMostDiscusPosts.makeGetNewPosts(context, prefUtils, adapter, progressBar);
        }).start();
    }

    // Обработка нажатия на элемент списка
    private static final DiscussPostsAdapter.OnItemClick<PostsModel.PostDetails> newsVideoItemListener =
        (item, position) -> {
            Log.d("OkHttp", "Pressed " + item.getPostId() + " category " + item.getCategoryName());
            cont.startActivity(new Intent(cont, PostActivity.class));
            ((Activity) cont).finish();
        };

}
