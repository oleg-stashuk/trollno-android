package com.apps.trollino.utils.networking.comment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.apps.trollino.data.model.comment.CreateCommentBody;
import com.apps.trollino.data.model.comment.CreateNewCommentRequest;
import com.apps.trollino.data.model.comment.CreateNewCommentResponse;
import com.apps.trollino.data.networking.ApiService;
import com.apps.trollino.ui.main_group.CommentToPostActivity;
import com.apps.trollino.utils.data.PrefUtils;
import com.apps.trollino.utils.networking_helper.ErrorMessageFromApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.apps.trollino.utils.Const.COUNT_TRY_REQUEST;

public class PostNewComment {
    private static Context cont;

    public static void postNewComment(Context context, PrefUtils prefUtils, String comment, String parentId,
                                      EditText commentEditText) {
        cont = context;
        String cookie = prefUtils.getCookie();
        String token = prefUtils.getToken();
        String postId = prefUtils.getCurrentPostId();

        List<CreateNewCommentRequest.PostId> postIdList = new ArrayList<>();
        postIdList.add(new CreateNewCommentRequest.PostId(postId));

        List<CreateCommentBody> commentList = new ArrayList<>();
        commentList.add(new CreateCommentBody(comment));

        List<CreateNewCommentRequest.ParentIdComment> parentIdList = new ArrayList<>();
        parentIdList.add(new CreateNewCommentRequest.ParentIdComment(parentId));

        ApiService.getInstance(context).postNewCommentToPost(cookie, token, postIdList, commentList, parentIdList,
                new Callback<CreateNewCommentResponse>() {

                    int countTry = 0;

                    @Override
                    public void onResponse(Call<CreateNewCommentResponse> call, Response<CreateNewCommentResponse> response) {
                        if (response.isSuccessful()) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder
                                    .setMessage("Ваш комментарий отправлен успешно")
                                    .setPositiveButton("Ok", (dialogInterface, i) -> {
                                        dialogInterface.dismiss();

                                        Intent intent = new Intent(context, CommentToPostActivity.class);
                                        ((Activity) context).finish();
                                        context.startActivity(intent);
                                    })
                                    .create()
                                    .show();

                            commentEditText.setText("");
                            prefUtils.saveAnswerToUserName("");
                            prefUtils.saveCommentIdToAnswer("");


                        } else {
                            String errorMessage = ErrorMessageFromApi.errorMessageFromApi(response.errorBody());
                            showToast(errorMessage);
                        }
                    }

                    @Override
                    public void onFailure(Call<CreateNewCommentResponse> call, Throwable t) {
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
}
