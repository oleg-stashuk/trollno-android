package com.apps.trollino.utils.recycler;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;
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

public class MakeRecyclerViewForCommentToUserActivity extends RecyclerView.OnScrollListener{
    private static Context cont;
    private static PrefUtils prefUt;

    public static void makeRecyclerViewForCommentToUserActivity(Context context, PrefUtils prefUtils, RecyclerView recyclerView,
                                                                View includeNoDataForUser, TextView noDataTextView, View view) {
        cont = context;
        prefUt = prefUtils;

        UserCommentAdapter adapter = new UserCommentAdapter((BaseActivity) context,
                CommentListToUserActivityFromApi.getInstance().getCommentList(), userCommentItemListener);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        new Thread(() -> {
            GetCommentListToUserActivity.getCommentListToUserActivity(context, prefUtils, adapter, includeNoDataForUser, noDataTextView, view);
        }).start();

        recyclerView.addOnScrollListener(new RecyclerScrollListener() {
            @Override
            public void onScrolledToEnd() {
                Handler handler = new Handler();
                handler.postDelayed(() -> new Thread(() -> {
                    GetCommentListToUserActivity.getCommentListToUserActivity(context, prefUtils, adapter, includeNoDataForUser, noDataTextView, view);
                }).start(), 1000);
            }

            @Override
            public void onScrolledToTop() {
                Log.d("OkHttp", "!!!!!!!!!!!!!!!!!!!!!! onScrolledToTop");
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
}
