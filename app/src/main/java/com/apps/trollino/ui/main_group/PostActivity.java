package com.apps.trollino.ui.main_group;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.trollino.R;
import com.apps.trollino.adapters.OnePostElementAdapter;
import com.apps.trollino.model.PostModel;
import com.apps.trollino.ui.base.BaseActivity;
import com.apps.trollino.utils.AppBarLayoutBehavior;
import com.apps.trollino.utils.HidingScrollListener;
import com.google.android.material.appbar.AppBarLayout;

import java.util.List;

public class PostActivity extends BaseActivity implements View.OnClickListener{
    private RecyclerView partOfPostRecyclerView;
    private String title = "Двуликая химера по кличке Кошка стала новой звездой интерннета";
    private List<PostModel.OneElementPost> postElementsList = PostModel.OneElementPost.makePostElementsList();
    private int countComment = 5;
    private boolean isFavoritePost = false;

    private LinearLayout relativeLayout;
    private LinearLayout topLinerLayout;

    private TextView titleTextView;
    private Button commentButton;
    private ImageButton favoriteImageButton;
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
        findViewById(R.id.back_button_post_activity).setOnClickListener(this);
        favoriteImageButton = findViewById(R.id.favorite_button_post_activity);
        favoriteImageButton.setOnClickListener(this);
        findViewById(R.id.comment_button_post_activity).setOnClickListener(this);

        titleTextView.setText(title);
        makePartOfPostRecyclerView();
        changeFavoriteButton();

        if(countComment > 0) {
            commentButton.setText("Читать комментарии");
            countCommentTextView.setVisibility(View.VISIBLE);
            countCommentTextView.setText(String.valueOf(countComment));
        } else {
            commentButton.setText("Написать комментарий");
            countCommentTextView.setVisibility(View.GONE);
        }


//        listScroller();

        relativeLayout = findViewById(R.id.video_new_video_item);
        topLinerLayout = findViewById(R.id.top_liner_layout_post_activity);

        AppBarLayout appBarLayout = findViewById(R.id.appbar_post_activity);
        ((CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams()).setBehavior(new AppBarLayoutBehavior());

    }

    private void listScroller(){
        partOfPostRecyclerView.addOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                hideViews();
            }

            @Override
            public void onShow() {
                showViews();
            }
        });
    }


    private void hideViews() {
        topLinerLayout.animate().translationY(-topLinerLayout.getHeight()).setInterpolator(new AccelerateInterpolator(2));
//        partOfPostRecyclerView.animate().translationY(-topLinerLayout.getHeight()).setInterpolator(new AccelerateInterpolator(2)).start();
//        relativeLayout.animate().translationY(-topLinerLayout.getHeight()).setInterpolator(new AccelerateInterpolator(2));
    }

    private void showViews() {
        topLinerLayout.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
//        partOfPostRecyclerView.animate().translationY(0).setInterpolator(new AccelerateInterpolator(2)).start();
//        relativeLayout.animate().translationY(0).setInterpolator(new AccelerateInterpolator(2));
    }









    private void makePartOfPostRecyclerView() {
        partOfPostRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        partOfPostRecyclerView.setAdapter(new OnePostElementAdapter(this, postElementsList));
    }

    private void changeFavoriteButton() {
        if(isFavoritePost) {
            favoriteImageButton.setImageResource(R.drawable.ic_favorite_button);
        } else {
            favoriteImageButton.setImageResource(R.drawable.ic_favorite_border_button);
        }
    }

    private void favoriteAddOrRemove() {
        isFavoritePost = !isFavoritePost;
        changeFavoriteButton();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, TapeActivity.class));
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button_post_activity:
                onBackPressed();
                break;
            case R.id.favorite_button_post_activity:
                favoriteAddOrRemove();
                break;
            case R.id.comment_button_post_activity:
            case R.id.add_comment_button_post_activity:
                startActivity(new Intent(this, CommentToPostActivity.class));
                finish();
                break;
        }
    }
}