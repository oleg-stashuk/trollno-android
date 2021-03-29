package com.apps.trollino.utils.recycler;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.trollino.adapters.FavoriteAdapter;
import com.apps.trollino.data.model.PostsModel;
import com.apps.trollino.ui.base.BaseActivity;
import com.apps.trollino.utils.RecyclerScrollListener;
import com.apps.trollino.utils.data.FavoritePostListFromApi;
import com.apps.trollino.utils.data.PrefUtils;
import com.apps.trollino.utils.networking.main_group.GetFavoriteList;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;

import static com.apps.trollino.utils.OpenPostActivityHelper.openPostActivity;

public class MakeLinerRecyclerViewForFavoriteActivity {
    private static Context cont;
    private static PrefUtils prefUt;

    public static void makeLinerRecyclerViewForFavoriteActivity(Context context, PrefUtils prefUtils,
                                                                RecyclerView recyclerView, ShimmerFrameLayout shimmer,
                                                                SwipyRefreshLayout refreshLayout, View noFavoriteListView,
                                                                View bottomNavigation, boolean isNewData, ProgressBar progressBar) {
        cont = context;
        prefUt = prefUtils;

        FavoriteAdapter adapter = new FavoriteAdapter((BaseActivity) context, prefUtils, FavoritePostListFromApi.getInstance().getFavoritePostList(), favoritePostItemListener);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.getLayoutManager().scrollToPosition(prefUtils.getCurrentAdapterPositionFavorite());

        infiniteScroll(recyclerView, shimmer, refreshLayout, adapter, noFavoriteListView, bottomNavigation, isNewData, progressBar);

        recyclerView.addOnScrollListener(new RecyclerScrollListener() {
            @Override
            public void onScrolledToEnd() {
                infiniteScroll(recyclerView, shimmer, refreshLayout, adapter, noFavoriteListView,
                        bottomNavigation, false, progressBar);
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    // Обработка нажатия на элемент списка
    private static final FavoriteAdapter.OnItemClick<PostsModel.PostDetails> favoritePostItemListener = (item, position) -> {
            openPostActivity(cont, item, prefUt, false);
    };

    // Загрузить/обновить данные с API
    private static void updateDataFromApi(RecyclerView recyclerView, ShimmerFrameLayout shimmer,
                                          SwipyRefreshLayout refreshLayout, FavoriteAdapter adapter,
                                          View noFavoriteListView, View bottomNavigation,
                                          boolean isGetNewList, ProgressBar progressBar) {
        new Thread(() -> {
            GetFavoriteList.getFavoritePosts(cont, prefUt, recyclerView, shimmer, refreshLayout,
                    noFavoriteListView, bottomNavigation, adapter, isGetNewList, progressBar);
        }).start();
    }

    // Загрузить/обновить данные с API при скролах ресайклера вверх или вниз, если достигнут конец списка
    private static void infiniteScroll(RecyclerView recyclerView,
                                       ShimmerFrameLayout shimmer, SwipyRefreshLayout refreshLayout,
                                       FavoriteAdapter adapter, View noFavoriteListView,
                                       View bottomNavigation, boolean isGetNewList, ProgressBar progressBar) {
        if (shimmer != null) {
            shimmer.setVisibility(isGetNewList ? View.VISIBLE : View.GONE);
        }
        Handler handler = new Handler();
        handler.postDelayed(() -> updateDataFromApi(recyclerView, shimmer, refreshLayout, adapter,
                noFavoriteListView, bottomNavigation, isGetNewList, progressBar), 1000);
    }

}
