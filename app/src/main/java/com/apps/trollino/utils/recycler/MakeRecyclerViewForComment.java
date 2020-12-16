package com.apps.trollino.utils.recycler;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.trollino.adapters.CommentToPostParentAdapter;
import com.apps.trollino.ui.base.BaseActivity;
import com.apps.trollino.utils.RecyclerScrollListener;
import com.apps.trollino.utils.data.CommentListFromApi;
import com.apps.trollino.utils.data.PrefUtils;
import com.apps.trollino.utils.networking.comment.GetCommentListByPost;

public class MakeRecyclerViewForComment extends RecyclerView.OnScrollListener{

    public static void makeRecyclerViewForComment(Context context, PrefUtils prefUtils, RecyclerView recyclerView,
                                                  ProgressBar progressBar, String postId, EditText commentEditText,
                                                  TextView noCommentTextView, TextView countTextView) {

        CommentToPostParentAdapter adapter = new CommentToPostParentAdapter((BaseActivity) context, CommentListFromApi.getInstance().getCommentList(), commentEditText);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        progressBar.setVisibility(View.VISIBLE);

        new Thread(() -> {
            GetCommentListByPost.getCommentListByPost(context, prefUtils, postId, recyclerView, adapter, commentEditText,
                    noCommentTextView, countTextView, progressBar);
        }).start();

        recyclerView.addOnScrollListener(new RecyclerScrollListener() {
            @Override
            public void onScrolledToEnd() {
                progressBar.setVisibility(View.VISIBLE);
                Handler handler = new Handler();
                handler.postDelayed(() -> new Thread(() -> {
                    GetCommentListByPost.getCommentListByPost(context, prefUtils, postId, recyclerView, adapter, commentEditText,
                            noCommentTextView, countTextView, progressBar);
                }).start(), 1000);
            }
        });
    }
}
