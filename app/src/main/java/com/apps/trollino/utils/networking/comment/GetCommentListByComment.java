package com.apps.trollino.utils.networking.comment;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.trollino.adapters.CommentToPostChildAdapter;
import com.apps.trollino.data.model.comment.CommentModel;
import com.apps.trollino.data.networking.ApiService;
import com.apps.trollino.ui.base.BaseActivity;
import com.apps.trollino.utils.data.PrefUtils;
import com.apps.trollino.utils.networking_helper.ErrorMessageFromApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.apps.trollino.utils.Const.COUNT_TRY_REQUEST;

public class GetCommentListByComment {
    private static Context cont;
    private static PrefUtils prefUt;

    public static void getCommentListByComment(Context context, PrefUtils prefUtils, String parentId,
                                               RecyclerView childCommentRecyclerView,
                                               TextView showMoreTextView, EditText commentEditText) {
        cont = context;
        prefUt = prefUtils;
        String cookie = prefUtils.getCookie();

        ApiService.getInstance(context).getCommentListByComment(cookie, parentId, new Callback<CommentModel>() {
            int countTry = 0;

            @Override
            public void onResponse(Call<CommentModel> call, Response<CommentModel> response) {
                if (response.isSuccessful()) {
                    checkAnswerToThisComment(response.body().getCommentsList(), childCommentRecyclerView, showMoreTextView, commentEditText);
                } else {
                    String errorMessage = ErrorMessageFromApi.errorMessageFromApi(response.errorBody());
                    showToast(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<CommentModel> call, Throwable t) {
                t.getStackTrace();
                if (countTry <= COUNT_TRY_REQUEST) {
                    call.clone().enqueue(this);
                    countTry++;
                } else {
                    showToast(t.getLocalizedMessage());
                    Log.d("OkHttp", "t.getLocalizedMessage() " + t.getLocalizedMessage());
                }
            }
        });
    }

    private static void showToast(String message) {
        Toast.makeText(cont, message, Toast.LENGTH_SHORT).show();
    }

    private static void checkAnswerToThisComment(List<CommentModel.Comments> commentsListToPost, final RecyclerView childCommentRecyclerView, final TextView showMoreTextView, EditText commentEditText) {
        if(commentsListToPost.size() > 0)  {
            childCommentRecyclerView.setVisibility(View.VISIBLE);

            // Если в дочернем списке ответов к коментарию больше 3 элементов, отображать кнопку "Показать все ответы"
            if(commentsListToPost.size() > 3) {
                showMoreTextView.setVisibility(View.VISIBLE);

                final List<CommentModel.Comments> commentsListSize2 = new ArrayList<>();
                commentsListSize2.add(commentsListToPost.get(0));
                commentsListSize2.add(commentsListToPost.get(1));
                makeChildCommentRecyclerView(childCommentRecyclerView, commentsListSize2, commentEditText);

                showMoreTextView.setOnClickListener(v -> {
                    makeChildCommentRecyclerView(childCommentRecyclerView, commentsListToPost, commentEditText);
                    showMoreTextView.setVisibility(View.GONE);
                });
            } else {
                makeChildCommentRecyclerView(childCommentRecyclerView, commentsListToPost, commentEditText);
                showMoreTextView.setVisibility(View.GONE);
            }

        } else {
            childCommentRecyclerView.setVisibility(View.GONE);
            showMoreTextView.setVisibility(View.GONE);
        }
    }

    // Создание childCommentRecyclerView
    private static void makeChildCommentRecyclerView(RecyclerView childCommentRecyclerView, List<CommentModel.Comments> commentList, EditText commentEditText) {
        childCommentRecyclerView.setLayoutManager(new LinearLayoutManager(cont));
        childCommentRecyclerView.setAdapter( new CommentToPostChildAdapter((BaseActivity) cont, prefUt, commentList, commentEditText));
    }
}
