package com.apps.trollino.ui.main_group;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.trollino.R;
import com.apps.trollino.ui.base.BaseActivity;
import com.apps.trollino.utils.Const;
import com.apps.trollino.utils.data.CommentListFromApi;
import com.apps.trollino.utils.dialogs.GuestDialog;
import com.apps.trollino.utils.recycler.MakeRecyclerViewForComment;

public class CommentToPostActivity extends BaseActivity implements View.OnClickListener{

    private TextView noCommentTextView;
    private RecyclerView commentsRecyclerView;
    private EditText commentEditText;
    private Spinner sortCommentSpinner;
    private TextView countTextView;
    private ProgressBar progressBar;

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
        countTextView = findViewById(R.id.count_comment_to_post);
        sortCommentSpinner = findViewById(R.id.spinner_comment_to_post);
        progressBar = findViewById(R.id.progress_bar_comment_to_post);
        noCommentTextView = findViewById(R.id.text_post_without_comment_comment_to_post);

        currentPostId = prefUtils.getCurrentPostId();
        isPostFromCategory = prefUtils.IsPostFromCategoryList();

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
                String sortBy = position == 0 ? Const.SORT_BY_COUNT : Const.SORT_BY_CHANGE;
                String sortOrder = position == 0 ? Const.SORT_ORDER_BY_ASC : Const.SORT_ORDER_BY_DESC;

                CommentListFromApi.getInstance().removeAllDataFromList(prefUtils);
                MakeRecyclerViewForComment.makeRecyclerViewForComment(CommentToPostActivity.this,
                        prefUtils, commentsRecyclerView, progressBar, currentPostId, commentEditText,
                        noCommentTextView, countTextView, sortBy, sortOrder);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    @Override
    public void onBackPressed() {
        CommentListFromApi.getInstance().removeAllDataFromList(prefUtils);
        Intent intent = new Intent(this, PostActivity.class);
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
                if(prefUtils.getIsUserAuthorization()) {
                    showToast("Отправить комментарий");
                    Log.d("OkHttp", "Коментарий: " +  commentEditText.getText().toString()
                            + " -> " + commentEditText.getText().toString().length());


//                    PostNewComment.postNewComment(this, prefUtils, currentPostId,
//                            commentEditText.getText().toString(), "33", commentEditText);
                } else {
                    GuestDialog dialog = new GuestDialog();
                    dialog.showDialog(this);
                }
                break;

        }
    }
}