package com.apps.trollino.ui.main_group;

import android.content.Intent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.trollino.R;
import com.apps.trollino.adapters.PostListAdapter;
import com.apps.trollino.data.model.PostsModel;
import com.apps.trollino.ui.base.BaseActivity;
import com.apps.trollino.utils.data.DataListFromApi;
import com.apps.trollino.utils.data.PostListBySearchFromApi;
import com.apps.trollino.utils.networking.main_group.GetPostBySearch;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import static com.apps.trollino.utils.OpenPostActivityHelper.openPostActivity;

public class SearchActivity extends BaseActivity implements View.OnClickListener {
    private SwipyRefreshLayout refreshLayout;
    private View nothingSearch;
    private RecyclerView recyclerView;
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
                makeSearchPostsRecyclerView(true);
                hideKeyBoard(); // Hide keyBoard if was press button "Search"
            }
            return true;
        }
        return false;
    };

    private void showMessageToPutDataSearch(){
        showSnackBarMessage(findViewById(R.id.activity_search), getString(R.string.enter_data_to_search));
    }

    public void makeSearchPostsRecyclerView( boolean isNewData) {
        DataListFromApi.getInstance().removeAllDataFromList(prefUtils);

        adapter = new PostListAdapter(SearchActivity.this, prefUtils, PostListBySearchFromApi.getInstance().getPostListBySearch(), searchPostsItemListener);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.getLayoutManager().scrollToPosition(prefUtils.getCurrentAdapterPositionPosts());
        getDataFromApi(isNewData);
    }

    // Обработка нажатия на элемент списка
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
                makeSearchPostsRecyclerView((direction == SwipyRefreshLayoutDirection.TOP));
            }
        });
    }

    // Загрузить/обновить данные с API
    private void getDataFromApi(boolean isNewData) {
        new Thread(() -> {
            GetPostBySearch.getPostBySearch(SearchActivity.this, prefUtils, recyclerView, refreshLayout,
                    searchString, nothingSearch, adapter, isNewData);
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
