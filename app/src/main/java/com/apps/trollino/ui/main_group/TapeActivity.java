package com.apps.trollino.ui.main_group;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.RecyclerView;

import com.apps.trollino.R;
import com.apps.trollino.data.model.CategoryModel;
import com.apps.trollino.data.model.FavoriteModel;
import com.apps.trollino.ui.base.BaseActivity;
import com.apps.trollino.utils.data.CategoryListFromApi;
import com.apps.trollino.utils.data.DataListFromApi;
import com.apps.trollino.utils.data.PostListByCategoryFromApi;
import com.apps.trollino.utils.recycler.MakeGridRecyclerViewForTapeActivity;
import com.apps.trollino.utils.recycler.MakeLinerRecyclerViewForTapeActivity;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

import static com.apps.trollino.utils.recycler.MakePostsByCategoryGridRecyclerViewForTapeActivity.makePostsByCategoryGridRecyclerViewForTapeActivity;

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
        MakeGridRecyclerViewForTapeActivity.makeNewPostsRecyclerView(this, newsRecyclerView, progressBar, prefUtils);
    }

    // Add category list from Api to TabLayout
    private void createTabLayout() {
        List<CategoryModel> categoryList = CategoryListFromApi.getInstance().getCategoryList();
        for (CategoryModel category : categoryList) {
            tabs.addTab(tabs.newTab().setText(category.getNameCategory()).setTag(category.getIdCategory()));
        }
    }

    // Обработка нажатия на элементы горизонтального ScrollBar
    private void makeTabSelectedListener() {
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectedTabs = tabs.getSelectedTabPosition();
                progressBar.setVisibility(View.GONE);
                if(tabs.getSelectedTabPosition() == 0) {
                    MakeGridRecyclerViewForTapeActivity.makeNewPostsRecyclerView(TapeActivity.this, newsRecyclerView, progressBar, prefUtils);
                } else if(tabs.getSelectedTabPosition() == 1) {
                    MakeLinerRecyclerViewForTapeActivity.makeLinerRecyclerViewForTapeActivity(TapeActivity.this, newsRecyclerView, progressBar, prefUtils);
                } else {
                    prefUtils.saveSelectedCategoryId(tab.getTag().toString());
                    PostListByCategoryFromApi.getInstance().removeAllDataFromList(prefUtils);
                    makePostsByCategoryGridRecyclerViewForTapeActivity(TapeActivity.this, newsRecyclerView, progressBar, prefUtils);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

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