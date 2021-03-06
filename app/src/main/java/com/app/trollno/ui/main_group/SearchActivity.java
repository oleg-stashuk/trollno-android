package com.app.trollno.ui.main_group;

import android.content.Intent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.trollno.R;
import com.app.trollno.adapters.PostListAdapter;
import com.app.trollno.data.model.PostsModel;
import com.app.trollno.ui.base.BaseActivity;
import com.app.trollno.utils.RecyclerScrollListener;
import com.app.trollno.utils.data.PostListBySearchFromApi;
import com.app.trollno.utils.networking.main_group.GetPostBySearch;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;

import static com.app.trollno.utils.OpenPostActivityHelper.openPostActivity;

public class SearchActivity extends BaseActivity implements View.OnClickListener {
    private SwipyRefreshLayout refreshLayout;
    private View nothingSearch;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private EditText searchEditText;
    private PostListAdapter adapter;
    private String searchString = "";

    @Override
    protected int getLayoutID() {
        return R.layout.activity_search;
    }

    @Override
    protected void initView() {
        refreshLayout = findViewById(R.id.refresh_layout_search);
        recyclerView = findViewById(R.id.recycler_search);
        progressBar = findViewById(R.id.progress_bar_search);
        nothingSearch = findViewById(R.id.include_nothing_search);
        searchEditText = findViewById(R.id.search_search);
        searchEditText.setOnEditorActionListener(editorActionListener);
        findViewById(R.id.back_button_search).setOnClickListener(this);

        updateDataBySwipe();
    }


    private TextView.OnEditorActionListener editorActionListener = (v, actionId, event) -> {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            searchString = searchEditText.getText().toString();
            if (searchString.isEmpty() || searchString.length() == 0) {
                showMessageToPutDataSearch();
            } else {
                PostListBySearchFromApi.getInstance().removeAllDataFromList(prefUtils);
                nothingSearch.setVisibility(View.GONE);
                makeSearchPostsRecyclerView();
                hideKeyBoard(); // Hide keyBoard if was press button "Search"
            }
            return true;
        }
        return false;
    };

    private void showMessageToPutDataSearch(){
        showSnackBarMessage(findViewById(R.id.activity_search), getString(R.string.enter_data_to_search));
    }

    public void makeSearchPostsRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        adapter = new PostListAdapter(SearchActivity.this, prefUtils, gridLayoutManager,
                PostListBySearchFromApi.getInstance().getPostListBySearch(), searchPostsItemListener);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        prefUtils.saveCurrentAdapterPositionPosts(0);
        recyclerView.getLayoutManager().scrollToPosition(0);
        getDataFromApi(true);

        recyclerView.addOnScrollListener(new RecyclerScrollListener() {
            @Override
            public void onScrolledToEnd() {
                recyclerView.getLayoutManager().scrollToPosition(prefUtils.getCurrentAdapterPositionPosts());
                progressBar.setVisibility(View.VISIBLE);
                getDataFromApi(false);
            }
        });
    }

    // ?????????????????? ?????????????? ???? ?????????????? ????????????
    private final PostListAdapter.OnItemClick<PostsModel.PostDetails> searchPostsItemListener = (item, position) -> {
        openPostActivity(this, item, prefUtils, false);
    };

    private void updateDataBySwipe() {
        refreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorPrimary));
        refreshLayout.setOnRefreshListener(direction -> {
            if (searchString.isEmpty() || searchString.length() == 0) {
                showMessageToPutDataSearch();
                refreshLayout.setRefreshing(false);
            } else {
                makeSearchPostsRecyclerView();
                recyclerView.suppressLayout(true);
            }
        });
    }

    // ??????????????????/???????????????? ???????????? ?? API
    private void getDataFromApi(boolean isNewData) {
        new Thread(() -> {
            GetPostBySearch.getPostBySearch(SearchActivity.this, prefUtils, recyclerView, refreshLayout,
                    searchString, nothingSearch, adapter, isNewData, progressBar);
        }).start();
    }

    @Override
    public void onBackPressed() {
        PostListBySearchFromApi.getInstance().removeAllDataFromList(prefUtils);
        startActivity(new Intent(this, TapeActivity.class));
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button_search:
                onBackPressed();
        }
    }
}
