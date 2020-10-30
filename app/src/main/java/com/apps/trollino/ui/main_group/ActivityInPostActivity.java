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
import com.apps.trollino.adapters.UserCommentAdapter;
import com.apps.trollino.adapters.base.BaseRecyclerAdapter;
import com.apps.trollino.model.UserCommentActivityModel;
import com.apps.trollino.ui.base.BaseActivity;

import java.util.List;

import static com.apps.trollino.model.UserCommentActivityModel.makeUserCommentList;

public class ActivityInPostActivity extends BaseActivity implements View.OnClickListener{
    private List<UserCommentActivityModel> userCommentList = makeUserCommentList();
    private RecyclerView postWithActivityRecyclerView;

    private View userAuthorizationView;
    private boolean isUserAuthorization; // Пользователь авторизирован или нет

    @Override
    protected int getLayoutID() {
        return R.layout.activity_in_post;
    }

    @Override
    protected void initView() {
        userAuthorizationView = findViewById(R.id.include_user_not_authorization_activity_in_post);
        postWithActivityRecyclerView = findViewById(R.id.recycler_activity_in_post);
        findViewById(R.id.tape_button_activity_in_post).setOnClickListener(this);
        findViewById(R.id.favorites_button_activity_in_post).setOnClickListener(this);
        findViewById(R.id.profile_button_activity_in_post).setOnClickListener(this);
        findViewById(R.id.include_login_button_user_not_authorization).setOnClickListener(this);
        findViewById(R.id.include_registration_button_user_not_authorization).setOnClickListener(this);

        isUserAuthorization = prefsUtils.getIsUserAuthorization();

        checkUserAuthorization(); // проверить пользователь авторизирован или нет, если да - то проверить есть посты добаленные в избранное или нет
        initToolbar();
    }

    private void checkUserAuthorization() {
        if(isUserAuthorization) {
            userAuthorizationView.setVisibility(View.GONE);
            postWithActivityRecyclerView.setVisibility(View.VISIBLE);
            makePostWithActivityRecyclerView();
        } else {
            userAuthorizationView.setVisibility(View.VISIBLE);
            postWithActivityRecyclerView.setVisibility(View.GONE);
        }
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
            startActivity(new Intent(ActivityInPostActivity.this, CommentToPostActivity.class));
            finish();
        }
    };

    // Иницировать Toolbar
    private void initToolbar() {
        final Toolbar toolbar = findViewById(R.id.toolbar_in_post);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getString(R.string.activity));

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
            getMenuInflater().inflate(R.menu.post_with_activity_menu, menu);
            return true;
        } else {
            return false;
        }
    }

    // Обрабтка нажатия на выпадающий список из Menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.mark_all_as_read_menu) {
            showToast(getString(R.string.mark_all_as_read));
        }
        return true;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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