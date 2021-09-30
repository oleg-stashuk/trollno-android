package com.app.trollno.utils.recycler;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.trollno.adapters.AnswersAdapter;
import com.app.trollno.data.model.user_action.AnswersModel;
import com.app.trollno.ui.base.BaseActivity;
import com.app.trollno.ui.main_group.CommentToPostActivity;
import com.app.trollno.utils.RecyclerScrollListener;
import com.app.trollno.utils.data.AnswersFromApi;
import com.app.trollno.utils.data.PrefUtils;
import com.app.trollno.utils.networking.user_action.GetAnswersActivity;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;

public class MakeRecyclerViewForAnswerActivity {
    private static Context cont;
    private static PrefUtils prefUt;

    public static void makeRecyclerViewForCommentToUserActivity(Context context, PrefUtils prefUtils, RecyclerView recyclerView,
                                                                ShimmerFrameLayout shimmer, SwipyRefreshLayout refreshLayout,
                                                                View includeNoDataForUser, TextView noDataTextView,
                                                                View bottomNavigation, ProgressBar progressBar) {
        cont = context;
        prefUt = prefUtils;

        AnswersAdapter adapter = new AnswersAdapter((BaseActivity) context, prefUtils,
                AnswersFromApi.getInstance().getAnswerList(), answerItemListener);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        prefUtils.saveCurrentAdapterPositionAnswers(0);
        recyclerView.getLayoutManager().scrollToPosition(0);

        infiniteScroll(recyclerView, shimmer, refreshLayout, adapter, bottomNavigation,
                includeNoDataForUser, noDataTextView, true, progressBar);

        recyclerView.addOnScrollListener(new RecyclerScrollListener() {
            @Override
            public void onScrolledToEnd() {
                recyclerView.getLayoutManager().scrollToPosition(prefUtils.getCurrentAdapterPositionAnswers());
                progressBar.setVisibility(View.VISIBLE);
                infiniteScroll(recyclerView, shimmer, refreshLayout, adapter, bottomNavigation,
                        includeNoDataForUser, noDataTextView, false, progressBar);
            }
        });
    }

    // Обработка нажатия на элемент списка
    private static final AnswersAdapter.OnItemClick<AnswersModel.Answers> answerItemListener = (item, position) -> {
        prefUt.saveCommentIdForActivity(item.getCommentId());
        prefUt.saveCurrentPostId(item.getPostId());
        prefUt.saveValuePostFromCategoryList(false);
        cont.startActivity(new Intent(cont, CommentToPostActivity.class));
    };


    // Загрузить/обновить данные с API
    private static void updateDataFromApi(RecyclerView recyclerView, ShimmerFrameLayout shimmer,
                                          SwipyRefreshLayout refreshLayout, AnswersAdapter adapter,
                                          View includeNoDataForUser, TextView noDataTextView,
                                          View bottomNavigation, boolean isGetNewList, ProgressBar progressBar) {
        new Thread(() -> {
            GetAnswersActivity.getAnswersActivity(cont, prefUt, adapter, recyclerView, shimmer, refreshLayout, isGetNewList,
                    includeNoDataForUser, noDataTextView, bottomNavigation, progressBar);
        }).start();
    }

    // Загрузить/обновить данные с API при скролах ресайклера вверх или вниз, если достигнут конец списка
    private static void infiniteScroll(RecyclerView recyclerView, ShimmerFrameLayout shimmer, SwipyRefreshLayout refreshLayout,
                                       AnswersAdapter adapter, View bottomNavigation,
                                       View includeNoDataForUser, TextView noDataTextView,
                                       boolean isGetNewList, ProgressBar progressBar) {
        if (shimmer != null) {
            shimmer.setVisibility(isGetNewList ? View.VISIBLE : View.GONE);
        }
        Handler handler = new Handler();
        handler.postDelayed(() -> updateDataFromApi(recyclerView, shimmer, refreshLayout, adapter,
                includeNoDataForUser, noDataTextView, bottomNavigation, isGetNewList, progressBar), 1000);
    }
}
