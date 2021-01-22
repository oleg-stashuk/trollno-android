package com.apps.trollino.ui.main_group;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.trollino.R;
import com.apps.trollino.adapters.PostListAdapter;
import com.apps.trollino.data.model.PostsModel;
import com.apps.trollino.ui.base.BaseActivity;
import com.apps.trollino.utils.RecyclerScrollListener;
import com.apps.trollino.utils.data.DataListFromApi;
import com.apps.trollino.utils.data.PostListBySearchFromApi;
import com.apps.trollino.utils.networking.main_group.GetPostBySearch;
import com.facebook.shimmer.ShimmerFrameLayout;

import static com.apps.trollino.utils.OpenPostActivityHelper.openPostActivity;

public class SearchActivity extends BaseActivity implements View.OnClickListener {
    private ShimmerFrameLayout shimmer;
    private View nothingSearch;
    private RecyclerView recyclerView;
    private EditText searchEditText;
    private ProgressBar progressBar;
    private PostListAdapter adapter;
    private String searchString = "";

    @Override
    protected int getLayoutID() {
        return R.layout.activity_search;
    }

    @Override
    protected void initView() {
        shimmer = findViewById(R.id.search_shimmer);
        recyclerView = findViewById(R.id.recycler_search);
        nothingSearch = findViewById(R.id.include_nothing_search);
        searchEditText = findViewById(R.id.search_search);
        progressBar = findViewById(R.id.progress_bar_search);
        searchEditText.setOnEditorActionListener(editorActionListener);
        findViewById(R.id.back_button_search).setOnClickListener(this);
    }


    private TextView.OnEditorActionListener editorActionListener = (v, actionId, event) -> {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            searchString = searchEditText.getText().toString();
            if (searchString.isEmpty() || searchString.length() == 0) {
                showSnackBarMessage(findViewById(R.id.activity_search), getString(R.string.enter_data_to_search));
            } else {
                PostListBySearchFromApi.getInstance().removeAllDataFromList(prefUtils);
                progressBar.setVisibility(View.VISIBLE);
                nothingSearch.setVisibility(View.GONE);
                makeSearchPostsRecyclerView();
                hideKeyBoard(); // Hide keyBoard if was press button "Search"
            }
            return true;
        }
        return false;
    };

    // Hide keyBoard if was press button "Search"
    private void hideKeyBoard() {
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void makeSearchPostsRecyclerView() {
        DataListFromApi.getInstance().removeAllDataFromList(prefUtils);

        adapter = new PostListAdapter(SearchActivity.this, prefUtils, PostListBySearchFromApi.getInstance().getPostListBySearch(), searchPostsItemListener);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        if(DataListFromApi.getInstance().getNewPostsList().isEmpty()) {
            infiniteScroll(true);
        }

        recyclerView.addOnScrollListener(new RecyclerScrollListener() {
            @Override
            public void onScrolledToEnd() {
                infiniteScroll(false);
            }

            @Override
            public void onScrolledToTop() {
                infiniteScroll(true);
            }
        });
    }

    // Обработка нажатия на элемент списка
    private final PostListAdapter.OnItemClick<PostsModel.PostDetails> searchPostsItemListener = (item, position) -> {
        openPostActivity(this, item, prefUtils, false);
    };

    // Загрузить/обновить данные с API
    private void updateDataFromApi(boolean isGetNewList) {
        new Thread(() -> {
            GetPostBySearch.getPostBySearch(SearchActivity.this, prefUtils, recyclerView, shimmer,
                    searchString, nothingSearch, progressBar, adapter, isGetNewList);
        }).start();
    }

    // Загрузить/обновить данные с API при скролах ресайклера вверх или вниз, если достигнут конец списка
    private void infiniteScroll(boolean isGetNewList) {
        progressBar.setVisibility(isGetNewList ? View.GONE : View.VISIBLE);
        shimmer.setVisibility(isGetNewList ? View.VISIBLE : View.GONE);
        Handler handler = new Handler();
        handler.postDelayed(() -> updateDataFromApi(isGetNewList), 1000);
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