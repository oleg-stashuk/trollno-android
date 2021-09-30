package com.app.trollno.utils.networking.comment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.app.trollno.R;
import com.app.trollno.data.model.comment.CreateCommentBody;
import com.app.trollno.data.model.comment.CreateNewCommentRequest;
import com.app.trollno.data.networking.ApiService;
import com.app.trollno.ui.main_group.CommentToPostActivity;
import com.app.trollno.utils.SnackBarMessageCustom;
import com.app.trollno.utils.data.PrefUtils;
import com.app.trollno.utils.dialogs.GuestDialog;
import com.app.trollno.utils.networking_helper.ErrorMessageFromApi;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.trollno.utils.data.Const.TAG_LOG;

public class PostNewComment {

    public static void postNewComment(Context context, PrefUtils prefUtils, String comment, String parentId,
                                      EditText commentEditText, ImageButton sendCommentImageButton, View view) {

        if(parentId.isEmpty()) {
            parentId = null;
        }
        String cookie = prefUtils.getCookie();
        String token = prefUtils.getToken();
        String postId = prefUtils.getCurrentPostId();

        List<CreateNewCommentRequest.PostId> postIdList = new ArrayList<>();
        postIdList.add(new CreateNewCommentRequest.PostId(postId));

        List<CreateCommentBody> commentList = new ArrayList<>();
        commentList.add(new CreateCommentBody(comment));

        List<CreateNewCommentRequest.ParentIdComment> parentIdList = new ArrayList<>();
        parentIdList.add(new CreateNewCommentRequest.ParentIdComment(parentId));

        ApiService.getInstance(context).postNewCommentToPost(cookie, token, postIdList, commentList, parentIdList, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    SnackBarMessageCustom.showSnackBar(view, context.getResources().getString(R.string.txt_comment_sent_succesful));

                    Intent intent = new Intent(context, CommentToPostActivity.class);
                    ((Activity) context).finish();
                    context.startActivity(intent);

                    commentEditText.setText("");
                    prefUtils.saveAnswerToUserName("");
                    prefUtils.saveCommentIdToAnswer("");

                    commentEditText.setText("");
                } else if(response.code() == 403) {
                    GuestDialog dialog = new GuestDialog();
                    dialog.showDialog(context);
                } else {
                    String errorMessage = ErrorMessageFromApi.errorMessageFromApi(response.errorBody());
                    SnackBarMessageCustom.showSnackBar(view, errorMessage);
                }

                makeButtonClickable(sendCommentImageButton);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
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
                makeButtonClickable(sendCommentImageButton);
                Log.d(TAG_LOG, "t.getLocalizedMessage() " + t.getLocalizedMessage());
            }
        });
    }

    private static void makeButtonClickable(ImageButton imageButton) {
        imageButton.setClickable(true);
        imageButton.setEnabled(true);
    }
}
