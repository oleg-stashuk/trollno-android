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
import com.apps.trollino.utils.networking.main_group.GetPostsByCategory;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;

import static com.apps.trollino.utils.OpenPostActivityHelper.openPostActivity;

public class MakePostsByCategoryGridRecyclerViewForTapeActivity extends RecyclerView.OnScrollListener{
    @SuppressLint("StaticFieldLeak")
    private static Context cont;
    private static PrefUtils prefUt;
    private static boolean isGetNewList = false;

    public static void makePostsByCategoryGridRecycler(Context context, PrefUtils prefUtils, RecyclerView recyclerView,
                                                       ShimmerFrameLayout shimmer, SwipyRefreshLayout refreshLayout,
                                                       View bottomNavigation, ProgressBar progressBar) {
        cont = context;
        prefUt = prefUtils;
        String categoryId = prefUtils.getSelectedCategoryId();
        String categoryName = CategoryStoreProvider.getInstance(context).getCategoryById(categoryId).getNameCategory();

        PostListAdapter adapter = new PostListAdapter((BaseActivity) context, prefUtils,
                PostStoreProvider.getInstance(context).getPostByCategoryName(categoryName), postsItemListener);
        recyclerView.setLayoutManager(new GridLayoutManager(cont, 2));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        int savedPosition = CategoryStoreProvider.getInstance(context).getCategoryById(categoryId).getPostInCategory();
        recyclerView.getLayoutManager().scrollToPosition(savedPosition);


        if (shimmer != null || refreshLayout != null) {
            isGetNewList = true;
            if (shimmer != null) shimmer.setVisibility(View.VISIBLE);
            infiniteScroll(adapter, recyclerView, shimmer, refreshLayout, bottomNavigation, progressBar);
        }

        recyclerView.addOnScrollListener(new RecyclerScrollListener() {
            @Override
            public void onScrolledToEnd() {
                isGetNewList = false;
                recyclerView.getLayoutManager().scrollToPosition(prefUtils.getCurrentAdapterPositionPosts());
                infiniteScroll(adapter, recyclerView, shimmer, refreshLayout, bottomNavigation, progressBar);
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    // Обработка нажатия на элемент списка
    private static final PostListAdapter.OnItemClick<PostsModel.PostDetails> postsItemListener = (item, position) -> {
        openPostActivity(cont, item, prefUt, true);
    };

    // Загрузить/обновить данные с API при скролах ресайклера вверх или вниз, если достигнут конец списка
    private static void infiniteScroll(PostListAdapter adapter, RecyclerView recyclerView,
                                       ShimmerFrameLayout shimmer, SwipyRefreshLayout refreshLayout,
                                       View bottomNavigation, ProgressBar progressBar) {
        new Handler().postDelayed(() ->
                new Thread(() -> GetPostsByCategory.getPostsByCategory(cont, prefUt, adapter, recyclerView,
                        shimmer, refreshLayout, bottomNavigation, isGetNewList, progressBar)).start()
                , 1000);
    }
}
