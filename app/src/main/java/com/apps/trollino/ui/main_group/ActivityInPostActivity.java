package com.apps.trollino.ui.main_group;

import android.content.Intent;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.trollino.R;
import com.apps.trollino.adapters.UserCommentAdapter;
import com.apps.trollino.adapters.base.BaseRecyclerAdapter;
import com.apps.trollino.model.UserCommentActivityModel;
import com.apps.trollino.ui.base.BaseActivity;

import java.util.List;

import static com.apps.trollino.model.UserCommentActivityModel.makeUserCommentList;

public class ActivityInPostActivity extends BaseActivity implements View.OnClickListener{
    private List<UserCommentActivityModel> userCommentList = makeUserCommentList();
    private RecyclerView postWithActivityRecyclerView;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_in_post;
    }

    @Override
    protected void initView() {
        postWithActivityRecyclerView = findViewById(R.id.recycler_activity_in_post);
        findViewById(R.id.menu_button_activity_in_post).setOnClickListener(this);
        findViewById(R.id.tape_button_activity_in_post).setOnClickListener(this);
        findViewById(R.id.favorites_button_activity_in_post).setOnClickListener(this);
        findViewById(R.id.profile_button_activity_in_post).setOnClickListener(this);

        makePostWithActivityRecyclerView();
    }

    private void makePostWithActivityRecyclerView() {
        postWithActivityRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        postWithActivityRecyclerView.setAdapter(new UserCommentAdapter(this, userCommentList, newsVideoGridItemListener));
    }

    // Обработка нажатия на элемент списка из ресайклера
    private final UserCommentAdapter.OnItemClick<UserCommentActivityModel> newsVideoGridItemListener = new BaseRecyclerAdapter.OnItemClick<UserCommentActivityModel>() {
        @Override
        public void onItemClick(UserCommentActivityModel item, int position) {
            showToast("Press " + item.getTitle());
            startActivity(new Intent(ActivityInPostActivity.this, PostActivity.class));
            finish();
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_button_activity_in_post: // "Перейти на экран Лента"
                showToast("Пометить все как прочитанное");
                break;
            case R.id.tape_button_activity_in_post: // "Перейти на экран Лента"
                startActivity(new Intent(this, TapeActivity.class));
                finish();
                break;
            case R.id.favorites_button_activity_in_post: // "Перейти на экран Избранное"
                startActivity(new Intent(this, FavoriteActivity.class));
                finish();
                break;
            case R.id.profile_button_activity_in_post: // "Перейти на экран Профиль"
                startActivity(new Intent(this, ProfileActivity.class));
                finish();
                break;
        }
    }
}