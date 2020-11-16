package com.apps.trollino.ui.main_group;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.trollino.R;
import com.apps.trollino.adapters.OnePostElementAdapter;
import com.apps.trollino.data.model.PostModel;
import com.apps.trollino.ui.base.BaseActivity;

import java.util.List;

public class PostActivity extends BaseActivity implements View.OnClickListener{
    private Menu menu;
    private RecyclerView partOfPostRecyclerView;
    private String title = "Двуликая химера по кличке Кошка стала новой звездой интерннета";
    private List<PostModel.OneElementPost> postElementsList = PostModel.OneElementPost.makePostElementsList();
    private int countComment = 5;
    private boolean isFavoritePost = false;

    private TextView titleTextView;
    private Button commentButton;
    private TextView countCommentTextView;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_post;
    }

    @Override
    protected void initView() {
        partOfPostRecyclerView = findViewById(R.id.recycler_post_activity);
        titleTextView = findViewById(R.id.title_post_activity);
        countCommentTextView = findViewById(R.id.comment_count_post_activity);
        commentButton = findViewById(R.id.add_comment_button_post_activity);
        commentButton.setOnClickListener(this);

        titleTextView.setText(title);
        makePartOfPostRecyclerView();

        initToolbar();

        if(countComment > 0) {
            commentButton.setText("Читать комментарии");
            countCommentTextView.setVisibility(View.VISIBLE);
            countCommentTextView.setText(String.valueOf(countComment));
        } else {
            commentButton.setText("Написать комментарий");
            countCommentTextView.setVisibility(View.GONE);
        }
    }

    private void makePartOfPostRecyclerView() {
        partOfPostRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        partOfPostRecyclerView.setAdapter(new OnePostElementAdapter(this, postElementsList));
    }

    // Иницировать Toolbar
    private void initToolbar() {
        final Toolbar toolbar = findViewById(R.id.toolbar_post);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // отображать кнопку BackPress
            getSupportActionBar().setHomeButtonEnabled(true); // вернуться на предыдущую активность
            getSupportActionBar().setDisplayShowTitleEnabled(false); // отображать Заголовок
        }
    }

    // Добавить Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.post_menu, menu);
        this.menu = menu;
        changeImageFavoriteButton(); // Смена картинки для кнопки favorite
        return true;
    }

    // Смена картинки для кнопки favorite из menu в ToolBar
    private void changeImageFavoriteButton() {
        if (isFavoritePost) {
            menu.getItem(1).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_button));
        } else {
            menu.getItem(1).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_border_button));
        }
    }

    // Обрабтка нажантия на выпадающий список из Menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.comment_button:
                startActivity(new Intent(this, CommentToPostActivity.class));
                finish();
                break;
            case R.id.favorite_button:
                isFavoritePost = !isFavoritePost;
                changeImageFavoriteButton(); // Смена картинки для кнопки favorite
                break;
            }
        return true;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, TapeActivity.class));
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_comment_button_post_activity:
                startActivity(new Intent(this, CommentToPostActivity.class));
                finish();
                break;
        }
    }
}