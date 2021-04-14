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
import com.facebook.shimmer.ShimmerFrameLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;

public class MakeRecyclerViewForComment {
    private static Context cont;
    private static PrefUtils prefUt;

    public static void makeRecyclerViewForComment(Context context, PrefUtils prefUtils,
                                                  RecyclerView recyclerView, ShimmerFrameLayout shimmer,
                                                  SwipyRefreshLayout refreshLayout,
                                                  String postId, EditText commentEditText,
                                                  TextView noCommentTextView, TextView countTextView,
                                                  String sortBy, ProgressBar progressBar) {

        cont = context;
        prefUt = prefUtils;

        CommentToPostParentAdapter adapter = new CommentToPostParentAdapter((BaseActivity) context,
                prefUtils, CommentListFromApi.getInstance().getCommentList(), commentEditText);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        prefUtils.saveCurrentAdapterPositionComment(0);
        recyclerView.getLayoutManager().scrollToPosition(0);

        infiniteScroll(recyclerView, shimmer, refreshLayout, adapter, postId, noCommentTextView,
                countTextView, sortBy, true, progressBar);

        recyclerView.addOnScrollListener(new RecyclerScrollListener() {
            @Override
            public void onScrolledToEnd() {
                recyclerView.getLayoutManager().scrollToPosition(prefUtils.getCurrentAdapterPositionComment());
                progressBar.setVisibility(View.VISIBLE);
                infiniteScroll(recyclerView, shimmer, refreshLayout, adapter, postId, noCommentTextView,
                        countTextView, sortBy, false, progressBar);
            }
        });
    }

    // Загрузить/обновить данные с API
    private static void updateDataFromApi(RecyclerView recyclerView, ShimmerFrameLayout shimmer, SwipyRefreshLayout refreshLayout,
                                          CommentToPostParentAdapter adapter, String postId,
                                          TextView noCommentTextView, TextView countTextView, String sortBy,
                                          boolean isGetNewList, ProgressBar progressBar) {
        new Thread(() -> {
            GetCommentListByPost.getCommentListByPost(cont, prefUt, postId, sortBy,
                    recyclerView, shimmer, refreshLayout, adapter,
                    noCommentTextView, countTextView, isGetNewList, progressBar);
        }).start();
    }

    // Загрузить/обновить данные с API при скролах ресайклера вверх или вниз, если достигнут конец списка
    private static void infiniteScroll(RecyclerView recyclerView, ShimmerFrameLayout shimmer, SwipyRefreshLayout refreshLayout,
                                       CommentToPostParentAdapter adapter, String postId,
                                       TextView noCommentTextView, TextView countTextView, String sortBy,
                                       boolean isGetNewList, ProgressBar progressBar) {
        if(shimmer != null) {
            shimmer.setVisibility(isGetNewList ? View.VISIBLE : View.GONE);
        }
        Handler handler = new Handler();
        handler.postDelayed(() -> updateDataFromApi(recyclerView, shimmer, refreshLayout, adapter, postId,
                noCommentTextView, countTextView, sortBy, isGetNewList, progressBar), 1000);
    }

}
