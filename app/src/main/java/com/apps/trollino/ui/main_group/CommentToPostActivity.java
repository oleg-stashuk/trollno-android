package com.apps.trollino.ui.main_group;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.trollino.R;
import com.apps.trollino.adapters.CommentToPostParentAdapter;
import com.apps.trollino.model.UserCommentActivityModel;
import com.apps.trollino.ui.base.BaseActivity;

import java.util.List;

public class CommentToPostActivity extends BaseActivity implements View.OnClickListener{

    private List<UserCommentActivityModel> commentList = UserCommentActivityModel.makeCommentsListToPostParent();
    private int countComment = 7;
    private String title = "Заголовок поста";

    private TextView noCommentTextView;
    private RecyclerView commentsRecyclerView;
    private EditText commentEditText;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_comment_to_post;
    }

    @Override
    protected void initView() {
        commentsRecyclerView = findViewById(R.id.recycler_comment_comment_to_post);
        findViewById(R.id.back_button_comment_comment_to_post).setOnClickListener(this);
        findViewById(R.id.send_button_comment_comment_to_post).setOnClickListener(this);
        commentEditText = findViewById(R.id.comment_message_comment_comment_to_post);
        TextView countTextView = findViewById(R.id.count_comment_comment_to_post);
        TextView titleTextView = findViewById(R.id.title_comment_comment_to_post);
        noCommentTextView = findViewById(R.id.text_post_without_comment_comment_to_post);

        showCorrectVariant();
        countTextView.setText(String.valueOf(countComment));
        titleTextView.setText(title);

        makeCommentListRecyclerView();
    }

    private void makeCommentListRecyclerView() {
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        commentsRecyclerView.setAdapter(new CommentToPostParentAdapter(this, commentList));
        commentsRecyclerView.setAdapter(new CommentToPostParentAdapter(this, commentList, commentEditText));
    }

    // Если для Поста нет комментариев, то выводится на экран сообщение что комментариев нет
    private void showCorrectVariant() {
        if(countComment > 0) {
            noCommentTextView.setVisibility(View.GONE);
            commentsRecyclerView.setVisibility(View.VISIBLE);
        } else {
            noCommentTextView.setVisibility(View.VISIBLE);
            commentsRecyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button_comment_comment_to_post:
                startActivity(new Intent(this, PostActivity.class));
                finish();
                break;
            case R.id.send_button_comment_comment_to_post:
                showToast("Отправить комментарий");
                break;

        }
    }
}