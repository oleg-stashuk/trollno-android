package com.apps.trollino.utils.networking.comment;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.apps.trollino.utils.Const.COUNT_TRY_REQUEST;

public class GetCommentListByPost {
    private static int page;

    public static void getCommentListByPost(Context context, PrefUtils prefUtils,
                                            String postId, String sortBy, String sortOrder,
                                            RecyclerView recyclerView, CommentToPostParentAdapter adapter,
                                            EditText commentEditText, TextView noCommentTextView,
                                            TextView countTextView, ProgressBar progressBar) {

        page = prefUtils.getNewPostCurrentPage();
        String cookie = prefUtils.getCookie();

        ApiService.getInstance(context).getCommentToPost(cookie, postId, sortBy, sortOrder, new Callback<CommentModel>() {
            int countTry = 0;

            @Override
            public void onResponse(Call<CommentModel> call, Response<CommentModel> response) {
                if (response.isSuccessful()) {
                    CommentModel commentModel = response.body();

                    PagerModel pagerModel = commentModel.getPagerModel();
                    saveCurrentPage(pagerModel.getTotalItems(), prefUtils);

                    List<CommentModel.Comments> commentList = commentModel.getCommentsList();
                    showCorrectVariant(commentList, recyclerView, adapter, commentEditText, noCommentTextView, countTextView);
                } else {
                    String errorMessage = ErrorMessageFromApi.errorMessageFromApi(response.errorBody());
                    SnackBarMessageCustom.showSnackBar(progressBar, errorMessage);
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
                                .make(progressBar, noInternetMessage, Snackbar.LENGTH_INDEFINITE)
                                .setMaxInlineActionWidth(3)
                                .setAction(R.string.refresh_button, v -> {
                                    call.clone().enqueue(this);
                                })
                                .show();
                    } else {
                        SnackBarMessageCustom.showSnackBar(progressBar, t.getLocalizedMessage());
                    }
                    progressBar.setVisibility(View.GONE);
                    Log.d("OkHttp", "t.getLocalizedMessage() " + t.getLocalizedMessage());
                }
            }
        });

    }

    // Если для Поста нет комментариев, то выводится на экран сообщение что комментариев нет
    private static void showCorrectVariant(List<CommentModel.Comments> commentList, RecyclerView recyclerView,
                                           CommentToPostParentAdapter adapter, EditText commentEditText,
                                           TextView noCommentTextView, TextView countTextView) {
        int commentCount = commentList.size();

        if(commentCount > 0) {
            noCommentTextView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            for(CommentModel.Comments comment : commentList) {
                commentCount += Integer.parseInt(comment.getCommentAnswersCount());
            }
            updatePostListAndNotifyRecyclerAdapter(commentList, adapter);

        } else {
            noCommentTextView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }

        countTextView.setText(String.valueOf(commentCount));
    }

    private static void saveCurrentPage(int totalPage, PrefUtils prefUtils) {
        if(page < totalPage - 1) {
            prefUtils.saveNewPostCurrentPage(page + 1);
        } else {
            prefUtils.saveNewPostCurrentPage(totalPage - 1);
        }
    }

    private static void updatePostListAndNotifyRecyclerAdapter(List<CommentModel.Comments> commentList, CommentToPostParentAdapter adapter) {
        CommentListFromApi.getInstance().saveCommentInList(commentList);
        adapter.notifyDataSetChanged();
    }
}
