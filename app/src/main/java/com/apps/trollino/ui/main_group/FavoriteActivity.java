package com.apps.trollino.ui.main_group;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.trollino.R;
import com.apps.trollino.adapters.FavoriteVideoAdapter;
import com.apps.trollino.adapters.base.BaseRecyclerAdapter;
import com.apps.trollino.model.FavoriteModel;
import com.apps.trollino.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends BaseActivity implements View.OnClickListener{
    private RecyclerView favoriteRecyclerView;
    private List<FavoriteModel> favoriteVideoList = FavoriteModel.makeFavoriteVideoList();
//    private List<FavoriteModel> favoriteVideoList = new ArrayList<>();
    private View noFavoriteListView;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_favorite;
    }

    @Override
    protected void initView() {
        favoriteRecyclerView = findViewById(R.id.recycler_favorite);
        noFavoriteListView = findViewById(R.id.include_no_favorite);
        findViewById(R.id.tape_button_favorite).setOnClickListener(this);
        findViewById(R.id.activity_button_favorite).setOnClickListener(this);
        findViewById(R.id.profile_button_favorite).setOnClickListener(this);

        checkFavoriteList();
        initToolbar();
    }

    private void checkFavoriteList() {
        if(favoriteVideoList.isEmpty()) {
            noFavoriteListView.setVisibility(View.VISIBLE);
            favoriteRecyclerView.setVisibility(View.GONE);
        } else {
            noFavoriteListView.setVisibility(View.GONE);
            favoriteRecyclerView.setVisibility(View.VISIBLE);
            makeFavoriteRecyclerView();
        }
    }

    private void makeFavoriteRecyclerView() {
        favoriteRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        favoriteRecyclerView.setAdapter(new FavoriteVideoAdapter(this, favoriteVideoList, favoriteVideoItemListener));
    }

    // Обработка нажатия на элемент списка
    private final FavoriteVideoAdapter.OnItemClick<FavoriteModel> favoriteVideoItemListener =
            new BaseRecyclerAdapter.OnItemClick<FavoriteModel>() {
        @Override
        public void onItemClick(FavoriteModel item, int position) {
            showToast("Press " + item.getVideoId());
            startActivity(new Intent(FavoriteActivity.this, PostActivity.class));
            finish();
        }
    };

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
        getMenuInflater().inflate(R.menu.favorite_menu, menu);
        return true;
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
        }
    }
}