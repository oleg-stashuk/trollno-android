package com.apps.trollino.ui.main_group;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.trollino.R;
import com.apps.trollino.data.model.CategoryModel;
import com.apps.trollino.ui.base.BaseActivity;
import com.apps.trollino.utils.data.DataListFromApi;
import com.apps.trollino.utils.data.PostListByCategoryFromApi;
import com.apps.trollino.utils.networking.user_action.GetNewAnswersCount;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.tabs.TabLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;

import java.util.List;

import static com.apps.trollino.utils.SnackBarMessageCustom.showSnackBarOnTheTopByBottomNavigation;
import static com.apps.trollino.utils.recycler.MakeGridRecyclerViewForTapeActivity.makeNewPostsRecyclerView;
import static com.apps.trollino.utils.recycler.MakeLinerRecyclerViewForTapeActivity.makeLinerRecyclerViewForTapeActivity;
import static com.apps.trollino.utils.recycler.MakePostsByCategoryGridRecyclerViewForTapeActivity.makePostsByCategoryGridRecyclerViewForTapeActivity;

public class TapeActivity extends BaseActivity implements View.OnClickListener{
    private RecyclerView newsRecyclerView;
    private TabLayout tabs;
    private ImageView indicatorImageView;

    private LinearLayout bottomNavigation;
    private ShimmerFrameLayout twoColumnShimmer;
    private ShimmerFrameLayout oneColumnShimmer;
    private SwipyRefreshLayout refreshLayout;
    private ProgressBar progressBar;
    private boolean doubleBackToExitPressedOnce = false;  // для обработки нажатия onBackPressed
    private int selectedTab = 0;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_tape;
    }

    @Override
    protected void initView() {
        bottomNavigation = findViewById(R.id.bottom_navigation_tape);
        twoColumnShimmer = findViewById(R.id.include_shimmer_post_two_column);
        oneColumnShimmer = findViewById(R.id.include_shimmer_post_one_column);
        refreshLayout = findViewById(R.id.refresh_layout_tape);
        progressBar = findViewById(R.id.progress_bar_tape);

        tabs = findViewById(R.id.tab_layout_tape);
        newsRecyclerView = findViewById(R.id.news_recycler_tape);
        TextView tapeBottomNavigationTextView = findViewById(R.id.tape_button);
        indicatorImageView = findViewById(R.id.indicator_image);
        ImageButton searchImageButton = findViewById(R.id.search_button_tape);
        searchImageButton.setOnClickListener(this);
        findViewById(R.id.activity_button).setOnClickListener(this);
        findViewById(R.id.favorites_button).setOnClickListener(this);
        findViewById(R.id.profile_button).setOnClickListener(this);

        createTabLayout();
        tapeBottomNavigationTextView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_tape_green, 0, 0);
        tapeBottomNavigationTextView.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));

        prefUtils.saveCurrentActivity("");
        makeTabSelectedListener();
        updateDataFromApiFresh(twoColumnShimmer, null, true);
        updateDataBySwipe();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (prefUtils.getIsUserAuthorization()) {
            getAnswersCount();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getAnswersCount();
                    handler.postDelayed(this, TIME_TO_UPDATE_DATA);
                }
            }, TIME_TO_UPDATE_DATA);
        }
    }

    private void getAnswersCount() {
        new Thread(() -> GetNewAnswersCount.getNewAnswersCount(this, prefUtils, indicatorImageView)).start();
    }

    // Add category list from Api to TabLayout
    private void createTabLayout() {
        List<CategoryModel> categoryList = prefUtils.getCategoryList();
        tabs.addTab(tabs.newTab().setText(getResources().getString(R.string.fresh_txt)));
        tabs.addTab(tabs.newTab().setText(getResources().getString(R.string.discuss_post)));
        for (CategoryModel category : categoryList) {
            tabs.addTab(tabs.newTab().setText(category.getNameCategory()).setTag(category.getIdCategory()));
        }
    }

    // Обработка нажатия на элементы горизонтального ScrollBar
    private void makeTabSelectedListener() {
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectedTab = tabs.getSelectedTabPosition();
                if(tabs.getSelectedTabPosition() == 0) {
                    prefUtils.saveCurrentAdapterPositionPosts(0);
                    updateDataFromApiFresh(twoColumnShimmer, null, true);
                } else if(tabs.getSelectedTabPosition() == 1) {
                    updateDataFromApiDiscuss(oneColumnShimmer, null, true);
                } else {
                    prefUtils.saveCurrentAdapterPositionPosts(0);
                    prefUtils.saveSelectedCategoryId(tab.getTag().toString());
                    updateDataFromApiOther(twoColumnShimmer, null, true);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void showCorrectShimmer(Boolean isOneColumn, boolean IsUpdateData) {
        if (IsUpdateData) {
            twoColumnShimmer.setVisibility(View.GONE);
            oneColumnShimmer.setVisibility(View.GONE);
        } else {
            twoColumnShimmer.setVisibility(isOneColumn ? View.GONE : View.VISIBLE);
            oneColumnShimmer.setVisibility(isOneColumn ? View.VISIBLE : View.GONE);
        }
    }


    private void updateDataFromApiFresh(ShimmerFrameLayout shimmerToApi, SwipyRefreshLayout refreshLayoutToApi, boolean IsUpdateData) {
        showCorrectShimmer(false, IsUpdateData);
        makeNewPostsRecyclerView(this, prefUtils, newsRecyclerView, shimmerToApi,
                refreshLayoutToApi, bottomNavigation, progressBar);
    }

    private void updateDataFromApiDiscuss(ShimmerFrameLayout shimmerToApi, SwipyRefreshLayout refreshLayoutToApi, boolean IsUpdateData) {
        showCorrectShimmer(true, IsUpdateData);
        makeLinerRecyclerViewForTapeActivity(this, prefUtils, newsRecyclerView,
                shimmerToApi, refreshLayoutToApi, bottomNavigation, progressBar);
    }

    private void updateDataFromApiOther(ShimmerFrameLayout shimmerToApi, SwipyRefreshLayout refreshLayoutToApi, boolean IsUpdateData) {
        showCorrectShimmer(false, IsUpdateData);
        makePostsByCategoryGridRecyclerViewForTapeActivity(this, prefUtils, newsRecyclerView,
                        shimmerToApi, refreshLayoutToApi, bottomNavigation, progressBar);
    }

    private void updateDataBySwipe() {
        refreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorPrimary));
        refreshLayout.setOnRefreshListener(direction -> {
            if(selectedTab == 0) {
                updateDataFromApiFresh(null, refreshLayout, true);
            } else if(selectedTab == 1) {
                updateDataFromApiDiscuss(null, refreshLayout, true);
            } else {
                updateDataFromApiOther(null, refreshLayout, true);
            }
        });
    }

    @Override
    public void onBackPressed() {
        removeAllDataFromPostList();
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        showSnackBarOnTheTopByBottomNavigation(bottomNavigation, getString(R.string.press_twice_to_exit));
        new Handler().postDelayed(() -> doubleBackToExitPressedOnce=false, 2000);
    }

    private void removeAllDataFromPostList() {
        DataListFromApi.getInstance().removeAllDataFromList(prefUtils);
        PostListByCategoryFromApi.getInstance().removeAllDataFromList(prefUtils);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.search_button_tape: // "Перейти на экран Поиска"
                removeAllDataFromPostList();
                startActivity(new Intent(this, SearchActivity.class));
                finish();
                break;
            case R.id.activity_button: // "Перейти на экран Активность"
                removeAllDataFromPostList();
                startActivity(new Intent(this, AnswersActivity.class));
                finish();
                break;
            case R.id.favorites_button: // "Перейти на экран Избранное"
                removeAllDataFromPostList();
                startActivity(new Intent(this, FavoriteActivity.class));
                finish();
                break;
            case R.id.profile_button: // "Перейти на экран Профиль"
                removeAllDataFromPostList();
                startActivity(new Intent(this, ProfileActivity.class));
                finish();
                break;
        }
    }
}
