package com.apps.trollino.ui.main_group;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.trollino.R;
import com.apps.trollino.adapters.NewsVideoForTwoColumnsAdapter;
import com.apps.trollino.data.model.CategoryModel;
import com.apps.trollino.data.model.FavoriteModel;
import com.apps.trollino.ui.base.BaseActivity;
import com.apps.trollino.utils.data.CategoryListFromApi;
import com.apps.trollino.utils.data.DataListFromApi;
import com.apps.trollino.utils.MakeLinerRecyclerViewForTapeActivity;
import com.apps.trollino.utils.MakeRecyclerViewForTapeActivity;
import com.apps.trollino.utils.networking.GetCategoryList;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class TapeActivity extends BaseActivity implements View.OnClickListener{
    private RecyclerView newsRecyclerView;
    private List<FavoriteModel> newsVideoList = FavoriteModel.makeFavoriteVideoList();
    private TabLayout tabs;
    private ProgressBar progressBar;

    private int selectedTabs;
    private boolean doubleBackToExitPressedOnce = false;  // для обработки нажатия onBackPressed

    @Override
    protected int getLayoutID() {
        return R.layout.activity_tape;
    }

    @Override
    protected void initView() {
        tabs = findViewById(R.id.tab_layout_tape);
        newsRecyclerView = findViewById(R.id.news_recycler_tape);
        progressBar = findViewById(R.id.progress_bar_tape);
        findViewById(R.id.search_button_tape).setOnClickListener(this);
        findViewById(R.id.activity_button_tape).setOnClickListener(this);
        findViewById(R.id.favorites_button_tape).setOnClickListener(this);
        findViewById(R.id.profile_button_tape).setOnClickListener(this);

        createTabLayout();

        makeTabSelectedListener();
        MakeRecyclerViewForTapeActivity.makeNewPostsRecyclerView(this, newsRecyclerView, progressBar, prefUtils);
    }

    // Add category list from Api to TabLayout
    private void createTabLayout() {
        List<CategoryModel> categoryList = CategoryListFromApi.getInstance().getCategoryList();
        for (CategoryModel category : categoryList) {
            tabs.addTab(tabs.newTab().setText(category.getNameCategory()).setContentDescription(category.getIdCategory()));
        }
    }

    // Обработка нажатия на элементы горизонтального ScrollBar
    private void makeTabSelectedListener() {
        Log.d("OkHttp", tabs.getScrollBarSize() + " - " + tabs.getTabCount());
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d("OkHttp", "" + tabs.getSelectedTabPosition());
                selectedTabs = tabs.getSelectedTabPosition();
                progressBar.setVisibility(View.GONE);
                if(tabs.getSelectedTabPosition() == 0) {
                    MakeRecyclerViewForTapeActivity.makeNewPostsRecyclerView(TapeActivity.this, newsRecyclerView, progressBar, prefUtils);
                } else if(tabs.getSelectedTabPosition() == 1) {
                    MakeLinerRecyclerViewForTapeActivity.makeLinerRecyclerViewForTapeActivity(TapeActivity.this, newsRecyclerView, progressBar, prefUtils);
                } else {
                    DataListFromApi.getInstance().removeAllDataFromList(prefUtils);
                    makeNewsRecyclerView();
                    Log.d("OkHttp_1", "selected tabs " + tab.getContentDescription());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void makeNewsRecyclerView() {
        newsRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        newsRecyclerView.setAdapter(new NewsVideoForTwoColumnsAdapter(this, newsVideoList, newsVideoGridItemListener));
        newsRecyclerView.setHasFixedSize(true);
    }

    // Обработка нажатия на элемент списка с испотльзованием Grid
    private final NewsVideoForTwoColumnsAdapter.OnItemClick<FavoriteModel> newsVideoGridItemListener = (item, position) -> {
        showToast("Press " + item.getVideoId());
        startActivity(new Intent(TapeActivity.this, PostActivity.class));
        finish();
    };

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        showToast(getString(R.string.press_twice_to_exit));
        new Handler().postDelayed(() -> doubleBackToExitPressedOnce=false, 2000);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.search_button_tape: // "Перейти на экран Поиска"
                DataListFromApi.getInstance().removeAllDataFromList(prefUtils);
                startActivity(new Intent(this, SearchActivity.class));
                finish();
                break;
            case R.id.activity_button_tape: // "Перейти на экран Активность"
                DataListFromApi.getInstance().removeAllDataFromList(prefUtils);
                startActivity(new Intent(this, ActivityInPostActivity.class));
                finish();
                break;
            case R.id.favorites_button_tape: // "Перейти на экран Избранное"
                DataListFromApi.getInstance().removeAllDataFromList(prefUtils);
                startActivity(new Intent(this, FavoriteActivity.class));
                finish();
                break;
            case R.id.profile_button_tape: // "Перейти на экран Профиль"
                DataListFromApi.getInstance().removeAllDataFromList(prefUtils);
                startActivity(new Intent(this, ProfileActivity.class));
                finish();
                break;
        }
    }
}