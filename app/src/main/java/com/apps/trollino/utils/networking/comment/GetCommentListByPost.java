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
import com.apps.trollino.utils.data.Const;
import com.apps.trollino.utils.data.PrefUtils;
import com.apps.trollino.utils.networking_helper.ErrorMessageFromApi;
import com.apps.trollino.utils.networking_helper.ShimmerHide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.snackbar.Snackbar;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.apps.trollino.utils.data.Const.TAG_LOG;

public class GetCommentListByPost {
    private static int page;
    private static int totalPage;
    private static int totalCountComment;
    private static RecyclerView recyclerView;
    private static boolean isGetNewListThis;
    private static Context cont;
    private static PrefUtils prefUt;

    public static void getCommentListByPost(Context context, PrefUtils prefUtils,
                                            String postId, String sortBy,
                                            RecyclerView recycler, ShimmerFrameLayout shimmer, SwipyRefreshLayout refreshLayout,
                                            CommentToPostParentAdapter adapter, TextView noCommentTextView,
                                            TextView countTextView, boolean isGetNewList, ProgressBar progressBar) {
         String sortOrder = Const.SORT_ORDER_BY_DESC;

        recyclerView = recycler;
        cont = context;
        prefUt = prefUtils;
        isGetNewListThis = isGetNewList;
        page = isGetNewList ? 0 : prefUtils.getCurrentPage();
        if(isGetNewList) {
            CommentListFromApi.getInstance().removeAllDataFromList(prefUtils);
        }
        String cookie = prefUtils.getCookie();

        ApiService.getInstance(context).getCommentToPost(cookie, postId, sortBy, sortOrder, new Callback<CommentModel>() {
            @Override
            public void onResponse(Call<CommentModel> call, Response<CommentModel> response) {
                if (response.isSuccessful()) {
                    CommentModel commentModel = response.body();

                    PagerModel pagerModel = commentModel.getPagerModel();
                    totalPage = pagerModel.getTotalPages() - 1;
                    saveCurrentPage();
                    totalCountComment = pagerModel.getTotalItems();

                    List<CommentModel.Comments> commentList = commentModel.getCommentsList();
                    showCorrectVariant(commentList, adapter, shimmer, noCommentTextView, countTextView);
                } else {
                    String errorMessage = ErrorMessageFromApi.errorMessageFromApi(response.errorBody());
                    SnackBarMessageCustom.showSnackBar(recycler, errorMessage);
                }
                hideUpdateProgressView(shimmer, refreshLayout, progressBar);
            }

            @Override
            public void onFailure(Call<CommentModel> call, Throwable t) {
                t.getStackTrace();
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
                hideUpdateProgressView(shimmer, refreshLayout, progressBar);
                Log.d(TAG_LOG, "t.getLocalizedMessage() " + t.getLocalizedMessage());
            }

        });

    }

    private static void hideUpdateProgressView(ShimmerFrameLayout shimmer, SwipyRefreshLayout refreshLayout,
                                               ProgressBar progressBar) {
        if(shimmer != null) {
            shimmer.setVisibility(View.GONE);
        }
        if(refreshLayout != null) {
            refreshLayout.setRefreshing(false);
        }
        progressBar.setVisibility(View.GONE);
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
            if (shimmer != null) {
                ShimmerHide.shimmerHide(recyclerView, shimmer);
            }

        } else {
            recyclerView.setVisibility(View.GONE);
            if (shimmer != null) {
                ShimmerHide.shimmerHide(noCommentTextView, shimmer);
            }
        }

        countTextView.setText(String.valueOf(commentCount));
    }

    private static void saveCurrentPage() {
        if(page < totalPage) {
            prefUt.saveCurrentPage(page + 1);
        } else {
            prefUt.saveCurrentPage(totalPage);
        }
    }

    private static void updatePostListAndNotifyRecyclerAdapter(List<CommentModel.Comments> commentList, CommentToPostParentAdapter adapter) {
        int currentListSize = CommentListFromApi.getInstance().getCommentList().size();
        CommentListFromApi.getInstance().saveCommentInList(commentList);
        int newListSize = CommentListFromApi.getInstance().getCommentList().size();

        if(newListSize == currentListSize && page == totalPage && ! isGetNewListThis) {
            SnackBarMessageCustom.showSnackBar(recyclerView, cont.getString(R.string.msg_all_comments_showed));
        } else {
            adapter.notifyDataSetChanged();
            recyclerView.getLayoutManager().scrollToPosition(0);
            recyclerView.suppressLayout(false);
        }

        int currentAdapterPosition =  prefUt.getCurrentAdapterPositionComment();
        if(currentAdapterPosition > 0 && totalCountComment - 1 > currentAdapterPosition){
            prefUt.saveCurrentAdapterPositionComment(currentAdapterPosition + 1);
        }
    }
}
