package com.apps.trollino.ui.main_group;

import android.content.Intent;
import android.view.View;

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
        findViewById(R.id.drop_down_menu_favorite).setOnClickListener(this);
        findViewById(R.id.tape_button_favorite).setOnClickListener(this);
        findViewById(R.id.activity_button_favorite).setOnClickListener(this);
        findViewById(R.id.profile_button_favorite).setOnClickListener(this);

        checkFavoriteList();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.drop_down_menu_favorite:
                showToast("drop_down_menu");
                break;
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