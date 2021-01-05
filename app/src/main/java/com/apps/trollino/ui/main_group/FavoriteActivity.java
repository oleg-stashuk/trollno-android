package com.apps.trollino.ui.main_group;

import android.content.Intent;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.trollino.R;
import com.apps.trollino.ui.authorisation.LoginActivity;
import com.apps.trollino.ui.authorisation.RegistrationActivity;
import com.apps.trollino.ui.base.BaseActivity;
import com.apps.trollino.utils.OpenActivityHelper;
import com.apps.trollino.utils.data.FavoritePostListFromApi;
import com.apps.trollino.utils.recycler.MakeLinerRecyclerViewForFavoriteActivity;

public class FavoriteActivity extends BaseActivity implements View.OnClickListener{
    private RecyclerView favoriteRecyclerView;
    private View noFavoriteListView;
    private View userAuthorizationView;
    private ProgressBar progressBar;
    private boolean isUserAuthorization; // Пользователь авторизирован или нет
    private boolean doubleBackToExitPressedOnce = false;  // для обработки нажатия onBackPressed

    @Override
    protected int getLayoutID() {
        return R.layout.activity_favorite;
    }

    @Override
    protected void initView() {
        favoriteRecyclerView = findViewById(R.id.recycler_favorite);
        noFavoriteListView = findViewById(R.id.include_no_favorite);
        userAuthorizationView = findViewById(R.id.include_user_not_authorization_favorite);
        progressBar = findViewById(R.id.progress_bar_favorite);
        findViewById(R.id.tape_button_favorite).setOnClickListener(this);
        findViewById(R.id.activity_button_favorite).setOnClickListener(this);
        findViewById(R.id.profile_button_favorite).setOnClickListener(this);
        findViewById(R.id.login_button_include_favorite_for_guest).setOnClickListener(this);
        findViewById(R.id.registration_button_include_activity_for_guest).setOnClickListener(this);

        prefUtils.saveCurrentActivity(OpenActivityHelper.FAVORITE_ACTIVITY);
        isUserAuthorization = prefUtils.getIsUserAuthorization();
        FavoritePostListFromApi.getInstance().removeAllDataFromList(prefUtils);

        checkFavoriteListAndUserAuthorization(); // проверить пользователь авторизирован или нет, если да - то проверить есть посты добаленные в избранное или нет
        initToolbar();
    }

    private void checkFavoriteListAndUserAuthorization() {
        if(isUserAuthorization) {
            userAuthorizationView.setVisibility(View.GONE);
            MakeLinerRecyclerViewForFavoriteActivity.makeLinerRecyclerViewForFavoriteActivity(this, favoriteRecyclerView, progressBar ,prefUtils, noFavoriteListView);
        } else {
            userAuthorizationView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
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

    // Добавить Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(isUserAuthorization) {
            getMenuInflater().inflate(R.menu.favorite_menu, menu);
            return true;
        } else {
            return false;
        }
    }

    // Обрабтка нажантия на выпадающий список из Menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.to_do_something) {
            showToast("Что-то сделать");
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        prefUtils.saveCurrentActivity("");
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        showToast(getString(R.string.press_twice_to_exit));
        new Handler().postDelayed(() -> doubleBackToExitPressedOnce=false, 2000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tape_button_favorite: // "Перейти на экран Лента"
                startActivity(new Intent(this, TapeActivity.class));
                finish();
                break;
            case R.id.activity_button_favorite: // "Перейти на экран Активность"
                startActivity(new Intent(this, ActivityInPostActivity.class));
                finish();
                break;
            case R.id.profile_button_favorite: // "Перейти на экран Профиль"
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