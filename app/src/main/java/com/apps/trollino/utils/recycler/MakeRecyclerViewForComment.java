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
import com.apps.trollino.utils.data.Const;
import com.apps.trollino.utils.RecyclerScrollListener;
import com.apps.trollino.utils.data.CommentListFromApi;
import com.apps.trollino.utils.data.PrefUtils;
import com.apps.trollino.utils.networking.comment.GetCommentListByPost;
import com.facebook.shimmer.ShimmerFrameLayout;

public class MakeRecyclerViewForComment extends RecyclerView.OnScrollListener{
    private static Context cont;
    private static PrefUtils prefUt;
    private static String sortOrder = Const.SORT_ORDER_BY_DESC;

    public static void makeRecyclerViewForComment(Context context, PrefUtils prefUtils,
                                                  RecyclerView recyclerView, ShimmerFrameLayout shimmer,
                                                  ProgressBar progressBar, String postId, EditText commentEditText,
                                                  TextView noCommentTextView, TextView countTextView, String sortBy) {

        cont = context;
        prefUt = prefUtils;

        CommentToPostParentAdapter adapter = new CommentToPostParentAdapter((BaseActivity) context,
                prefUtils, CommentListFromApi.getInstance().getCommentList(), commentEditText);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        progressBar.setVisibility(View.VISIBLE);
        if(CommentListFromApi.getInstance().getCommentList().isEmpty()) {
            infiniteScroll(progressBar, recyclerView, shimmer, adapter, postId, noCommentTextView,
                    countTextView, sortBy, true);
        }

        recyclerView.addOnScrollListener(new RecyclerScrollListener() {
            @Override
            public void onScrolledToEnd() {
                infiniteScroll(progressBar, recyclerView, shimmer, adapter, postId, noCommentTextView,
                        countTextView, sortBy, false);
            }

            @Override
            public void onScrolledToTop() {
                infiniteScroll(progressBar, recyclerView, shimmer, adapter, postId, noCommentTextView,
                        countTextView, sortBy, true);
            }
        });

    }

    // Загрузить/обновить данные с API
    private static void updateDataFromApi(ProgressBar progressBar, RecyclerView recyclerView, ShimmerFrameLayout shimmer,
                                          CommentToPostParentAdapter adapter, String postId,
                                          TextView noCommentTextView, TextView countTextView, String sortBy,
                                          boolean isGetNewList) {
        new Thread(() -> {
            GetCommentListByPost.getCommentListByPost(cont, prefUt, postId, sortBy, sortOrder,
                    recyclerView, shimmer, adapter,
                    noCommentTextView, countTextView, progressBar, isGetNewList);
        }).start();
    }

    // Загрузить/обновить данные с API при скролах ресайклера вверх или вниз, если достигнут конец списка
    private static void infiniteScroll(ProgressBar progressBar, RecyclerView recyclerView, ShimmerFrameLayout shimmer,
                                       CommentToPostParentAdapter adapter, String postId,
                                       TextView noCommentTextView, TextView countTextView, String sortBy,
                                       boolean isGetNewList) {
        progressBar.setVisibility(isGetNewList ? View.GONE : View.VISIBLE);
        shimmer.setVisibility(isGetNewList ? View.VISIBLE : View.GONE);
        Handler handler = new Handler();
        handler.postDelayed(() -> updateDataFromApi(progressBar, recyclerView, shimmer, adapter, postId,
                noCommentTextView, countTextView, sortBy, isGetNewList), 1000);
    }

}
