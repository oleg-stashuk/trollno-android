package com.app.trollno.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.trollno.R;
import com.app.trollno.adapters.base.PostByCategoryAdapter;
import com.app.trollno.data.model.PostsModel;
import com.app.trollno.db_room.category.CategoryStoreProvider;
import com.app.trollno.db_room.posts.PostStoreProvider;
import com.app.trollno.ui.fragment.base.BaseFragment;
import com.app.trollno.utils.RecyclerScrollListener;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;

import java.util.List;
import java.util.Objects;

import static com.app.trollno.utils.OpenPostActivityHelper.openPostActivity;
import static com.app.trollno.utils.networking.main_group.GetPostsByCategory.getPostsByCategory;

public class NinthTabFragment extends BaseFragment {
    private ShimmerFrameLayout shimmer;
    private SwipyRefreshLayout refreshLayout;
    private RecyclerView recycler;
    private ProgressBar progressBar;
    private View bottomLine;

    private PostByCategoryAdapter adapter;
    private List<PostsModel.PostDetails> postsList;
    private String categoryId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ninth_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        shimmer = view.findViewById(R.id.include_shimmer_post_two_column);
        refreshLayout = view.findViewById(R.id.refresh_ninth_tab);
        recycler = view.findViewById(R.id.recycler_ninth_tab);
        progressBar = view.findViewById(R.id.progress_ninth_tab);
        bottomLine = view.findViewById(R.id.line_ninth_tab);
    }

    @Override
    protected void initView() {
        int tabPosition = 8;
        categoryId = CategoryStoreProvider.getInstance(context).getCategoryList().get(tabPosition).getIdCategory();
        String categoryName = CategoryStoreProvider.getInstance(context).getCategoryList().get(tabPosition).getNameCategory();
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

        // ??????????????????/???????????????? ???????????? ?? API ?????? ?????????????? ???????????????????? ????????, ???????? ?????????????????? ?????????? ????????????
        recycler.addOnScrollListener(new RecyclerScrollListener() {
            @Override
            public void onScrolledToEnd() {
                progressBar.setVisibility(View.VISIBLE);
                CategoryStoreProvider.getInstance(context)
                        .updatePositionInCategory(categoryId, postsList.size() - 1);
                Objects.requireNonNull(recycler.getLayoutManager()).scrollToPosition(postsList.size() - 1);
                updateDataFromApi(null, null, false);
            }
        });
    }

    // ?????????????????? ?????????????? ???? ?????????????? ????????????
    private final PostByCategoryAdapter.OnItemClick<PostsModel.PostDetails> newPostsItemListener =
            (item) -> openPostActivity(getActivity(), item, prefUtils, true);

    private void updateDataFromApi(ShimmerFrameLayout shimmerToApi, SwipyRefreshLayout refreshLayoutToApi, boolean isGetNewList) {
        if (isGetNewList) CategoryStoreProvider.getInstance(context).updatePositionInCategory(categoryId, 0);
        try {
            new Handler().postDelayed(() ->
                            new Thread(() -> getPostsByCategory(context, prefUtils, adapter, recycler, shimmerToApi,
                                    refreshLayoutToApi, isGetNewList, progressBar, bottomLine, categoryId)).start()
                    , 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateDataBySwipe() {
        refreshLayout.setColorSchemeColors(ContextCompat.getColor(context, R.color.colorPrimary));
        refreshLayout.setOnRefreshListener(direction -> {
            updateDataFromApi(null, refreshLayout, true);
            recycler.suppressLayout(true);
        });
    }

}