package com.apps.trollino.ui.main_group;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.trollino.R;
import com.apps.trollino.adapters.CommentToPostParentAdapter;
import com.apps.trollino.data.model.UserCommentActivityModel;
import com.apps.trollino.ui.base.BaseActivity;

import java.util.List;

import static com.apps.trollino.ui.main_group.PostActivity.POST_FAVORITE_VALUE;
import static com.apps.trollino.ui.main_group.PostActivity.POST_FROM_CATEGORY_LIST;
import static com.apps.trollino.ui.main_group.PostActivity.POST_ID_KEY;

public class CommentToPostActivity extends BaseActivity implements View.OnClickListener{

    private List<UserCommentActivityModel> commentList = UserCommentActivityModel.makeCommentsListToPostParent();
    private int countComment = 7;

    private TextView noCommentTextView;
    private RecyclerView commentsRecyclerView;
    private EditText commentEditText;
    private Spinner sortCommentSpinner;

    private String currentPostId;
    private boolean isPostFromCategory;

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
        TextView countTextView = findViewById(R.id.count_comment_to_post);
        sortCommentSpinner = findViewById(R.id.spinner_comment_to_post);
        noCommentTextView = findViewById(R.id.text_post_without_comment_comment_to_post);

        countTextView.setText(String.valueOf(countComment));


        currentPostId = this.getIntent().getStringExtra(POST_ID_KEY);
        isPostFromCategory = this.getIntent().getBooleanExtra(POST_FROM_CATEGORY_LIST, false);


        makeCommentListRecyclerView();
        showCorrectVariant();     // Если для Поста нет комментариев, то выводится на экран сообщение что комментариев нет
        makeSortCommentSpinner(this);
    }

    private void makeSortCommentSpinner(final Context context) {
        final String[] sortCommentArray = getResources().getStringArray(R.array.sort_comment_value);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sortCommentArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortCommentSpinner.setAdapter(adapter);
        sortCommentSpinner.setSelection(0);
        sortCommentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView)parent.getChildAt(0)).setTextColor(ContextCompat.getColor(context, R.color.white));
                Log.d("12345", sortCommentArray[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void makeCommentListRecyclerView() {
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
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
    public void onBackPressed() {
        Intent intent = new Intent(this, PostActivity.class);
        intent.putExtra(POST_FAVORITE_VALUE, 0);
        intent.putExtra(POST_ID_KEY, currentPostId);
        intent.putExtra(POST_FROM_CATEGORY_LIST,isPostFromCategory);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button_comment_comment_to_post:
                onBackPressed();
                break;
            case R.id.send_button_comment_comment_to_post:
                showToast("Отправить комментарий");
                break;

        }
    }
}