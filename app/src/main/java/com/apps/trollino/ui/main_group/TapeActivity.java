package com.apps.trollino.ui.main_group;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.trollino.R;
import com.apps.trollino.adapters.NewsVideoAdapter;
import com.apps.trollino.adapters.NewsVideoForTwoColumnsAdapter;
import com.apps.trollino.adapters.base.BaseRecyclerAdapter;
import com.apps.trollino.model.FavoriteModel;
import com.apps.trollino.ui.base.BaseActivity;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class TapeActivity extends BaseActivity implements View.OnClickListener{
    private RecyclerView newsRecyclerView;
    private List<FavoriteModel> newsVideoList = FavoriteModel.makeFavoriteVideoList();
    private TabLayout tabs;
    private int selectedTabs = 0;
    private boolean doubleBackToExitPressedOnce = false;  // для обработки нажатия onBackPressed

    @Override
    protected int getLayoutID() {
        return R.layout.activity_tape;
    }

    @Override
    protected void initView() {
        tabs = findViewById(R.id.tab_layout_tape);
        newsRecyclerView = findViewById(R.id.news_recycler_tape);
        findViewById(R.id.search_button_tape).setOnClickListener(this);
        findViewById(R.id.activity_button_tape).setOnClickListener(this);
        findViewById(R.id.favorites_button_tape).setOnClickListener(this);
        findViewById(R.id.profile_button_tape).setOnClickListener(this);

        makeNewsRecyclerView();
        makeTabSelectedListener();
    }

    // Обработка нажатия на элементы горизонтального ScrollBar
    private void makeTabSelectedListener() {
        Log.d("123456", tabs.getScrollBarSize() + " - " + tabs.getTabCount());
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d("123456", "" + tabs.getSelectedTabPosition());
                showToast("" + tabs.getSelectedTabPosition());
                selectedTabs = tabs.getSelectedTabPosition();
                makeNewsRecyclerView();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    private void makeNewsRecyclerView() {
        if(selectedTabs == 1) {
            newsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            newsRecyclerView.setAdapter(new NewsVideoAdapter(this, newsVideoList, newsVideoItemListener));
            newsRecyclerView.setHasFixedSize(true);
        } else {
            newsRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            newsRecyclerView.setAdapter(new NewsVideoForTwoColumnsAdapter(this, newsVideoList, newsVideoGridItemListener));
            newsRecyclerView.setHasFixedSize(true);
        }
    }

    // Обработка нажатия на элемент списка
    private final NewsVideoAdapter.OnItemClick<FavoriteModel> newsVideoItemListener =
            new BaseRecyclerAdapter.OnItemClick<FavoriteModel>() {
                @Override
                public void onItemClick(FavoriteModel item, int position) {
                    showToast("Press " + item.getVideoId());
                    startActivity(new Intent(TapeActivity.this, PostActivity.class));
                    finish();
                }
            };

    // Обработка нажатия на элемент списка с испотльзованием Grid
    private final NewsVideoForTwoColumnsAdapter.OnItemClick<FavoriteModel> newsVideoGridItemListener = new BaseRecyclerAdapter.OnItemClick<FavoriteModel>() {
        @Override
        public void onItemClick(FavoriteModel item, int position) {
            showToast("Press " + item.getVideoId());
            startActivity(new Intent(TapeActivity.this, PostActivity.class));
            finish();
        }
    };

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        showToast(getString(R.string.press_twice_to_exit));

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.search_button_tape: // "Перейти на экран Поиска"
                startActivity(new Intent(this, SearchActivity.class));
                finish();
                break;
            case R.id.activity_button_tape: // "Перейти на экран Активность"
                startActivity(new Intent(this, ActivityInPostActivity.class));
                finish();
                break;
            case R.id.favorites_button_tape: // "Перейти на экран Избранное"
                startActivity(new Intent(this, FavoriteActivity.class));
                finish();
                break;
            case R.id.profile_button_tape: // "Перейти на экран Профиль"
                startActivity(new Intent(this, ProfileActivity.class));
                finish();
                break;
        }
    }
}