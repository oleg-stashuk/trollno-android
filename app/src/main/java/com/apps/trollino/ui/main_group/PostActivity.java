package com.apps.trollino.ui.main_group;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.trollino.R;
import com.apps.trollino.adapters.OnePostElementAdapter;
import com.apps.trollino.data.model.ItemPostModel;
import com.apps.trollino.ui.base.BaseActivity;
import com.apps.trollino.utils.OnSwipeTouchListener;
import com.apps.trollino.utils.networking.GetItemPost;

import java.util.List;

public class PostActivity extends BaseActivity implements View.OnClickListener{
    public static String POST_ID_KEY = "POST_ID_KEY";
    public static String POST_FAVORITE_VALUE = "POST_FAVORITE_VALUE";
    // Если пост открыт с категории, то предыдущий/следующий пост смотрим в данных с API для "category", если со Свежее или Обсуждаемое - с "publ"
    public static String POST_FROM_CATEGORY_LIST = "POST_FROM_CATEGORY_LIST";

    private NestedScrollView layout;
    private TextView categoryTextView;
    private TextView titleTextView;
    private TextView countCommentTextView;
    private ImageView imageView;
    private TextView body;
    private Button commentButton;

    private Menu menu;
    private RecyclerView partOfPostRecyclerView;
    private List<ItemPostModel.OneElementPost> postElementsList = ItemPostModel.OneElementPost.makePostElementsList();
    private boolean isFavoritePost;
    private boolean isPostFromCategory;
    private String currentPostId;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_post;
    }

    @Override
    protected void initView() {
        layout = findViewById(R.id.include_post_screen);
        partOfPostRecyclerView = findViewById(R.id.recycler_post_activity);
        categoryTextView = findViewById(R.id.category_post_activity);
        titleTextView = findViewById(R.id.title_post_activity);
        countCommentTextView = findViewById(R.id.comment_count_post_activity);
        imageView = findViewById(R.id.image_post_activity);
        body = findViewById(R.id.body_post_activity);
        commentButton = findViewById(R.id.add_comment_button_post_activity);
        commentButton.setOnClickListener(this);

        makePartOfPostRecyclerView();
        initToolbar();
        makeTouchListener();

        int favoriteValue = this.getIntent().getIntExtra(POST_FAVORITE_VALUE, 0);
        currentPostId = this.getIntent().getStringExtra(POST_ID_KEY);
        isPostFromCategory = this.getIntent().getBooleanExtra(POST_FROM_CATEGORY_LIST, false);
        categoryTextView.setFocusable(true);

        if (favoriteValue == 0) {
            isFavoritePost = false;
        } else {
            isFavoritePost = true;
        }

        getPostFromAPi(currentPostId);
    }

    private void makePartOfPostRecyclerView() {
        partOfPostRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        partOfPostRecyclerView.setAdapter(new OnePostElementAdapter(this, postElementsList));
        partOfPostRecyclerView.setFocusable(false);
//                                                                                                  !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//        partOfPostRecyclerView.setOnFlingListener(new RecyclerView.OnFlingListener() {
//            int MAX_VELOCITY_X = 10;
//
//            @Override
//            public boolean onFling(int velocityX, int velocityY) {
//                Log.d("OkHttp", velocityX + " " + velocityY);
//                if (Math.abs(velocityY) > 100) {
//                    velocityY = 100 * (int) Math.signum((double)velocityY);
//                    partOfPostRecyclerView.fling(velocityX, velocityY);
//                    Log.d("OkHttp", "!!!!!!!!!!!!!!!!!!!!!");
//                    return true;
//                }
//                if (Math.abs(velocityX) > 100) {
//                    velocityX = 10 * (int) Math.signum((double)velocityX);
//                    partOfPostRecyclerView.fling(velocityX, velocityY);
//                    Log.d("OkHttp", "222222222222222222222");
//                    return true;
//                }
//
//                return false;
//            }
//        });
//                                                                                                  !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
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

    // Действия при свайпах в разные стороны
    private void makeTouchListener() {
        layout.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipeRight() {
                showToast("onSwipeRight");
                String nextPostId = prefUtils.getNextPostId();
                if(!nextPostId.isEmpty() && nextPostId.length() > 0 && !nextPostId.equals("0")) {
                    getPostFromAPi(nextPostId);
                    currentPostId = nextPostId;
                }
            }

            public void onSwipeLeft() {
                showToast("onSwipeLeft");
                String prevPostId = prefUtils.gePrevPostId();
                if(!prevPostId.isEmpty() && prevPostId.length() > 0 && !prevPostId.equals("0")) {
                    getPostFromAPi(prevPostId);
                    currentPostId = prevPostId;
                }
            }
        });
    }

    // Get post data by Id from API
    private void getPostFromAPi(String postId) {
        layout.smoothScrollTo(0, layout.getTop());
        new Thread(() -> {
            GetItemPost.getItemPost(PostActivity.this, prefUtils, categoryTextView, postId, titleTextView, countCommentTextView, commentButton, imageView, body, isPostFromCategory);
        }).start();
    }

    // Open activity with category
    private void commentToPostActivity() {
        Intent intent = new Intent( this, CommentToPostActivity.class);
        intent.putExtra(POST_ID_KEY, currentPostId);
        intent.putExtra(POST_FROM_CATEGORY_LIST, isPostFromCategory);
        startActivity(intent);
        finish();
    }

    // Обрабтка нажантия на выпадающий список из Menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.comment_button:
                commentToPostActivity();
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
                commentToPostActivity();
                break;
        }
    }
}