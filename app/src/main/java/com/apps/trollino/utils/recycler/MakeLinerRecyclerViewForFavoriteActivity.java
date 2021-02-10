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

import static com.apps.trollino.utils.OpenPostActivityHelper.openPostActivity;

public class MakeLinerRecyclerViewForFavoriteActivity extends RecyclerView.OnScrollListener{
    private static Context cont;
    private static PrefUtils prefUt;

    public static void makeLinerRecyclerViewForFavoriteActivity(Context context, PrefUtils prefUtils,
                                                                RecyclerView recyclerView, ShimmerFrameLayout shimmer,
                                                                ProgressBar progressBar, View noFavoriteListView, View bottomNavigation) {
        cont = context;
        prefUt = prefUtils;

        FavoriteAdapter adapter = new FavoriteAdapter((BaseActivity) context, prefUtils, FavoritePostListFromApi.getInstance().getFavoritePostLis(), favoritePostItemListener);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        if (FavoritePostListFromApi.getInstance().getFavoritePostLis().isEmpty()) {
            infiniteScroll(progressBar, recyclerView, shimmer, adapter, noFavoriteListView, bottomNavigation, true);
        }

        recyclerView.addOnScrollListener(new RecyclerScrollListener() {
            @Override
            public void onScrolledToEnd() {
                infiniteScroll(progressBar, recyclerView, shimmer, adapter, noFavoriteListView, bottomNavigation, false);
            }

            @Override
            public void onScrolledToTop() {
                infiniteScroll(progressBar, recyclerView, shimmer, adapter, noFavoriteListView, bottomNavigation, true);
            }
        });
    }

    // Обработка нажатия на элемент списка
    private static final FavoriteAdapter.OnItemClick<PostsModel.PostDetails> favoritePostItemListener = (item, position) -> {
            openPostActivity(cont, item, prefUt, false);
    };



    // Загрузить/обновить данные с API
    private static void updateDataFromApi(ProgressBar progressBar, RecyclerView recyclerView,
                                          ShimmerFrameLayout shimmer, FavoriteAdapter adapter,
                                          View noFavoriteListView, View bottomNavigation, boolean isGetNewList) {
        new Thread(() -> {
            GetFavoriteList.getFavoritePosts(cont, prefUt, recyclerView, shimmer, progressBar, noFavoriteListView, bottomNavigation, adapter, isGetNewList);
        }).start();
    }

    // Загрузить/обновить данные с API при скролах ресайклера вверх или вниз, если достигнут конец списка
    private static void infiniteScroll(ProgressBar progressBar, RecyclerView recyclerView,
                                       ShimmerFrameLayout shimmer, FavoriteAdapter adapter, View noFavoriteListView,
                                       View bottomNavigation, boolean isGetNewList) {
        progressBar.setVisibility(isGetNewList ? View.GONE : View.VISIBLE);
        shimmer.setVisibility(isGetNewList ? View.VISIBLE : View.GONE);
        Handler handler = new Handler();
        handler.postDelayed(() -> updateDataFromApi(progressBar, recyclerView, shimmer, adapter, noFavoriteListView, bottomNavigation, isGetNewList), 1000);
    }

}
