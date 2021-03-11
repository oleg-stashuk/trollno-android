package com.apps.trollino.ui.main_group;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.trollino.R;
import com.apps.trollino.ui.base.BaseActivity;
import com.apps.trollino.utils.OnSwipeTouchListener;
import com.apps.trollino.utils.OpenActivityHelper;
import com.apps.trollino.utils.ShowAdvertising;
import com.apps.trollino.utils.dialogs.GuestDialog;
import com.apps.trollino.utils.networking.single_post.GetItemPost;
import com.apps.trollino.utils.networking.single_post.PostBookmark;
import com.apps.trollino.utils.networking.single_post.PostUnbookmark;
import com.facebook.shimmer.ShimmerFrameLayout;

public class PostActivity extends BaseActivity implements View.OnClickListener{
    private NestedScrollView nestedScrollView;
    private LinearLayout postLayout;
    private ShimmerFrameLayout shimmerLayout;
    private LinearLayout swipedImageView;
    private RelativeLayout advRelativeLayout;
    private TextView categoryTextView;
    private TextView titleTextView;
    private TextView countCommentTextView;
    private TextView body;
    private Button commentButton;

    private Menu menu;
    private RecyclerView partOfPostRecyclerView;
    private boolean isPostFromCategory;
    private String currentPostId;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_post;
    }

    @Override
    protected void initView() {
        nestedScrollView = findViewById(R.id.include_post_screen);
        postLayout = findViewById(R.id.post_layout);
        shimmerLayout = findViewById(R.id.include_shimmer_post);
        swipedImageView = findViewById(R.id.swiped_image_post_activity);
        advRelativeLayout = findViewById(R.id.ad_mob_in_post);
        partOfPostRecyclerView = findViewById(R.id.recycler_post_activity);
        categoryTextView = findViewById(R.id.category_post_activity);
        titleTextView = findViewById(R.id.title_post_activity);
        countCommentTextView = findViewById(R.id.comment_count_post_activity);
        body = findViewById(R.id.body_post_activity);
        commentButton = findViewById(R.id.add_comment_button_post_activity);
        commentButton.setOnClickListener(this);

        initToolbar();
        makeTouchListener();
        ShowAdvertising.showAdvertising(advRelativeLayout, prefUtils, this);

        prefUtils.saveCurrentActivity(OpenActivityHelper.POST_ACTIVITY);
        currentPostId = prefUtils.getCurrentPostId();
        isPostFromCategory = prefUtils.IsPostFromCategoryList();

        categoryTextView.setFocusable(true);
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
        getPostFromAPi(currentPostId);
        return true;
    }

    // Действия при свайпах в разные стороны
    private void makeTouchListener() {
        swipedImageView.setOnTouchListener(new OnSwipeTouchListener(this) {
//        layout.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipeRight() {
                String nextPostId = prefUtils.getNextPostId();
                if(!nextPostId.isEmpty() && nextPostId.length() > 0 && !nextPostId.equals("0")) {
                    getPostFromAPi(nextPostId);
                    currentPostId = nextPostId;
                }
            }

            public void onSwipeLeft() {
                String prevPostId = prefUtils.getPrevPostId();
                if(!prevPostId.isEmpty() && prevPostId.length() > 0 && !prevPostId.equals("0")) {
                    getPostFromAPi(prevPostId);
                    currentPostId = prevPostId;
                }
            }
        });
    }

    // Get post data by Id from API
    private void getPostFromAPi(String postId) {
        nestedScrollView.smoothScrollTo(0, nestedScrollView.getTop());
        postLayout.setVisibility(View.GONE);
        shimmerLayout.setVisibility(View.VISIBLE);
        new Thread(() -> {
            GetItemPost.getItemPost(PostActivity.this, prefUtils,
                    partOfPostRecyclerView, menu, categoryTextView, postId, titleTextView,
                    countCommentTextView, commentButton, body, isPostFromCategory, findViewById(R.id.activity_post), postLayout, shimmerLayout);
        }).start();
    }

    // Open activity with category
    private void commentToPostActivity() {
        prefUtils.saveCommentIdForActivity("");
        prefUtils.saveCurrentPostId(currentPostId);
        prefUtils.saveValuePostFromCategoryList(isPostFromCategory);
        prefUtils.saveCurrentActivity("");
        startActivity(new Intent( this, CommentToPostActivity.class));
        finish();
    }

    // Press "favorite" button
    private void pressFavoriteButton() {
        if (!prefUtils.getCookie().isEmpty()) {
            if (prefUtils.getIsFavorite()) {
                new Thread(() -> PostUnbookmark.removePostFromFavorite(this, prefUtils, currentPostId, menu, findViewById(R.id.activity_post))).start();
            } else {
                new Thread(() -> PostBookmark.addPostToFavorite(this, prefUtils, currentPostId, menu, findViewById(R.id.activity_post))).start();
            }
        } else {
            GuestDialog dialog = new GuestDialog();
            dialog.showDialog(this);
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
                commentToPostActivity();
                break;
            case R.id.favorite_button:
                pressFavoriteButton();
                break;
            }
        return true;
    }

    @Override
    public void onBackPressed() {
        prefUtils.saveCurrentActivity("");
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