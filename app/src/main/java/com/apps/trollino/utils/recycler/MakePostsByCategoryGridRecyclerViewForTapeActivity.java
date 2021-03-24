package com.apps.trollino.utils.recycler;

import android.content.Context;
import android.os.Handler;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.trollino.adapters.PostListAdapter;
import com.apps.trollino.data.model.PostsModel;
import com.apps.trollino.ui.base.BaseActivity;
import com.apps.trollino.utils.data.PostListByCategoryFromApi;
import com.apps.trollino.utils.data.PrefUtils;
import com.apps.trollino.utils.networking.main_group.GetPostsByCategory;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;

import static com.apps.trollino.utils.OpenPostActivityHelper.openPostActivity;

public class MakePostsByCategoryGridRecyclerViewForTapeActivity extends RecyclerView.OnScrollListener{
    private static Context cont;
    private static PrefUtils prefUt;

    public static void makePostsByCategoryGridRecyclerViewForTapeActivity(Context context, PrefUtils prefUtils, RecyclerView recyclerView,
                                                                          ShimmerFrameLayout shimmer, SwipyRefreshLayout refreshLayout,
                                                                          View bottomNavigation, Boolean isNewData) {
        cont = context;
        prefUt = prefUtils;

        PostListAdapter adapter = new PostListAdapter((BaseActivity) cont, prefUtils, PostListByCategoryFromApi.getInstance().getPostListByCategory(), newPostsItemListener);
        recyclerView.setLayoutManager(new GridLayoutManager(cont, 2));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        infiniteScroll(adapter, recyclerView, shimmer, refreshLayout, bottomNavigation,isNewData);
        recyclerView.getLayoutManager().scrollToPosition(prefUtils.getCurrentAdapterPositionPosts());
    }

    // Обработка нажатия на элемент списка
    private static final PostListAdapter.OnItemClick<PostsModel.PostDetails> newPostsItemListener = (item, position) -> {
        openPostActivity(cont, item, prefUt, true);
    };

    // Загрузить/обновить данные с API
    private static void updateDataFromApi(PostListAdapter adapter, RecyclerView recyclerView,
                                          ShimmerFrameLayout shimmer, SwipyRefreshLayout refreshLayout,
                                          View bottomNavigation, boolean isScrollOnTop) {
        new Thread(() -> {
            GetPostsByCategory.getPostsByCategory(cont, prefUt, adapter, recyclerView, shimmer, refreshLayout, bottomNavigation, isScrollOnTop);
        }).start();
    }

    // Загрузить/обновить данные с API при скролах ресайклера вверх или вниз, если достигнут конец списка
    private static void infiniteScroll(PostListAdapter adapter, RecyclerView recyclerView,
                                       ShimmerFrameLayout shimmer, SwipyRefreshLayout refreshLayout,
                                       View bottomNavigation, boolean isScrollOnTop) {
        if (shimmer != null) {
            shimmer.setVisibility(isScrollOnTop ? View.VISIBLE : View.GONE);
        }
        Handler handler = new Handler();
        handler.postDelayed(() -> updateDataFromApi(adapter, recyclerView, shimmer, refreshLayout, bottomNavigation, isScrollOnTop), 1000);
    }
}
