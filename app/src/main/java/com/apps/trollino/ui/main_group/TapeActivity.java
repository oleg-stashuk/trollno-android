package com.apps.trollino.ui.main_group;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.apps.trollino.R;
import com.apps.trollino.adapters.PagerAdapter;
import com.apps.trollino.data.model.CategoryModel;
import com.apps.trollino.db_room.category.CategoryStoreProvider;
import com.apps.trollino.ui.base.BaseActivity;
import com.apps.trollino.utils.networking.user_action.GetNewAnswersCount;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

import static com.apps.trollino.utils.SnackBarMessageCustom.showSnackBarOnTheTopByBottomNavigation;

public class TapeActivity extends BaseActivity implements View.OnClickListener{
    private TabLayout tabs;
    private ImageView indicatorImageView;
    private LinearLayout bottomNavigation;
    private ViewPager viewPager;

    private boolean doubleBackToExitPressedOnce = false;  // для обработки нажатия onBackPressed
    private int selectedTab;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_tape;
    }

    @Override
    protected void initView() {
        findView();
        createTabLayout();
        makeTabSelectedListener();

        prefUtils.saveCurrentActivity("");
        selectedTab = prefUtils.getSelectedCategoryPosition();
        tabs.getTabAt(selectedTab).select();
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

        tabs = findViewById(R.id.tab_layout_tape);
        viewPager = findViewById(R.id.view_pager);
        indicatorImageView = findViewById(R.id.indicator_image);
        findViewById(R.id.search_button_tape).setOnClickListener(this);
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

        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), tabs.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        viewPager.setCurrentItem(selectedTab);
    }

    // Обработка нажатия на элементы горизонтального ScrollBar
    private void makeTabSelectedListener() {
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectedTab = tabs.getSelectedTabPosition();
                prefUtils.saveSelectedCategoryPosition(selectedTab);
                viewPager.setCurrentItem(selectedTab);
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
            removeAllDataFromDB();
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        showSnackBarOnTheTopByBottomNavigation(bottomNavigation, getString(R.string.press_twice_to_exit));
        new Handler().postDelayed(() -> doubleBackToExitPressedOnce=false, 2000);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.search_button_tape: // "Перейти на экран Поиска"
                startActivity(new Intent(this, SearchActivity.class));
                finish();
                break;
            case R.id.activity_button: // "Перейти на экран Активность"
                startActivity(new Intent(this, AnswersActivity.class));
                finish();
                break;
            case R.id.favorites_button: // "Перейти на экран Избранное"
                startActivity(new Intent(this, FavoriteActivity.class));
                finish();
                break;
            case R.id.profile_button: // "Перейти на экран Профиль"
                startActivity(new Intent(this, ProfileActivity.class));
                finish();
                break;
        }
    }
}
