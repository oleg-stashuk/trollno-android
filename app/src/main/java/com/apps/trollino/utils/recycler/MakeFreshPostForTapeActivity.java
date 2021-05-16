package com.apps.trollino.utils.recycler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.trollino.adapters.PostListAdapter;
import com.apps.trollino.data.model.PostsModel;
import com.apps.trollino.db_room.category.CategoryStoreProvider;
import com.apps.trollino.db_room.posts.PostStoreProvider;
import com.apps.trollino.ui.base.BaseActivity;
import com.apps.trollino.utils.RecyclerScrollListener;
import com.apps.trollino.utils.data.PrefUtils;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;

import java.util.Objects;

import static com.apps.trollino.utils.OpenPostActivityHelper.openPostActivity;
import static com.apps.trollino.utils.data.Const.CATEGORY_FRESH;
import static com.apps.trollino.utils.networking.main_group.GetNewPosts.makeGetNewPosts;

public class MakeFreshPostForTapeActivity {
    @SuppressLint("StaticFieldLeak")
    private static Context cont;
    private static PrefUtils prefUt;
    private static boolean isGetNewList = false;

    public static void makeNewPostsRecyclerView(Context context, PrefUtils prefUtils, RecyclerView recyclerView,
                                            ShimmerFrameLayout shimmer, SwipyRefreshLayout refreshLayout,
                                            View bottomNavigation, ProgressBar progressBar) {
        cont = context;
        prefUt = prefUtils;

        PostListAdapter adapter = new PostListAdapter((BaseActivity) cont, prefUtils,
                PostStoreProvider.getInstance(context).getPostByCategoryName(CATEGORY_FRESH), newPostsItemListener);
        recyclerView.setLayoutManager(new GridLayoutManager(cont, 2));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        int savedPostPosition = CategoryStoreProvider.getInstance(context).getCategoryById(CATEGORY_FRESH).getPostInCategory();
        Objects.requireNonNull(recyclerView.getLayoutManager()).scrollToPosition(
                savedPostPosition > 2 ? savedPostPosition - 2 : savedPostPosition);

        if (shimmer != null || refreshLayout != null) {
            isGetNewList = true;
            if (shimmer != null ) shimmer.setVisibility(View.VISIBLE);
            infiniteScroll(recyclerView, shimmer, refreshLayout, bottomNavigation, adapter, progressBar);
        }

        recyclerView.addOnScrollListener(new RecyclerScrollListener() {
            @Override
            public void onScrolledToEnd() {
                isGetNewList = false;
                progressBar.setVisibility(View.VISIBLE);
                infiniteScroll(recyclerView, shimmer, refreshLayout, bottomNavigation, adapter, progressBar);
            }
        });
    }

    // Обработка нажатия на элемент списка
    private static final PostListAdapter.OnItemClick<PostsModel.PostDetails> newPostsItemListener = (item, position) -> {
        openPostActivity(cont, item, prefUt, false);
    };

    // Загрузить/обновить данные с API при скролах ресайклера вверх или вниз, если достигнут конец списка
    private static void infiniteScroll(RecyclerView recyclerView, ShimmerFrameLayout shimmer, SwipyRefreshLayout refreshLayout,
                                       View bottomNavigation, PostListAdapter adapter, ProgressBar progressBar) {
        // Загрузить/обновить данные с API
        new Handler().postDelayed(() ->
                        new Thread(() -> makeGetNewPosts(cont, prefUt, adapter, recyclerView, shimmer, refreshLayout,
                                bottomNavigation, isGetNewList, progressBar)).start()
                , 1000);
    }
}
