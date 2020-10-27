package com.apps.trollino.ui.main_group;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.apps.trollino.R;
import com.apps.trollino.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class CommentActivity extends BaseActivity implements View.OnClickListener{

    private List<String> commentList = new ArrayList<>();
    private int countComment = 7;
    private String title = "Заголовок поста";

    private TextView noCommentTextView;
    private RecyclerView commentsRecyclerView;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_comment;
    }

    @Override
    protected void initView() {
        commentsRecyclerView = findViewById(R.id.recycler_comment);
        findViewById(R.id.back_button_comment).setOnClickListener(this);
        findViewById(R.id.send_button_comment).setOnClickListener(this);
        EditText commentEditText = findViewById(R.id.comment_message_comment);
        TextView countTextView = findViewById(R.id.count_comment);
        TextView titleTextVieww = findViewById(R.id.title_comment);
        noCommentTextView = findViewById(R.id.text_post_without_comment);

        showCorrectVariant();
        countTextView.setText(String.valueOf(countComment));
        titleTextVieww.setText(title);
    }

    // Если на Пост нет комментариев,
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
            case R.id.back_button_comment:
                startActivity(new Intent(this, PostActivity.class));
                finish();
                break;
            case R.id.send_button_comment:
                showToast("Отправить комментарий");
                break;

        }
    }
}