package com.apps.trollino.ui.main_group;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
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
import com.apps.trollino.utils.recycler.MakeGridRecyclerViewForTapeActivity;
import com.apps.trollino.utils.recycler.MakeLinerRecyclerViewForTapeActivity;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

import static com.apps.trollino.utils.recycler.MakePostsByCategoryGridRecyclerViewForTapeActivity.makePostsByCategoryGridRecyclerViewForTapeActivity;

public class TapeActivity extends BaseActivity implements View.OnClickListener{
    private RecyclerView newsRecyclerView;
    private TabLayout tabs;
    private ProgressBar progressBarBottom;
    private ProgressBar progressBarTop;

    private ShimmerFrameLayout twoColumnShimmer;
    private boolean doubleBackToExitPressedOnce = false;  // для обработки нажатия onBackPressed

    @Override
    protected int getLayoutID() {
        return R.layout.activity_tape;
    }

    @Override
    protected void initView() {
        twoColumnShimmer = findViewById(R.id.include_tape_two_column_shimmer);

        tabs = findViewById(R.id.tab_layout_tape);
        newsRecyclerView = findViewById(R.id.news_recycler_tape);
        progressBarBottom = findViewById(R.id.progress_bar_bottom_tape);
        progressBarTop = findViewById(R.id.progress_bar_top_tape);
        TextView tapeBottomNavigationTextView = findViewById(R.id.tape_button);
        ImageView indicatorImageView = findViewById(R.id.indicator_image);
        findViewById(R.id.search_button_tape).setOnClickListener(this);
        findViewById(R.id.activity_button).setOnClickListener(this);
        findViewById(R.id.favorites_button).setOnClickListener(this);
        findViewById(R.id.profile_button).setOnClickListener(this);

        createTabLayout();
        tapeBottomNavigationTextView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_tape_green, 0, 0);
        tapeBottomNavigationTextView.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        if(prefUtils.getIsUserAuthorization()) {
            new Thread(() -> GetNewAnswersCount.getNewAnswersCount(this, prefUtils, indicatorImageView)).start();
        }

        prefUtils.saveCurrentActivity("");
        makeTabSelectedListener();
        MakeGridRecyclerViewForTapeActivity.makeNewPostsRecyclerView(this, prefUtils, newsRecyclerView, progressBarBottom, twoColumnShimmer);
    }

    // Add category list from Api to TabLayout
    private void createTabLayout() {
        List<CategoryModel> categoryList = prefUtils.getCategoryList();
        tabs.addTab(tabs.newTab().setText("Свежее"));
        tabs.addTab(tabs.newTab().setText("Обсуждаемое"));
        for (CategoryModel category : categoryList) {
            tabs.addTab(tabs.newTab().setText(category.getNameCategory()).setTag(category.getIdCategory()));
        }
    }

    // Обработка нажатия на элементы горизонтального ScrollBar
    private void makeTabSelectedListener() {
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                progressBarTop.setVisibility(View.GONE);
                progressBarBottom.setVisibility(View.GONE);
                if(tabs.getSelectedTabPosition() == 0) {
                    MakeGridRecyclerViewForTapeActivity.makeNewPostsRecyclerView(TapeActivity.this, prefUtils, newsRecyclerView, progressBarBottom, twoColumnShimmer);
                } else if(tabs.getSelectedTabPosition() == 1) {
                    twoColumnShimmer.setVisibility(View.GONE);
                    MakeLinerRecyclerViewForTapeActivity.makeLinerRecyclerViewForTapeActivity(TapeActivity.this, newsRecyclerView, progressBarBottom, progressBarTop, prefUtils);
                } else {
                    twoColumnShimmer.setVisibility(View.GONE);
                    prefUtils.saveSelectedCategoryId(tab.getTag().toString());
                    PostListByCategoryFromApi.getInstance().removeAllDataFromList(prefUtils);
                    makePostsByCategoryGridRecyclerViewForTapeActivity(TapeActivity.this, newsRecyclerView, progressBarBottom, progressBarTop, prefUtils);
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
        removeAllDataFromPostList();
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        showToast(getString(R.string.press_twice_to_exit));
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
                startActivity(new Intent(this, ActivityInPostActivity.class));
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
