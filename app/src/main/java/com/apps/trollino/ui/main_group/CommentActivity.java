package com.apps.trollino.ui.main_group;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.apps.trollino.R;
import com.apps.trollino.ui.base.BaseActivity;

public class CommentActivity extends BaseActivity implements View.OnClickListener{
    TextView noCommentTextView;
    RecyclerView commentsRecyclerView;

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
        noCommentTextView = findViewById(R.id.text_post_without_comment);

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