package com.apps.trollino.utils.networking.comment;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.apps.trollino.R;
import com.apps.trollino.adapters.CommentToPostParentAdapter;
import com.apps.trollino.data.model.PagerModel;
import com.apps.trollino.data.model.comment.CommentModel;
import com.apps.trollino.data.networking.ApiService;
import com.apps.trollino.utils.SnackBarMessageCustom;
import com.apps.trollino.utils.data.CommentListFromApi;
import com.apps.trollino.utils.data.PrefUtils;
import com.apps.trollino.utils.networking_helper.ErrorMessageFromApi;
import com.apps.trollino.utils.networking_helper.ShimmerHide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.apps.trollino.utils.data.Const.COUNT_TRY_REQUEST;
import static com.apps.trollino.utils.data.Const.TAG_LOG;

public class GetCommentListByPost {
    private static int page;
    private static int totalPage;
    private static RecyclerView recyclerView;
    private static boolean isGetNewListThis;

    public static void getCommentListByPost(Context context, PrefUtils prefUtils,
                                            String postId, String sortBy, String sortOrder,
                                            RecyclerView recycler, ShimmerFrameLayout shimmer,
                                            CommentToPostParentAdapter adapter, TextView noCommentTextView,
                                            TextView countTextView, ProgressBar progressBar, boolean isGetNewList) {

        recyclerView = recycler;
        isGetNewListThis = isGetNewList;
        page = isGetNewList ? 0 : prefUtils.getCurrentPage();
        if(isGetNewList) {
            CommentListFromApi.getInstance().removeAllDataFromList(prefUtils);
        }
        String cookie = prefUtils.getCookie();

        ApiService.getInstance(context).getCommentToPost(cookie, postId, sortBy, sortOrder, new Callback<CommentModel>() {
            int countTry = 0;

            @Override
            public void onResponse(Call<CommentModel> call, Response<CommentModel> response) {
                if (response.isSuccessful()) {
                    CommentModel commentModel = response.body();

                    PagerModel pagerModel = commentModel.getPagerModel();
                    totalPage = pagerModel.getTotalItems() - 1;
                    saveCurrentPage(prefUtils);

                    List<CommentModel.Comments> commentList = commentModel.getCommentsList();
                    showCorrectVariant(commentList, adapter, shimmer, noCommentTextView, countTextView);
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
                    Log.d(TAG_LOG, "t.getLocalizedMessage() " + t.getLocalizedMessage());
                }
            }
        });

    }

    // Если для Поста нет комментариев, то выводится на экран сообщение что комментариев нет
    private static void showCorrectVariant(List<CommentModel.Comments> commentList,
                                           CommentToPostParentAdapter adapter, ShimmerFrameLayout shimmer,
                                           TextView noCommentTextView, TextView countTextView) {
        int commentCount = commentList.size();

        if(commentCount > 0) {
            noCommentTextView.setVisibility(View.GONE);

            for(CommentModel.Comments comment : commentList) {
                commentCount += Integer.parseInt(comment.getCommentAnswersCount());
            }
            updatePostListAndNotifyRecyclerAdapter(commentList, adapter);
            ShimmerHide.shimmerHide(recyclerView, shimmer);
        } else {
            recyclerView.setVisibility(View.GONE);
            ShimmerHide.shimmerHide(noCommentTextView, shimmer);
        }

        countTextView.setText(String.valueOf(commentCount));
    }

    private static void saveCurrentPage(PrefUtils prefUtils) {
        if(page < totalPage) {
            prefUtils.saveCurrentPage(page + 1);
        } else {
            prefUtils.saveCurrentPage(totalPage);
        }
    }

    private static void updatePostListAndNotifyRecyclerAdapter(List<CommentModel.Comments> commentList, CommentToPostParentAdapter adapter) {
        int currentListSize = CommentListFromApi.getInstance().getCommentList().size();
        CommentListFromApi.getInstance().saveCommentInList(commentList);
        int newListSize = CommentListFromApi.getInstance().getCommentList().size();

        if(newListSize == currentListSize && page == totalPage && ! isGetNewListThis) {
            SnackBarMessageCustom.showSnackBar(recyclerView, "Показаны все комментарии к этому посту");
        }

        adapter.notifyDataSetChanged();
    }
}
