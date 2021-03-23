package com.apps.trollino.data.networking;

import com.apps.trollino.data.model.comment.CommentModel;
import com.apps.trollino.data.model.comment.CreateNewCommentRequest;
import com.apps.trollino.data.model.comment.LikeCommentModelRequest;
import com.apps.trollino.data.model.user_action.CountNewAnswersModel;
import com.apps.trollino.data.model.user_action.ReadAnswerRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CommentApi {
    @Headers({
            "Content-Type: application/json",
            "Accepts: application/json"
    })
    @GET("/post/{post_id}/comments?_format=json")
    Call<CommentModel> getCommentListByPost(@Header("Cookie") String cookie, @Path("post_id") String postId,
                                            @Query("sort_by") String sortBy, @Query("sort_order") String sortOrder);

    @Headers({
            "Content-Type: application/json",
            "Accepts: application/json"
    })
    @GET("/comment/{parent_cid}/answers/list")
    Call<CommentModel> getCommentListByComment(@Header("Cookie") String cookie, @Path("parent_cid") String parentCid);

    @Headers({
            "Content-Type: application/json",
            "Accepts: application/json"
    })
    @POST("/comment/?_format=json")
    Call<Void> postNewCommentToPost(@Header("Cookie") String cookie, @Header("X-CSRF-Token") String token,
                                                        @Body CreateNewCommentRequest createNewCommentRequest);

    @Headers({
        "Content-Type: application/json",
        "Accepts: application/json"
    })
    @POST("/api/flag?_format=json")
    Call<Void> postLikeToComment(@Header("Cookie") String cookie, @Header("X-CSRF-Token") String token, @Body LikeCommentModelRequest likeCommentModelRequest);

    @Headers({
            "Content-Type: application/json",
            "Accepts: application/json"
    })
    @POST("/api/unflag?_format=json")
    Call<Void> postUnlikeToComment(@Header("Cookie") String cookie, @Header("X-CSRF-Token") String token, @Body LikeCommentModelRequest likeCommentModelRequest);

    @Headers({
            "Content-Type: application/json",
            "Accepts: application/json"
    })
    @GET("/user/{uid}/comments?_format=json")
    Call<CommentModel> getAnswersActivity(@Header("Cookie") String cookie, @Path("uid") String userId, @Query("page") int page);

    @Headers({
            "Content-Type: application/json",
            "Accepts: application/json"
    })
    @GET("/user/{uid}/new_comments_count")
    Call<CountNewAnswersModel> getNewAnswerToUserComment(@Header("Cookie") String cookie, @Header("X-CSRF-Token") String token, @Path("uid") String userId);

    @Headers({
            "Content-Type: application/json",
            "Accepts: application/json"
    })
    @POST("/read_answers")
    Call<Void> markReadAllAnswersToComment(@Header("Cookie") String cookie, @Header("X-CSRF-Token") String token, @Body ReadAnswerRequest readAnswerRequest);
}
