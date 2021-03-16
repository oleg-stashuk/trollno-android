package com.apps.trollino.utils.recycler;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.trollino.adapters.CommentToPostParentAdapter;
import com.apps.trollino.ui.base.BaseActivity;
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
                                                  SwipyRefreshLayout refreshLayout, boolean isNewData,
                                                  String postId, EditText commentEditText,
                                                  TextView noCommentTextView, TextView countTextView, String sortBy) {

        cont = context;
        prefUt = prefUtils;

        CommentToPostParentAdapter adapter = new CommentToPostParentAdapter((BaseActivity) context,
                prefUtils, CommentListFromApi.getInstance().getCommentList(), commentEditText);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        infiniteScroll(recyclerView, shimmer, refreshLayout, adapter, postId, noCommentTextView,
                countTextView, sortBy, isNewData);
    }

    // Загрузить/обновить данные с API
    private static void updateDataFromApi(RecyclerView recyclerView, ShimmerFrameLayout shimmer, SwipyRefreshLayout refreshLayout,
                                          CommentToPostParentAdapter adapter, String postId,
                                          TextView noCommentTextView, TextView countTextView, String sortBy,
                                          boolean isGetNewList) {
        new Thread(() -> {
            GetCommentListByPost.getCommentListByPost(cont, prefUt, postId, sortBy,
                    recyclerView, shimmer, refreshLayout, adapter,
                    noCommentTextView, countTextView, isGetNewList);
        }).start();
    }

    // Загрузить/обновить данные с API при скролах ресайклера вверх или вниз, если достигнут конец списка
    private static void infiniteScroll(RecyclerView recyclerView, ShimmerFrameLayout shimmer, SwipyRefreshLayout refreshLayout,
                                       CommentToPostParentAdapter adapter, String postId,
                                       TextView noCommentTextView, TextView countTextView, String sortBy,
                                       boolean isGetNewList) {
        if(shimmer != null) {
            shimmer.setVisibility(isGetNewList ? View.VISIBLE : View.GONE);
        }
        Handler handler = new Handler();
        handler.postDelayed(() -> updateDataFromApi(recyclerView, shimmer, refreshLayout, adapter, postId,
                noCommentTextView, countTextView, sortBy, isGetNewList), 1000);
    }

}
