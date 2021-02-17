package com.apps.trollino.ui.main_group;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.trollino.R;
import com.apps.trollino.ui.base.BaseActivity;
import com.apps.trollino.utils.OpenActivityHelper;
import com.apps.trollino.utils.data.CommentListFromApi;
import com.apps.trollino.utils.data.Const;
import com.apps.trollino.utils.dialogs.GuestDialog;
import com.apps.trollino.utils.networking.comment.PostNewComment;
import com.apps.trollino.utils.recycler.MakeRecyclerViewForComment;
import com.facebook.shimmer.ShimmerFrameLayout;

public class CommentToPostActivity extends BaseActivity implements View.OnClickListener{
    private ShimmerFrameLayout shimmer;
    private TextView noCommentTextView;
    private RecyclerView commentsRecyclerView;
    private EditText commentEditText;
    private Spinner sortCommentSpinner;
    private TextView countTextView;
    private ImageButton sendCommentImageButton;
    private ProgressBar progressBar;

    private String currentPostId;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_comment_to_post;
    }

    @Override
    protected void initView() {
        shimmer = findViewById(R.id.comments_to_post_shimmer);
        commentsRecyclerView = findViewById(R.id.recycler_comment_comment_to_post);
        findViewById(R.id.back_button_comment_comment_to_post).setOnClickListener(this);
        sendCommentImageButton = findViewById(R.id.send_button_comment_comment_to_post);
        sendCommentImageButton.setOnClickListener(this);
        commentEditText = findViewById(R.id.comment_message_comment_comment_to_post);
        countTextView = findViewById(R.id.count_comment_to_post);
        sortCommentSpinner = findViewById(R.id.spinner_comment_to_post);
        progressBar = findViewById(R.id.progress_bar_comment_to_post);
        noCommentTextView = findViewById(R.id.text_post_without_comment_comment_to_post);

        prefUtils.saveCurrentActivity(OpenActivityHelper.COMMENT_ACTIVITY);
        currentPostId = prefUtils.getCurrentPostId();
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

                CommentListFromApi.getInstance().removeAllDataFromList(prefUtils);
                MakeRecyclerViewForComment.makeRecyclerViewForComment(CommentToPostActivity.this,
                        prefUtils, commentsRecyclerView, shimmer, progressBar, currentPostId, commentEditText,
                        noCommentTextView, countTextView, sortBy);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    @Override
    public void onBackPressed() {
        CommentListFromApi.getInstance().removeAllDataFromList(prefUtils);
        Intent intent;
        if(prefUtils.getCommentIdForActivity().isEmpty()) {
            intent = new Intent(this, PostActivity.class);
        } else {
            intent = new Intent(this, ActivityInPostActivity.class);
        }
        prefUtils.saveCommentIdForActivity("");
        prefUtils.saveCurrentActivity("");
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
                    String commentId;
                    String userName = prefUtils.getAnswerToUserName();
                    if(commentEditText.getText().toString().length() >= userName.length()) {
                        String commentTo = commentEditText.getText().toString().substring(0, userName.length());
                        if (commentTo.equals(userName)) {
                            commentId = prefUtils.getCommentIdToAnswer();
                        } else {
                            commentId = "";
                        }
                    } else {
                        commentId = "";
                    }

                    sendCommentImageButton.setClickable(false);
                    new Thread(() ->
                            PostNewComment.postNewComment(this, prefUtils,
                            commentEditText.getText().toString(), commentId, commentEditText, sendCommentImageButton, findViewById(R.id.activity_comment_to_post))
                    ).start();

                } else {
                    GuestDialog dialog = new GuestDialog();
                    dialog.showDialog(this);
                }
                break;

        }
    }
}