package com.apps.trollino.utils.recycler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.trollino.adapters.DiscussPostsAdapter;
import com.apps.trollino.data.model.PostsModel;
import com.apps.trollino.db_room.category.CategoryStoreProvider;
import com.apps.trollino.db_room.posts.PostStoreProvider;
import com.apps.trollino.ui.base.BaseActivity;
import com.apps.trollino.utils.data.PrefUtils;
import com.apps.trollino.utils.networking.main_group.GetMostDiscusPosts;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;

import static com.apps.trollino.utils.OpenPostActivityHelper.openPostActivity;
import static com.apps.trollino.utils.data.Const.CATEGORY_DISCUSSED;

public class MakeLinerRecyclerViewForTapeActivity {
    @SuppressLint("StaticFieldLeak")
    private static Context cont;
    private static PrefUtils prefUt;

    public static void makeLinerRecyclerViewForTapeActivity(Context context, PrefUtils prefUtils,
                                                            RecyclerView recyclerView, ShimmerFrameLayout shimmer,
                                                            SwipyRefreshLayout refreshLayout, View bottomNavigation) {
        cont = context;
        prefUt = prefUtils;

        DiscussPostsAdapter adapter = new DiscussPostsAdapter((BaseActivity) context, prefUtils,
                PostStoreProvider.getInstance(context).getPostByCategoryName(CATEGORY_DISCUSSED), discussedItemListener);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        int savedDiscussedPosition = CategoryStoreProvider.getInstance(context).getCategoryById(CATEGORY_DISCUSSED).getPostInCategory();
        recyclerView.getLayoutManager().scrollToPosition(savedDiscussedPosition);

        if (shimmer != null || refreshLayout != null) {
            if (shimmer != null) shimmer.setVisibility(View.VISIBLE);
            infiniteScroll(recyclerView, shimmer, refreshLayout, bottomNavigation, adapter);
        }
    }

    // Обработка нажатия на элемент списка
    private static final DiscussPostsAdapter.OnItemClick<PostsModel.PostDetails> discussedItemListener = (item, position) -> {
        openPostActivity(cont, item, prefUt, false);
    };

    // Загрузить/обновить данные с API при скролах ресайклера вверх или вниз, если достигнут конец списка
    private static void infiniteScroll(RecyclerView recyclerView, ShimmerFrameLayout shimmer,
                                       SwipyRefreshLayout refreshLayout, View bottomNavigation,
                                       DiscussPostsAdapter adapter) {
        new Handler().postDelayed(() ->
                new Thread(() -> GetMostDiscusPosts.makeGetNewPosts(cont, prefUt, adapter,
                            recyclerView, shimmer, refreshLayout, bottomNavigation)).start()
                , 1000);
    }
}
