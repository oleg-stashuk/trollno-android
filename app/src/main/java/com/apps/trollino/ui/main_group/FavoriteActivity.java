package com.apps.trollino.ui.main_group;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.trollino.R;
import com.apps.trollino.ui.authorisation.LoginActivity;
import com.apps.trollino.ui.authorisation.RegistrationActivity;
import com.apps.trollino.ui.base.BaseActivity;
import com.apps.trollino.utils.OpenActivityHelper;
import com.apps.trollino.utils.data.FavoritePostListFromApi;
import com.apps.trollino.utils.networking.user_action.GetNewAnswersCount;
import com.apps.trollino.utils.recycler.MakeLinerRecyclerViewForFavoriteActivity;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import static com.apps.trollino.utils.SnackBarMessageCustom.showSnackBarOnTheTopByBottomNavigation;

public class FavoriteActivity extends BaseActivity implements View.OnClickListener{
    private ShimmerFrameLayout shimmer;
    private SwipyRefreshLayout refreshLayout;
    private LinearLayout bottomNavigation;
    private RecyclerView favoriteRecyclerView;
    private View noFavoriteListView;
    private View userAuthorizationView;
    private ImageView indicatorImageView;
    private boolean isUserAuthorization; // Пользователь авторизирован или нет
    private boolean doubleBackToExitPressedOnce = false;  // для обработки нажатия onBackPressed

    @Override
    protected int getLayoutID() {
        return R.layout.activity_favorite;
    }

    @Override
    protected void initView() {
        shimmer = findViewById(R.id.include_favorite_shimmer);
        refreshLayout = findViewById(R.id.refresh_layout_favorite);
        bottomNavigation = findViewById(R.id.bottom_navigation_favorite);
        favoriteRecyclerView = findViewById(R.id.recycler_favorite);
        noFavoriteListView = findViewById(R.id.include_no_favorite);
        userAuthorizationView = findViewById(R.id.include_user_not_authorization_favorite);
        TextView favoriteBottomNavigationTextView = findViewById(R.id.favorites_button);
        indicatorImageView = findViewById(R.id.indicator_image);
        findViewById(R.id.tape_button).setOnClickListener(this);
        findViewById(R.id.activity_button).setOnClickListener(this);
        findViewById(R.id.profile_button).setOnClickListener(this);
        findViewById(R.id.login_button_include_favorite_for_guest).setOnClickListener(this);
        findViewById(R.id.registration_button_include_activity_for_guest).setOnClickListener(this);

        prefUtils.saveCurrentActivity(OpenActivityHelper.FAVORITE_ACTIVITY);
        isUserAuthorization = prefUtils.getIsUserAuthorization();
        FavoritePostListFromApi.getInstance().removeAllDataFromList(prefUtils);

        favoriteBottomNavigationTextView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_favorite_green, 0, 0);
        favoriteBottomNavigationTextView.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        if(isUserAuthorization) {
            shimmer.setVisibility(View.VISIBLE);
            new Thread(() -> GetNewAnswersCount.getNewAnswersCount(this, prefUtils, indicatorImageView)).start();
        }

        checkFavoriteListAndUserAuthorization(); // проверить пользователь авторизирован или нет, если да - то проверить есть посты добаленные в избранное или нет
        updateDataBySwipe();
        initToolbar();
    }

    private void checkFavoriteListAndUserAuthorization() {
        if(isUserAuthorization) {
            userAuthorizationView.setVisibility(View.GONE);
            getDataFromApi(shimmer, null, true);
        } else {
            userAuthorizationView.setVisibility(View.VISIBLE);
        }
    }

    private void updateDataBySwipe() {
        refreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorPrimary));
        refreshLayout.setOnRefreshListener(direction -> {
            getDataFromApi(null, refreshLayout, (direction == SwipyRefreshLayoutDirection.TOP));
        });
    }

    private void getDataFromApi(ShimmerFrameLayout shimmerToShow, SwipyRefreshLayout refreshTopLayoutToShow, boolean isNewData) {
        MakeLinerRecyclerViewForFavoriteActivity.makeLinerRecyclerViewForFavoriteActivity(this, prefUtils, favoriteRecyclerView,
                shimmerToShow, refreshTopLayoutToShow,  noFavoriteListView, bottomNavigation, isNewData);
    }

    // Иницировать Toolbar
    private void initToolbar() {
        final Toolbar toolbar = findViewById(R.id.toolbar_favorite);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getString(R.string.favorites));

        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false); // отображать кнопку BackPress
            getSupportActionBar().setHomeButtonEnabled(false); // вернуться на предыдущую активность
            getSupportActionBar().setDisplayShowTitleEnabled(true); // отображать Заголовок
        }
    }

    @Override
    public void onBackPressed() {
        prefUtils.saveCurrentActivity("");
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        showSnackBarOnTheTopByBottomNavigation(bottomNavigation, getString(R.string.press_twice_to_exit));
        new Handler().postDelayed(() -> doubleBackToExitPressedOnce=false, 2000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tape_button: // "Перейти на экран Лента"
                startActivity(new Intent(this, TapeActivity.class));
                finish();
                break;
            case R.id.activity_button: // "Перейти на экран Активность"
                startActivity(new Intent(this, ActivityInPostActivity.class));
                finish();
                break;
            case R.id.profile_button: // "Перейти на экран Профиль"
                startActivity(new Intent(this, ProfileActivity.class));
                finish();
                break;
            case R.id.login_button_include_favorite_for_guest: // "Перейти на экран Авторизации"
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            case R.id.registration_button_include_activity_for_guest: // "Перейти на экран Регистрации"
                startActivity(new Intent(this, RegistrationActivity.class));
                finish();
                break;
        }
    }
}
