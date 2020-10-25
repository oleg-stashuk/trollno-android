package com.apps.trollino.ui.main_group;

import android.content.Intent;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.apps.trollino.R;
import com.apps.trollino.ui.base.BaseActivity;

public class FavoriteActivity extends BaseActivity implements View.OnClickListener{
    private RecyclerView favoriteRecyclerView;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_favorite;
    }

    @Override
    protected void initView() {
        favoriteRecyclerView = findViewById(R.id.recycler_favorite);
        findViewById(R.id.drop_down_menu_favorite).setOnClickListener(this);
        findViewById(R.id.tape_button_favorite).setOnClickListener(this);
        findViewById(R.id.activity_button_favorite).setOnClickListener(this);
        findViewById(R.id.profile_button_favorite).setOnClickListener(this);

        makeFavoriteRecyclerView();
    }

    private void makeFavoriteRecyclerView() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.drop_down_menu_favorite:
                showToast("drop_down_menu");
                break;
            case R.id.tape_button_favorite: // "Перейти на экран Лента"
                showToast("Перейти на экран Лента");
                break;
            case R.id.activity_button_favorite: // "Перейти на экран Активность"
                showToast("Перейти на экран Активность");
                break;
            case R.id.profile_button_favorite: // "Перейти на экран Профиль"
                startActivity(new Intent(this, ProfileActivity.class));
                finish();
                break;
        }
    }
}