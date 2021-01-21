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
import com.apps.trollino.utils.data.PrefUtils;
import com.apps.trollino.utils.networking.main_group.GetMostDiscusPosts;

import static com.apps.trollino.utils.OpenPostActivityHelper.openPostActivity;

public class MakeLinerRecyclerViewForTapeActivity {
    private static Context cont;
    private static PrefUtils prefUt;


    public static void makeLinerRecyclerViewForTapeActivity(Context context, RecyclerView recyclerView,
                                                            ProgressBar progressBarBottom, ProgressBar progressBarTop, PrefUtils prefUtils) {
        cont = context;
        prefUt = prefUtils;

        DiscussPostsAdapter adapter = new DiscussPostsAdapter((BaseActivity) context, prefUtils, DataListFromApi.getInstance().getDiscussPostsList(), newsVideoItemListener);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        new Thread(() -> {
            GetMostDiscusPosts.makeGetNewPosts(cont, prefUt, adapter, progressBarBottom, progressBarTop, true);
        }).start();

        recyclerView.addOnScrollListener(new RecyclerScrollListener() {
            @Override
            public void onScrolledToEnd() {
                infiniteScroll(progressBarBottom, progressBarTop, adapter,false);
            }

            @Override
            public void onScrolledToTop() {
                infiniteScroll(progressBarBottom, progressBarTop, adapter,true);
            }
        });
    }

    // Обработка нажатия на элемент списка
    private static final DiscussPostsAdapter.OnItemClick<PostsModel.PostDetails> newsVideoItemListener = (item, position) -> {
        openPostActivity(cont, item, prefUt, false);
    };


    // Загрузить/обновить данные с API
    private static void updateDataFromApi(ProgressBar progressBarBottom, ProgressBar progressBarTop, DiscussPostsAdapter adapter, boolean isScrollOnTop) {
        new Thread(() -> {
            GetMostDiscusPosts.makeGetNewPosts(cont, prefUt, adapter, progressBarBottom, progressBarTop, isScrollOnTop);
        }).start();
    }

    // Загрузить/обновить данные с API при скролах ресайклера вверх или вниз, если достигнут конец списка
    private static void infiniteScroll(ProgressBar progressBarBottom, ProgressBar progressBarTop, DiscussPostsAdapter adapter, boolean isScrollOnTop) {
        progressBarTop.setVisibility(isScrollOnTop ? View.VISIBLE : View.GONE);
        progressBarBottom.setVisibility(isScrollOnTop ? View.GONE : View.VISIBLE);
        Handler handler = new Handler();
        handler.postDelayed(() -> updateDataFromApi(progressBarBottom, progressBarTop, adapter, isScrollOnTop), 1000);
    }
}
