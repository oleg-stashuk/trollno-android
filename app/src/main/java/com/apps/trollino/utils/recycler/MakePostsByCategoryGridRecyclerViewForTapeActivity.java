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
import com.apps.trollino.utils.data.PostListByCategoryFromApi;
import com.apps.trollino.utils.data.PrefUtils;
import com.apps.trollino.utils.networking.main_group.GetPostsByCategory;

import static com.apps.trollino.utils.OpenPostActivityHelper.openPostActivity;

public class MakePostsByCategoryGridRecyclerViewForTapeActivity extends RecyclerView.OnScrollListener{
    private static Context cont;
    private static PrefUtils prefUt;

    public static void makePostsByCategoryGridRecyclerViewForTapeActivity(Context context, RecyclerView recyclerView,
                                                                          ProgressBar progressBarBottom, ProgressBar progressBarTop, PrefUtils prefUtils) {
        cont = context;
        prefUt = prefUtils;

        PostListAdapter adapter = new PostListAdapter((BaseActivity) cont, prefUtils, PostListByCategoryFromApi.getInstance().getPostListByCategory(), newPostsItemListener);
        recyclerView.setLayoutManager(new GridLayoutManager(cont, 2));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        if(PostListByCategoryFromApi.getInstance().getPostListByCategory().isEmpty()) {
            new Thread(() -> {
                GetPostsByCategory.getPostsByCategory(cont, prefUtils, adapter, progressBarBottom, progressBarTop, true);
            }).start();
        }

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
    private static final PostListAdapter.OnItemClick<PostsModel.PostDetails> newPostsItemListener = (item, position) -> {
        openPostActivity(cont, item, prefUt, true);
    };

    // Загрузить/обновить данные с API
    private static void updateDataFromApi(ProgressBar progressBarBottom, ProgressBar progressBarTop, PostListAdapter adapter, boolean isScrollOnTop) {
        new Thread(() -> {
            GetPostsByCategory.getPostsByCategory(cont, prefUt, adapter, progressBarBottom, progressBarTop, isScrollOnTop);
        }).start();
    }

    // Загрузить/обновить данные с API при скролах ресайклера вверх или вниз, если достигнут конец списка
    private static void infiniteScroll(ProgressBar progressBarBottom, ProgressBar progressBarTop, PostListAdapter adapter, boolean isScrollOnTop) {
        progressBarTop.setVisibility(isScrollOnTop ? View.VISIBLE : View.GONE);
        progressBarBottom.setVisibility(isScrollOnTop ? View.GONE : View.VISIBLE);
        Handler handler = new Handler();
        handler.postDelayed(() -> updateDataFromApi(progressBarBottom, progressBarTop, adapter, isScrollOnTop), 1000);
    }
}
