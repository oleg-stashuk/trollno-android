package com.apps.trollino.utils.recycler;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.trollino.adapters.PostListAdapter;
import com.apps.trollino.data.model.PostsModel;
import com.apps.trollino.ui.base.BaseActivity;
import com.apps.trollino.utils.RecyclerScrollListener;
import com.apps.trollino.utils.data.DataListFromApi;
import com.apps.trollino.utils.data.PrefUtils;
import com.facebook.shimmer.ShimmerFrameLayout;

import static com.apps.trollino.utils.OpenPostActivityHelper.openPostActivity;
import static com.apps.trollino.utils.networking.main_group.GetNewPosts.makeGetNewPosts;

public class MakeGridRecyclerViewForTapeActivity {
    private static Context cont;
    private static PrefUtils prefUt;

public static void makeNewPostsRecyclerView(Context context, PrefUtils prefUtils, RecyclerView recyclerView,
                                            ProgressBar progressBar, ShimmerFrameLayout shimmer) {
        cont = context;
        prefUt = prefUtils;
        DataListFromApi.getInstance().removeAllDataFromList(prefUtils);

        PostListAdapter adapter = new PostListAdapter((BaseActivity) cont, prefUtils, DataListFromApi.getInstance().getNewPostsList(), newPostsItemListener);
        recyclerView.setLayoutManager(new GridLayoutManager(cont, 2));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        if(DataListFromApi.getInstance().getNewPostsList().isEmpty()) {
            new Thread(() -> {
                makeGetNewPosts(cont, prefUtils, adapter, progressBar, recyclerView, shimmer, true);
            }).start();
        }

        recyclerView.addOnScrollListener(new RecyclerScrollListener() {
            @Override
            public void onScrolledToEnd() {
                infiniteScroll(progressBar, recyclerView, shimmer, adapter,false);
            }

            @Override
            public void onScrolledToTop() {
                infiniteScroll(progressBar, recyclerView, shimmer, adapter,true);
            }
        });
    }

    // Обработка нажатия на элемент списка
    private static final PostListAdapter.OnItemClick<PostsModel.PostDetails> newPostsItemListener = (item, position) -> {
        openPostActivity(cont, item, prefUt, false);
    };

    // Загрузить/обновить данные с API
    private static void updateDataFromApi(ProgressBar progressBar, RecyclerView recyclerView, ShimmerFrameLayout shimmer, PostListAdapter adapter, boolean isScrollOnTop) {
        new Thread(() -> {
            makeGetNewPosts(cont, prefUt, adapter, progressBar, recyclerView, shimmer, isScrollOnTop);
        }).start();
    }

    // Загрузить/обновить данные с API при скролах ресайклера вверх или вниз, если достигнут конец списка
    private static void infiniteScroll(ProgressBar progressBar, RecyclerView recyclerView, ShimmerFrameLayout shimmer, PostListAdapter adapter, boolean isScrollOnTop) {
        progressBar.setVisibility(isScrollOnTop? View.GONE : View.VISIBLE);
        shimmer.setVisibility(isScrollOnTop ? View.VISIBLE : View.GONE);
        Handler handler = new Handler();
        handler.postDelayed(() -> updateDataFromApi(progressBar, recyclerView, shimmer, adapter, isScrollOnTop), 1000);
    }
}
