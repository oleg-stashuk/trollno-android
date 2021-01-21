package com.apps.trollino.utils.recycler;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.trollino.adapters.DiscussPostsAdapter;
import com.apps.trollino.data.model.PostsModel;
import com.apps.trollino.ui.base.BaseActivity;
import com.apps.trollino.utils.RecyclerScrollListener;
import com.apps.trollino.utils.data.DataListFromApi;
import com.apps.trollino.utils.data.PostListByCategoryFromApi;
import com.apps.trollino.utils.data.PrefUtils;
import com.apps.trollino.utils.networking.main_group.GetMostDiscusPosts;
import com.facebook.shimmer.ShimmerFrameLayout;

import static com.apps.trollino.utils.OpenPostActivityHelper.openPostActivity;

public class MakeLinerRecyclerViewForTapeActivity {
    private static Context cont;
    private static PrefUtils prefUt;


    public static void makeLinerRecyclerViewForTapeActivity(Context context, PrefUtils prefUtils,
                                                            RecyclerView recyclerView, ShimmerFrameLayout shimmer,
                                                            ProgressBar progressBar) {
        cont = context;
        prefUt = prefUtils;

        DiscussPostsAdapter adapter = new DiscussPostsAdapter((BaseActivity) context, prefUtils, DataListFromApi.getInstance().getDiscussPostsList(), newsVideoItemListener);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        if(PostListByCategoryFromApi.getInstance().getPostListByCategory().isEmpty()) {
            infiniteScroll(recyclerView, shimmer, progressBar, adapter,true);
        }

        recyclerView.addOnScrollListener(new RecyclerScrollListener() {
            @Override
            public void onScrolledToEnd() {
//                infiniteScroll(recyclerView, shimmer, progressBar, adapter,false);
                infiniteScroll(recyclerView, shimmer, progressBar, adapter,true);
            }

            @Override
            public void onScrolledToTop() {
                infiniteScroll(recyclerView, shimmer, progressBar, adapter,true);
            }
        });
    }

    // Обработка нажатия на элемент списка
    private static final DiscussPostsAdapter.OnItemClick<PostsModel.PostDetails> newsVideoItemListener = (item, position) -> {
        openPostActivity(cont, item, prefUt, false);
    };


    // Загрузить/обновить данные с API
    private static void updateDataFromApi(RecyclerView recyclerView, ShimmerFrameLayout shimmer,
                                          ProgressBar progressBar, DiscussPostsAdapter adapter, boolean isGetNewList) {
        new Thread(() -> {
            GetMostDiscusPosts.makeGetNewPosts(cont, prefUt, adapter, recyclerView, shimmer, progressBar, isGetNewList);
        }).start();
    }

    // Загрузить/обновить данные с API при скролах ресайклера вверх или вниз, если достигнут конец списка
    private static void infiniteScroll(RecyclerView recyclerView, ShimmerFrameLayout shimmer,
                                       ProgressBar progressBar, DiscussPostsAdapter adapter, boolean isGetNewList) {
        progressBar.setVisibility(isGetNewList ? View.GONE : View.VISIBLE);
        shimmer.setVisibility(isGetNewList ? View.VISIBLE : View.GONE);
        Handler handler = new Handler();
        handler.postDelayed(() -> updateDataFromApi(recyclerView, shimmer, progressBar, adapter, isGetNewList), 1000);
    }
}
