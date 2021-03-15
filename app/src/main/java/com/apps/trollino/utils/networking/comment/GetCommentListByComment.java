package com.apps.trollino.utils.networking.comment;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.trollino.R;
import com.apps.trollino.adapters.CommentToPostChildAdapter;
import com.apps.trollino.data.model.comment.CommentModel;
import com.apps.trollino.data.networking.ApiService;
import com.apps.trollino.ui.base.BaseActivity;
import com.apps.trollino.utils.SnackBarMessageCustom;
import com.apps.trollino.utils.data.PrefUtils;
import com.apps.trollino.utils.networking_helper.ErrorMessageFromApi;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.apps.trollino.utils.data.Const.TAG_LOG;

public class GetCommentListByComment {
    private static Context cont;
    private static PrefUtils prefUt;

    public static void getCommentListByComment(Context context, PrefUtils prefUtils, String parentId,
                                               RecyclerView childCommentRecyclerView,
                                               EditText commentEditText, View view) {
        cont = context;
        prefUt = prefUtils;
        String cookie = prefUtils.getCookie();

        ApiService.getInstance(context).getCommentListByComment(cookie, parentId, new Callback<CommentModel>() {
            @Override
            public void onResponse(Call<CommentModel> call, Response<CommentModel> response) {
                if (response.isSuccessful()) {
                    makeChildCommentRecyclerView(childCommentRecyclerView, response.body().getCommentsList(), commentEditText);
                    childCommentRecyclerView.setVisibility(View.VISIBLE);
                } else {
                    String errorMessage = ErrorMessageFromApi.errorMessageFromApi(response.errorBody());
                    SnackBarMessageCustom.showSnackBar(view, errorMessage);
                }
            }

            @Override
            public void onFailure(Call<CommentModel> call, Throwable t) {
                t.getStackTrace();
                boolean isHaveNotInternet = t.getLocalizedMessage().contains(context.getString(R.string.internet_error_from_api));
                String noInternetMessage = context.getResources().getString(R.string.internet_error_message);
                if (isHaveNotInternet) {
                    Snackbar
                            .make(view, noInternetMessage, Snackbar.LENGTH_INDEFINITE)
                            .setMaxInlineActionWidth(3)
                            .setAction(R.string.refresh_button, v -> {
                                call.clone().enqueue(this);
                            })
                            .show();
                } else {
                    SnackBarMessageCustom.showSnackBar(view, t.getLocalizedMessage());
                }
                Log.d(TAG_LOG, "t.getLocalizedMessage() " + t.getLocalizedMessage());
            }

        });
    }

    // Создание childCommentRecyclerView
    private static void makeChildCommentRecyclerView(RecyclerView childCommentRecyclerView, List<CommentModel.Comments> commentList, EditText commentEditText) {
        childCommentRecyclerView.setLayoutManager(new LinearLayoutManager(cont));
        childCommentRecyclerView.setAdapter( new CommentToPostChildAdapter((BaseActivity) cont, prefUt, commentList, commentEditText));
    }
}
