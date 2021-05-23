package com.apps.trollino.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.trollino.R;
import com.apps.trollino.adapters.base.PostByCategoryAdapter;
import com.apps.trollino.data.model.PostsModel;
import com.apps.trollino.db_room.category.CategoryStoreProvider;
import com.apps.trollino.db_room.posts.PostStoreProvider;
import com.apps.trollino.ui.fragment.base.BaseFragment;
import com.apps.trollino.utils.RecyclerScrollListener;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;

import java.util.List;
import java.util.Objects;

import static com.apps.trollino.utils.OpenPostActivityHelper.openPostActivity;
import static com.apps.trollino.utils.data.Const.TAG_LOG;
import static com.apps.trollino.utils.networking.main_group.GetPostsByCategory.getPostsByCategory;

public class FirstCategoryPostFragment extends BaseFragment {
    private ShimmerFrameLayout shimmer;
    private SwipyRefreshLayout refreshLayout;
    private RecyclerView recycler;
    private ProgressBar progressBar;

    private PostByCategoryAdapter adapter;
    private List<PostsModel.PostDetails> postsList;
    private String categoryId;
    private String categoryName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_first_category_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        shimmer = view.findViewById(R.id.include_shimmer_post_two_column);
        refreshLayout = view.findViewById(R.id.refresh_first_category);
        recycler = view.findViewById(R.id.recycler_first_category);
        progressBar = view.findViewById(R.id.progress_first_category);
    }

    @Override
    protected void initView() {
        categoryId = CategoryStoreProvider.getInstance(context).getCategoryList().get(2).getIdCategory();
        categoryName = CategoryStoreProvider.getInstance(context).getCategoryList().get(2).getNameCategory();
        Log.d(TAG_LOG, "FirstCategoryPostFragment " + categoryId + " " + categoryName);
        postsList = PostStoreProvider.getInstance(context).getPostByCategoryName(categoryName);

        createAdapter();
        updateDataBySwipe();
    }

    private void createAdapter() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
        adapter = new PostByCategoryAdapter(prefUtils, gridLayoutManager, postsList, newPostsItemListener);
        recycler.setLayoutManager(gridLayoutManager);
        recycler.setAdapter(adapter);
        recycler.setHasFixedSize(true);

        if(postsList.size() == 0) {
            shimmer.setVisibility(View.VISIBLE);
            updateDataFromApi(shimmer, null, postsList.size() == 0);
        } else {
            int savedPostPosition = CategoryStoreProvider.getInstance(context).getCategoryById(categoryId).getPostInCategory();
            Objects.requireNonNull(recycler.getLayoutManager()).scrollToPosition(savedPostPosition);
        }


        // Загрузить/обновить данные с API при скролах ресайклера вниз, если достигнут конец списка
        recycler.addOnScrollListener(new RecyclerScrollListener() {
            @Override
            public void onScrolledToEnd() {
                progressBar.setVisibility(View.VISIBLE);
                Objects.requireNonNull(recycler.getLayoutManager()).scrollToPosition(postsList.size() - 1);
                updateDataFromApi(null, null, false);
            }
        });
    }

    // Обработка нажатия на элемент списка
    private final PostByCategoryAdapter.OnItemClick<PostsModel.PostDetails> newPostsItemListener =
            (item) -> openPostActivity(getActivity(), item, prefUtils, true);

    private void updateDataFromApi(ShimmerFrameLayout shimmerToApi, SwipyRefreshLayout refreshLayoutToApi, boolean isGetNewList) {
        if (isGetNewList) CategoryStoreProvider.getInstance(context).updatePositionInCategory(categoryId, 0);
        new Handler().postDelayed(() ->
                        new Thread(() -> getPostsByCategory(context, prefUtils, adapter, recycler,
                                shimmerToApi, refreshLayoutToApi, isGetNewList, progressBar, categoryId)).start()
                , 1000);
    }

    private void updateDataBySwipe() {
        refreshLayout.setColorSchemeColors(ContextCompat.getColor(context, R.color.colorPrimary));
        refreshLayout.setOnRefreshListener(direction -> {
            updateDataFromApi(null, refreshLayout, true);
            recycler.suppressLayout(true);
        });
    }
}
