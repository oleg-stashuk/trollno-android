package com.apps.trollino.ui.main_group;

import android.content.Intent;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.trollino.R;
import com.apps.trollino.adapters.NewsVideoAdapter;
import com.apps.trollino.adapters.base.BaseRecyclerAdapter;
import com.apps.trollino.model.FavoriteVideoModel;
import com.apps.trollino.ui.base.BaseActivity;

import java.util.List;

public class TapeActivity extends BaseActivity implements View.OnClickListener{
    private RecyclerView newsRecyclerView;
    private List<FavoriteVideoModel> newsVideoList = FavoriteVideoModel.makeFavoriteVideoList();

    @Override
    protected int getLayoutID() {
        return R.layout.activity_tape;
    }

    @Override
    protected void initView() {
        newsRecyclerView = findViewById(R.id.news_recycler_tape);
        findViewById(R.id.search_button_tape).setOnClickListener(this);
        findViewById(R.id.activity_button_tape).setOnClickListener(this);
        findViewById(R.id.favorites_button_tape).setOnClickListener(this);
        findViewById(R.id.profile_button_tape).setOnClickListener(this);

        makeNewsRecyclerView();
    }

    private void makeNewsRecyclerView() {
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        newsRecyclerView.setAdapter(new NewsVideoAdapter(this, newsVideoList, newsVideoItemListener));
    }

    // Обработка нажатия на элемент списка
    private final NewsVideoAdapter.OnItemClick<FavoriteVideoModel> newsVideoItemListener =
            new BaseRecyclerAdapter.OnItemClick<FavoriteVideoModel>() {
                @Override
                public void onItemClick(FavoriteVideoModel item, int position) {
                    showToast("Press " + item.getVideoId());
                }
            };

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.search_button_tape: // "Перейти на экран Поиска"
                showToast("Open SearchActivity");
                break;
            case R.id.activity_button_tape: // "Перейти на экран Активность"
                showToast("Перейти на экран Активность");
                break;
            case R.id.favorites_button_tape: // "Перейти на экран Избранное"
                startActivity(new Intent(this, FavoriteActivity.class));
                finish();
                break;
            case R.id.profile_button_tape: // "Перейти на экран Профиль"
                startActivity(new Intent(this, ProfileActivity.class));
                finish();
                break;
        }
    }
}