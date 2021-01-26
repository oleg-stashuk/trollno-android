package com.apps.trollino.utils.networking.user_action;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.apps.trollino.R;
import com.apps.trollino.adapters.UserCommentAdapter;
import com.apps.trollino.data.model.comment.CommentModel;
import com.apps.trollino.data.networking.ApiService;
import com.apps.trollino.utils.SnackBarMessageCustom;
import com.apps.trollino.utils.data.CommentListToUserActivityFromApi;
import com.apps.trollino.utils.data.PrefUtils;
import com.apps.trollino.utils.networking_helper.ErrorMessageFromApi;
import com.apps.trollino.utils.networking_helper.ShimmerHide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.apps.trollino.utils.Const.COUNT_TRY_REQUEST;
import static com.apps.trollino.utils.Const.LOG_TAG;

public class GetCommentListToUserActivity {
    private static int page;
    private static int totalPage;
    private static boolean isGetNewListThis;
    private static RecyclerView recyclerView;

    public static void getCommentListToUserActivity(Context context, PrefUtils prefUtils, UserCommentAdapter adapter,
                                                    RecyclerView recycler, ShimmerFrameLayout shimmer, boolean isGetNewList,
                                                    ProgressBar progressBar, View includeNoDataForUser, TextView noDataTextView) {
        String cookie = prefUtils.getCookie();
        String userId = prefUtils.getUserUid();
        page = isGetNewList ? 0 : prefUtils.getNewPostCurrentPage();
        isGetNewListThis = isGetNewList;
        recyclerView = recycler;

        ApiService.getInstance(context).getCommentListToUserActivity(cookie, userId, page, new Callback<CommentModel>() {
            int countTry = 0;

            @Override
            public void onResponse(Call<CommentModel> call, Response<CommentModel> response) {
                if(response.isSuccessful()) {
                    CommentModel commentModel = response.body();
                    List<CommentModel.Comments> commentList = commentModel.getCommentsList();
                    totalPage = commentModel.getPagerModel().getTotalPages() - 1;

                    if (commentList.size() == 0 || commentList.isEmpty()) {
                        includeNoDataForUser.setVisibility(View.VISIBLE);
                        noDataTextView.setText(context.getResources().getString(R.string.txt_have_no_comments));
                    } else {
                        includeNoDataForUser.setVisibility(View.GONE);
                        updatePostListAndNotifyRecyclerAdapter(commentList, adapter);
                        saveCurrentPage(prefUtils);
                        ShimmerHide.shimmerHide(recycler, shimmer);
                    }
                } else {
                    String errorMessage = ErrorMessageFromApi.errorMessageFromApi(response.errorBody());
                    SnackBarMessageCustom.showSnackBar(recycler, errorMessage);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<CommentModel> call, Throwable t) {
                t.getStackTrace();
                if (countTry <= COUNT_TRY_REQUEST) {
                    call.clone().enqueue(this);
                    countTry++;
                } else {
                    boolean isHaveNotInternet = t.getLocalizedMessage().contains(context.getString(R.string.internet_error_from_api));
                    String noInternetMessage = context.getResources().getString(R.string.internet_error_message);
                    if (isHaveNotInternet) {
                        Snackbar
                                .make(recycler, noInternetMessage, Snackbar.LENGTH_INDEFINITE)
                                .setMaxInlineActionWidth(3)
                                .setAction(R.string.refresh_button, v -> {
                                    call.clone().enqueue(this);
                                })
                                .show();
                    } else {
                        SnackBarMessageCustom.showSnackBar(recycler, t.getLocalizedMessage());
                    }
                    progressBar.setVisibility(View.GONE);
                    Log.d(LOG_TAG, "t.getLocalizedMessage() " + t.getLocalizedMessage());
                }
            }
        });
    }

    private static void saveCurrentPage(PrefUtils prefUtils) {
        if(page < totalPage) {
            prefUtils.saveNewPostCurrentPage(page + 1);
        } else {
            prefUtils.saveNewPostCurrentPage(totalPage);
        }
    }

    private static void updatePostListAndNotifyRecyclerAdapter(List<CommentModel.Comments> comments, UserCommentAdapter adapter) {
        int currentListSize = CommentListToUserActivityFromApi.getInstance().getCommentList().size();
        CommentListToUserActivityFromApi.getInstance().saveCommentInList(comments);
        int newListSize = CommentListToUserActivityFromApi.getInstance().getCommentList().size();
        if(newListSize == currentListSize && page == totalPage && ! isGetNewListThis) {
            SnackBarMessageCustom.showSnackBar(recyclerView, "Ответов больше нет");
        }
        adapter.notifyDataSetChanged();
    }
}
