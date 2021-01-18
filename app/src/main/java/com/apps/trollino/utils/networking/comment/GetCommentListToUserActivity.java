package com.apps.trollino.utils.networking.comment;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.trollino.R;
import com.apps.trollino.adapters.UserCommentAdapter;
import com.apps.trollino.data.model.comment.CommentModel;
import com.apps.trollino.data.networking.ApiService;
import com.apps.trollino.utils.data.CommentListToUserActivityFromApi;
import com.apps.trollino.utils.data.PrefUtils;
import com.apps.trollino.utils.networking_helper.ErrorMessageFromApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.apps.trollino.utils.Const.COUNT_TRY_REQUEST;

public class GetCommentListToUserActivity {
    private static Context cont;
    private static int page;

    public static void getCommentListToUserActivity(Context context, PrefUtils prefUtils, UserCommentAdapter adapter, View includeNoDataForUser, TextView noDataTextView) {
        cont = context;
        String cookie = prefUtils.getCookie();
        String userId = prefUtils.getUserUid();
        page = prefUtils.getCurrentPage();

        ApiService.getInstance(context).getCommentListToUserActivity(cookie, userId, new Callback<CommentModel>() {
            int countTry = 0;

            @Override
            public void onResponse(Call<CommentModel> call, Response<CommentModel> response) {
                if(response.isSuccessful()) {
                    CommentModel commentModel = response.body();
                    List<CommentModel.Comments> commentList = commentModel.getCommentsList();

                    if (commentList.size() == 0 || commentList.isEmpty()) {
                        includeNoDataForUser.setVisibility(View.VISIBLE);
                        noDataTextView.setText(context.getResources().getString(R.string.txt_have_no_comments));
                    } else {
                        includeNoDataForUser.setVisibility(View.GONE);
                        updatePostListAndNotifyRecyclerAdapter(commentList, adapter);
                        saveCurrentPage(commentModel.getPagerModel().getTotalPages(), prefUtils);
                    }
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
                    Log.d("OkHttp", "!!!!!!!!!!! t.getLocalizedMessage() " + ErrorMessageFromApi.messageNoInternet(t));
                    Log.d("OkHttp", "t.getLocalizedMessage() " + t.getLocalizedMessage());
                }
            }
        });
    }

    private static void showToast(String message) {
        Toast.makeText(cont, message, Toast.LENGTH_SHORT).show();
    }

    private static void saveCurrentPage(int totalPage, PrefUtils prefUtils) {
        if(page < totalPage - 1) {
            prefUtils.saveNewPostCurrentPage(page + 1);
        } else {
            prefUtils.saveNewPostCurrentPage(totalPage - 1);
        }
    }

    private static void updatePostListAndNotifyRecyclerAdapter(List<CommentModel.Comments> comments, UserCommentAdapter adapter) {
        CommentListToUserActivityFromApi.getInstance().saveCommentInList(comments);
        adapter.notifyDataSetChanged();
    }
}
