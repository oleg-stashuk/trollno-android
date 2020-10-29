package com.apps.trollino.ui.main_group;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.trollino.R;
import com.apps.trollino.adapters.OnePostElementAdapter;
import com.apps.trollino.model.PostModel;
import com.apps.trollino.ui.base.BaseActivity;

import java.util.List;

public class PostActivity extends BaseActivity implements View.OnClickListener{
    private RecyclerView partOfPostRecyclerView;
    private String title = "Двуликая химера по кличке Кошка стала новой звездой интерннета";
    private List<PostModel.OneElementPost> postElementsList = PostModel.OneElementPost.makePostElementsList();
    private int countComment = 5;

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
        findViewById(R.id.back_button_post_activity).setOnClickListener(this);
        findViewById(R.id.favorite_button_post_activity).setOnClickListener(this);
        findViewById(R.id.comment_button_post_activity).setOnClickListener(this);
        findViewById(R.id.share_button_post_activity).setOnClickListener(this);

        titleTextView.setText(title);
        makePartOfPostRecyclerView();

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button_post_activity:
                startActivity(new Intent(this, TapeActivity.class));
                finish();
                break;
            case R.id.favorite_button_post_activity:
                showToast("Добавить в избранное");
                break;
            case R.id.comment_button_post_activity:
            case R.id.add_comment_button_post_activity:
                startActivity(new Intent(this, CommentToPostActivity.class));
                finish();
                break;
            case R.id.share_button_post_activity:
                showToast("Поделиться");
                break;
        }
    }
}