package com.app.trollno.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.trollno.R;
import com.app.trollno.adapters.DiscussPostAdapter;
import com.app.trollno.data.model.PostsModel;
import com.app.trollno.db_room.category.CategoryStoreProvider;
import com.app.trollno.db_room.posts.PostStoreProvider;
import com.app.trollno.ui.fragment.base.BaseFragment;
import com.app.trollno.utils.RecyclerScrollListener;
import com.app.trollno.utils.networking.main_group.GetMostDiscusPosts;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;

import java.util.List;
import java.util.Objects;

import static com.app.trollno.utils.OpenPostActivityHelper.openPostActivity;
import static com.app.trollno.utils.data.Const.CATEGORY_DISCUSSED;

public class DiscussTabFragment extends BaseFragment {
    private ShimmerFrameLayout shimmer;
    private SwipyRefreshLayout swipyRefresh;
    private RecyclerView recycler;
    private View bottomLine;

    private DiscussPostAdapter adapter;
    private List<PostsModel.PostDetails> postsList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_discuss_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        shimmer = view.findViewById(R.id.include_shimmer_post_one_column);
        swipyRefresh = view.findViewById(R.id.refresh_discuss_tab);
        recycler = view.findViewById(R.id.recycler_discuss_tab);
        bottomLine = view.findViewById(R.id.line_discuss_tab);
    }

    @Override
    protected void initView() {
        postsList = PostStoreProvider.getInstance(context).getPostByCategoryName(CATEGORY_DISCUSSED);

        createAdapter();
        updateDataBySwipe();
    }

    private void createAdapter() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        adapter = new DiscussPostAdapter(prefUtils, linearLayoutManager, postsList, discussedItemListener);
        recycler.setLayoutManager(linearLayoutManager);
        recycler.setAdapter(adapter);
        recycler.setHasFixedSize(true);

        if(postsList.size() == 0) {
            shimmer.setVisibility(View.VISIBLE);
            updateDataFromApi(shimmer, null);
        } else {
            int savedPostPosition = CategoryStoreProvider.getInstance(context)
                    .getCategoryById(CATEGORY_DISCUSSED).getPostInCategory();
            Objects.requireNonNull(recycler.getLayoutManager()).scrollToPosition(savedPostPosition);
        }

        // Загрузить/обновить данные с API при скролах ресайклера вниз, если достигнут конец списка
        recycler.addOnScrollListener(new RecyclerScrollListener() {
            @Override
            public void onScrolledToEnd() {
                CategoryStoreProvider.getInstance(context)
                        .updatePositionInCategory(CATEGORY_DISCUSSED, postsList.size() - 1);
                Objects.requireNonNull(recycler.getLayoutManager()).scrollToPosition(postsList.size() - 1);
            }
        });
    }

    // Обработка нажатия на элемент списка
    private final DiscussPostAdapter.OnItemClick<PostsModel.PostDetails> discussedItemListener = (item)
            -> openPostActivity(getActivity(), item, prefUtils, false);

    // Загрузить/обновить данные с API
    private void updateDataFromApi(ShimmerFrameLayout shimmerToApi, SwipyRefreshLayout refreshLayoutToApi) {
        try {
            new Handler().postDelayed(() ->
                            new Thread(() -> GetMostDiscusPosts.makeGetNewPosts(context, prefUtils, adapter,
                                    recycler, shimmerToApi, refreshLayoutToApi, bottomLine)).start()
                    , 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateDataBySwipe() {
        swipyRefresh.setColorSchemeColors(ContextCompat.getColor(context, R.color.colorPrimary));
        swipyRefresh.setOnRefreshListener(direction -> {
            updateDataFromApi(null, swipyRefresh);
            recycler.suppressLayout(true);
        });
    }
}
