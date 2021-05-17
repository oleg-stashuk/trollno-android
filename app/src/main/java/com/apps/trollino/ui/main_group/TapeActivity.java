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
import com.apps.trollino.db_room.category.CategoryStoreProvider;
import com.apps.trollino.db_room.posts.PostStoreProvider;
import com.apps.trollino.ui.base.BaseActivity;
import com.apps.trollino.utils.data.Const;
import com.apps.trollino.utils.data.DataListFromApi;
import com.apps.trollino.utils.data.PostListByCategoryFromApi;
import com.apps.trollino.utils.networking.user_action.GetNewAnswersCount;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.tabs.TabLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;

import java.util.List;

import static com.apps.trollino.utils.SnackBarMessageCustom.showSnackBarOnTheTopByBottomNavigation;
import static com.apps.trollino.utils.recycler.MakeFreshPostForTapeActivity.makeNewPostsRecyclerView;
import static com.apps.trollino.utils.recycler.MakeLinerRecyclerViewForTapeActivity.makeLinerRecyclerViewForTapeActivity;
import static com.apps.trollino.utils.recycler.MakePostsByCategoryGridRecyclerViewForTapeActivity.makePostsByCategoryGridRecyclerViewForTapeActivity;

public class TapeActivity extends BaseActivity implements View.OnClickListener{
    private RecyclerView newsRecyclerView;
    private SwipyRefreshLayout newRefreshLayout;
    private RecyclerView discussRecyclerView;
    private SwipyRefreshLayout discussRefreshLayout;
    private TabLayout tabs;
    private ImageView indicatorImageView;
    private LinearLayout bottomNavigation;
    private ShimmerFrameLayout twoColumnShimmer;
    private ShimmerFrameLayout oneColumnShimmer;
    private ProgressBar progressBar;

    private boolean doubleBackToExitPressedOnce = false;  // для обработки нажатия onBackPressed
    private int selectedTab = 0;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_tape;
    }

    @Override
    protected void initView() {
        findView();
        createTabLayout();
        makeTabSelectedListener();
        updateDataBySwipe();

        prefUtils.saveCurrentActivity("");
        prefUtils.saveValuePostFromCategoryList(false);

        twoColumnShimmer.setVisibility(View.GONE);
        oneColumnShimmer.setVisibility(View.GONE);

        // Если список постов из категории "Свежее" пуст, то показать Shimmer
        int freshPostsSize = PostStoreProvider.getInstance(this).getPostByCategoryName(Const.CATEGORY_FRESH).size();
        updateDataFromApiFresh(freshPostsSize > 0 ? null : twoColumnShimmer, null);
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

    private void findView() {
        bottomNavigation = findViewById(R.id.bottom_navigation_tape);
        twoColumnShimmer = findViewById(R.id.include_shimmer_post_two_column);
        oneColumnShimmer = findViewById(R.id.include_shimmer_post_one_column);
        progressBar = findViewById(R.id.progress_bar_tape);

        newsRecyclerView = findViewById(R.id.recycler_tape_new);
        newRefreshLayout = findViewById(R.id.refresh_layout_tape_new);
        discussRecyclerView = findViewById(R.id.recycler_tape_discuss);
        discussRefreshLayout = findViewById(R.id.refresh_layout_tape_discuss);

        tabs = findViewById(R.id.tab_layout_tape);
        indicatorImageView = findViewById(R.id.indicator_image);
        ImageButton searchImageButton = findViewById(R.id.search_button_tape);
        searchImageButton.setOnClickListener(this);
        findViewById(R.id.activity_button).setOnClickListener(this);
        findViewById(R.id.favorites_button).setOnClickListener(this);
        findViewById(R.id.profile_button).setOnClickListener(this);

        TextView tapeBottomNavigationTextView = findViewById(R.id.tape_button);
        tapeBottomNavigationTextView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_tape_green, 0, 0);
        tapeBottomNavigationTextView.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
    }

    private void getAnswersCount() {
        new Thread(() -> GetNewAnswersCount.getNewAnswersCount(this, prefUtils, indicatorImageView)).start();
    }

    // Добавить категории в TabLayout с БД
    private void createTabLayout() {
        List<CategoryModel> categoryListFromDB = CategoryStoreProvider.getInstance(this).getCategoryList();
        for (CategoryModel category : categoryListFromDB) {
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
                    showCorrectRecycler(false);
                    prefUtils.saveValuePostFromCategoryList(false);
                    updateDataFromApiFresh(null, null);
                } else if(tabs.getSelectedTabPosition() == 1) {
                    showCorrectRecycler(true);
                    prefUtils.saveValuePostFromCategoryList(false);
                    int discussedListSize = PostStoreProvider.getInstance(TapeActivity.this).getPostByCategoryName(Const.CATEGORY_DISCUSSED).size();
                    updateDataFromApiDiscuss(discussedListSize > 0 ? null : oneColumnShimmer, null);
                } else {
                    showCorrectRecycler(false);
                    prefUtils.saveValuePostFromCategoryList(true);
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

    private void showCorrectRecycler(boolean isDiscuss) {
        discussRecyclerView.setVisibility(isDiscuss ? View.VISIBLE : View.GONE);
        discussRefreshLayout.setVisibility(isDiscuss ? View.VISIBLE : View.GONE);
        newsRecyclerView.setVisibility(isDiscuss ? View.GONE : View.VISIBLE);
        newRefreshLayout.setVisibility(isDiscuss ? View.GONE : View.VISIBLE);
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

    private void updateDataFromApiFresh(ShimmerFrameLayout shimmerToApi, SwipyRefreshLayout newRefreshLayout) {
        makeNewPostsRecyclerView(this, prefUtils, newsRecyclerView, shimmerToApi,
                newRefreshLayout, bottomNavigation, progressBar);
    }

    private void updateDataFromApiDiscuss(ShimmerFrameLayout shimmerToApi, SwipyRefreshLayout refreshLayoutToApi) {
        makeLinerRecyclerViewForTapeActivity(this, prefUtils, discussRecyclerView,
                shimmerToApi, refreshLayoutToApi, bottomNavigation);
    }

    private void updateDataFromApiOther(ShimmerFrameLayout shimmerToApi, SwipyRefreshLayout refreshLayoutToApi, boolean IsUpdateData) {
        showCorrectShimmer(false, IsUpdateData);
        makePostsByCategoryGridRecyclerViewForTapeActivity(this, prefUtils, newsRecyclerView,
                        shimmerToApi, refreshLayoutToApi, bottomNavigation, progressBar);
    }

    private void updateDataBySwipe() {
        newRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorPrimary));
        newRefreshLayout.setOnRefreshListener(direction -> {
            if(selectedTab == 0) {
                updateDataFromApiFresh(null, newRefreshLayout);
            } else {
                updateDataFromApiOther(null, newRefreshLayout, true);
            }
            newsRecyclerView.suppressLayout(true);
        });

        discussRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorPrimary));
        discussRefreshLayout.setOnRefreshListener(direction -> {
            if(selectedTab == 1) {
                updateDataFromApiDiscuss(null, discussRefreshLayout);
            }
            discussRecyclerView.suppressLayout(true);
        });
    }

    @Override
    public void onBackPressed() {
        removeAllDataFromPostList();
        if (doubleBackToExitPressedOnce) {
            removeAllDataFromDB();
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
