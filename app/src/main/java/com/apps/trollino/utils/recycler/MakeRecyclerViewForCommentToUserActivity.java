package com.apps.trollino.utils.recycler;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.trollino.adapters.UserCommentAdapter;
import com.apps.trollino.data.model.comment.CommentModel;
import com.apps.trollino.ui.base.BaseActivity;
import com.apps.trollino.ui.main_group.CommentToPostActivity;
import com.apps.trollino.utils.RecyclerScrollListener;
import com.apps.trollino.utils.data.CommentListToUserActivityFromApi;
import com.apps.trollino.utils.data.PrefUtils;
import com.apps.trollino.utils.networking.user_action.GetCommentListToUserActivity;
import com.facebook.shimmer.ShimmerFrameLayout;

public class MakeRecyclerViewForCommentToUserActivity extends RecyclerView.OnScrollListener{
    private static Context cont;
    private static PrefUtils prefUt;

    public static void makeRecyclerViewForCommentToUserActivity(Context context, PrefUtils prefUtils, ProgressBar progressBar,
                                                                RecyclerView recyclerView, ShimmerFrameLayout shimmer,
                                                                View includeNoDataForUser, TextView noDataTextView, View bottomNavigation) {
        cont = context;
        prefUt = prefUtils;

        UserCommentAdapter adapter = new UserCommentAdapter((BaseActivity) context,
                CommentListToUserActivityFromApi.getInstance().getCommentList(), userCommentItemListener);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        if(CommentListToUserActivityFromApi.getInstance().getCommentList().isEmpty()) {
            infiniteScroll(progressBar, recyclerView, shimmer, adapter, bottomNavigation, includeNoDataForUser, noDataTextView, true);
        }

        recyclerView.addOnScrollListener(new RecyclerScrollListener() {
            @Override
            public void onScrolledToEnd() {
                infiniteScroll(progressBar, recyclerView, shimmer, adapter, bottomNavigation, includeNoDataForUser, noDataTextView, false);
            }

            @Override
            public void onScrolledToTop() {
                infiniteScroll(progressBar, recyclerView, shimmer, adapter, bottomNavigation, includeNoDataForUser, noDataTextView, true);
            }
        });
    }

    // Обработка нажатия на элемент списка
    private static final UserCommentAdapter.OnItemClick<CommentModel.Comments> userCommentItemListener = (item, position) -> {
        prefUt.saveCommentIdForActivity(item.getCommentId());
        prefUt.saveCurrentPostId(item.getPostId());
        prefUt.saveValuePostFromCategoryList(false);
        cont.startActivity(new Intent(cont, CommentToPostActivity.class));
    };


    // Загрузить/обновить данные с API
    private static void updateDataFromApi(ProgressBar progressBar,
                                          RecyclerView recyclerView, ShimmerFrameLayout shimmer,
                                          UserCommentAdapter adapter,
                                          View includeNoDataForUser, TextView noDataTextView, View bottomNavigation, boolean isGetNewList) {
        new Thread(() -> {
            GetCommentListToUserActivity.getCommentListToUserActivity(cont, prefUt, adapter, recyclerView, shimmer, isGetNewList, progressBar,
                    includeNoDataForUser, noDataTextView, bottomNavigation);
        }).start();
    }

    // Загрузить/обновить данные с API при скролах ресайклера вверх или вниз, если достигнут конец списка
    private static void infiniteScroll(ProgressBar progressBar,
                                       RecyclerView recyclerView, ShimmerFrameLayout shimmer,
                                       UserCommentAdapter adapter, View bottomNavigation,
                                       View includeNoDataForUser, TextView noDataTextView, boolean isGetNewList) {
        progressBar.setVisibility(isGetNewList ? View.GONE : View.VISIBLE);
        shimmer.setVisibility(isGetNewList ? View.VISIBLE : View.GONE);
        Handler handler = new Handler();
        handler.postDelayed(() -> updateDataFromApi(progressBar, recyclerView, shimmer, adapter, includeNoDataForUser, noDataTextView, bottomNavigation, isGetNewList), 1000);
    }
}
