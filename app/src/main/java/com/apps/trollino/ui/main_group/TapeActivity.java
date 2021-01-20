package com.apps.trollino.ui.main_group;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.apps.trollino.R;
import com.apps.trollino.data.model.CategoryModel;
import com.apps.trollino.ui.base.BaseActivity;
import com.apps.trollino.ui.fragment.DiscussPostFragment;
import com.apps.trollino.ui.fragment.FreshPostFragment;
import com.apps.trollino.ui.fragment.OtherCategoryPostFragment;
import com.apps.trollino.utils.ViewPagerAdapter;
import com.apps.trollino.utils.data.DataListFromApi;
import com.apps.trollino.utils.data.PostListByCategoryFromApi;
import com.apps.trollino.utils.recycler.MakePostsByCategoryGridRecyclerViewForTapeActivity;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class TapeActivity extends BaseActivity implements View.OnClickListener{
    private TabLayout tabs;
    private ViewPager viewPager;

    private int pagerPosition;
    private List<CategoryModel> categoryList;
    private RecyclerView recyclerView;
    private ProgressBar progressBarBottom;
    private ProgressBar progressBarTop;
    private TextView tapeBottomNavigationTextView;
    private ImageView indicatorImageView;

    private boolean doubleBackToExitPressedOnce = false;  // для обработки нажатия onBackPressed

    @Override
    protected int getLayoutID() {
        return R.layout.activity_tape;
    }

    @Override
    protected void initView() {
        tabs = findViewById(R.id.tab_layout_tape);
        viewPager = findViewById(R.id.view_pager_tape);
        recyclerView = findViewById(R.id.news_recycler_tape);
        progressBarBottom = findViewById(R.id.progress_bar_bottom_tape);
        progressBarTop = findViewById(R.id.progress_bar_top_tape);
        tapeBottomNavigationTextView = findViewById(R.id.tape_button);
        indicatorImageView = findViewById(R.id.indicator_image);
        findViewById(R.id.search_button_tape).setOnClickListener(this);
        findViewById(R.id.activity_button).setOnClickListener(this);
        findViewById(R.id.favorites_button).setOnClickListener(this);
        findViewById(R.id.profile_button).setOnClickListener(this);

        tapeBottomNavigationTextView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_tape_green, 0, 0);
        tapeBottomNavigationTextView.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        indicatorImageView.setVisibility(View.VISIBLE);

        removeAllDataFromPostList();

        categoryList = prefUtils.getCategoryList();
        prefUtils.saveCurrentActivity("");
        try {
            pagerPosition = viewPager.getCurrentItem();
        } catch (Exception e) {
            e.printStackTrace();
            pagerPosition = 0;
        }
        getTabs();
    }

    private void getTabs() {
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
        new Handler().post(() -> {
            pagerAdapter.addFragment(FreshPostFragment.getInstance(), getResources().getString(R.string.fresh_post));
            pagerAdapter.addFragment(DiscussPostFragment.getInstance(), getResources().getString(R.string.discuss_post));

            for (CategoryModel category : categoryList) {
                pagerAdapter.addFragment(OtherCategoryPostFragment.getInstance(), category.getNameCategory());
            }

            viewPager.setAdapter(pagerAdapter);
            tabs.setupWithViewPager(viewPager);

            makePageChangeListener();
        });
    }

    private void makePageChangeListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(positionOffsetPixels == 0 && pagerPosition != position) {
                    pagerPosition = position;
                    if(pagerPosition > 1) {
                        PostListByCategoryFromApi.getInstance().removeAllDataFromList(prefUtils);
                        prefUtils.saveSelectedCategoryId(categoryList.get(pagerPosition - 2).getIdCategory());

                        showOrHideItem(View.VISIBLE);
                        MakePostsByCategoryGridRecyclerViewForTapeActivity
                                .makePostsByCategoryGridRecyclerViewForTapeActivity(TapeActivity.this, recyclerView, progressBarBottom, progressBarTop, prefUtils);
                    } else {
                        prefUtils.saveSelectedCategoryId("");
                        showOrHideItem(View.GONE);
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {}

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    private void showOrHideItem(int visibility) {
        recyclerView.setVisibility(visibility);
        progressBarBottom.setVisibility(visibility);
    }

    @Override
    public void onBackPressed() {
        removeAllDataFromPostList();
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        showSnackBarMessage(findViewById(R.id.activity_tape), getString(R.string.press_twice_to_exit));
        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
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
