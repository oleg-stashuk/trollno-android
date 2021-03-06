package com.app.trollno.data.networking;

import android.content.Context;

import com.app.trollno.data.model.CategoryModel;
import com.app.trollno.data.model.PostsModel;
import com.app.trollno.data.model.SettingsModel;
import com.app.trollno.data.model.comment.CommentModel;
import com.app.trollno.data.model.comment.CreateCommentBody;
import com.app.trollno.data.model.comment.CreateNewCommentRequest;
import com.app.trollno.data.model.comment.LikeCommentModelRequest;
import com.app.trollno.data.model.login.FacebookRequestModel;
import com.app.trollno.data.model.login.RegistrationRequestModel;
import com.app.trollno.data.model.login.RegistrationResponseModel;
import com.app.trollno.data.model.login.RequestLoginModel;
import com.app.trollno.data.model.login.ResponseLoginModel;
import com.app.trollno.data.model.profile.RequestBlockUserModel;
import com.app.trollno.data.model.profile.RequestPushNotificationToken;
import com.app.trollno.data.model.profile.RequestUpdateAvatarModel;
import com.app.trollno.data.model.profile.RequestUpdateSentPushNewAnswers;
import com.app.trollno.data.model.profile.RequestUpdateShowName;
import com.app.trollno.data.model.profile.RequestUpdateShowReadPosts;
import com.app.trollno.data.model.profile.RequestUpdateUserPassword;
import com.app.trollno.data.model.profile.UserProfileModel;
import com.app.trollno.data.model.single_post.ItemPostModel;
import com.app.trollno.data.model.single_post.MarkPostAsReadModel;
import com.app.trollno.data.model.single_post.RequestBookmarkPostModel;
import com.app.trollno.data.model.single_post.ResponseBookmarkModel;
import com.app.trollno.data.model.user_action.AnswersModel;
import com.app.trollno.data.model.user_action.CountNewAnswersModel;
import com.app.trollno.data.model.user_action.ReadAnswerRequest;
import com.app.trollno.utils.networking_helper.CustomConverterForItemPost;
import com.app.trollno.utils.networking_helper.ReceivedCookiesInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.app.trollno.utils.data.Const.BASE_URL;

public class ApiService {
    private PostApi postApi;
    private AuthorisationApi authorisationApi;
    private SettingsApi settingsApi;
    private UserApi userApi;
    private SinglePostApi singlePostApi;
    private CommentApi commentApi;

    private static volatile ApiService instance = null;
    public static ApiService getInstance(Context context) {
        if(instance == null) {
            instance = new ApiService(context);
        }
        return instance;
    }

    private ApiService(Context context) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(ItemPostModel.class, new CustomConverterForItemPost())
//                .registerTypeAdapter(ItemPostModel.class, new CustomConverterForItemPost())
                .setLenient()
                .create();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new ReceivedCookiesInterceptor(context));
        httpClient.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        postApi = retrofit.create(PostApi.class);
        authorisationApi = retrofit.create(AuthorisationApi.class);
        settingsApi = retrofit.create(SettingsApi.class);
        userApi = retrofit.create(UserApi.class);
        singlePostApi = retrofit.create(SinglePostApi.class);
        commentApi = retrofit.create(CommentApi.class);
    }

    // GET and POST request for work with for Tape Post block
    public void getNewPosts(String cookie, int page, Callback<PostsModel> callback) {
        postApi.getNewPosts(cookie, page).enqueue(callback);
    }

    public void getMostDiscusPosts(String cookie, int page, Callback<PostsModel> callback) {
        postApi.getMostDiscusPosts(cookie, page).enqueue(callback);
    }

    public void getCategoryList(String cookie, Callback<List<CategoryModel>> callback) {
        postApi.getCategoryList(cookie).enqueue(callback);
    }

    public void getPostsByCategory(String cookie, String categoryId, int page, Callback<PostsModel> callback) {
        postApi.getPostsByCategory(cookie, categoryId, page).enqueue(callback);
    }


    public void getSearchPosts(String cookie, String searchText, int page, Callback<PostsModel> callback) {
        postApi.getSearchPosts(cookie, searchText, page).enqueue(callback);
    }

    public void getFavoritePostList(String cookie, int page, Callback<PostsModel> callback) {
        postApi.getFavoritePostList(cookie, page).enqueue(callback);
    }

    // GET and POST request for Single Post block
    public void getItemPost(String cookie, String postId, Callback<ItemPostModel> callback) {
        singlePostApi.getPostItem(cookie, postId).enqueue(callback);
    }

    public void addPostToFavorite(String cookie, String token, String postId, Callback<ResponseBookmarkModel> callback) {
        singlePostApi.addPostInFavorite(cookie, token, new RequestBookmarkPostModel(postId)).enqueue(callback);
    }

    public void removePostFromFavorite(String cookie, String token, String postId, Callback<Void> callback) {
        singlePostApi.removePostFromFavorite(cookie, token, new RequestBookmarkPostModel(postId)).enqueue(callback);
    }

    public void markPostAsRead(String cookie, String token, String postId, Callback<Void> callback) {
        singlePostApi.markPostAsRead(cookie, token, new MarkPostAsReadModel(postId)).enqueue(callback);
    }

    // GET and POST request for work with Authorisation block
    public void postLogin(String login, String password, Callback<ResponseLoginModel> callback) {
        authorisationApi.postLogin(new RequestLoginModel(login, password)).enqueue(callback);
    }

    public void loginWithFacebook(String facebookToken, Callback<ResponseLoginModel> callback) {
        authorisationApi.loginWithFacebook(new FacebookRequestModel(facebookToken)).enqueue(callback);
    }

    public void postRegistration(String login, String mail,String pass, String showName, Callback<RegistrationResponseModel> callback) {
        authorisationApi.postRegistration(new RegistrationRequestModel(login, mail, pass, showName)).enqueue(callback);
    }


    public void getUserProfileData(String cookie, String userUid, Callback<UserProfileModel> callback) {
        authorisationApi.getUserProfileData(cookie, userUid).enqueue(callback);
    }

    public void postLogout(String cookie, String token, String logoutToken, Callback<Void> callback) {
        authorisationApi.postLogout(cookie, token, logoutToken).enqueue(callback);
    }

    public void postLostPassword(String email, Callback<Void> callback) {
        authorisationApi.postLostPassword(new RequestLoginModel(email)).enqueue(callback);
    }

    // GET request settings: get list for avatar's image and count block before advertising
    public void getSettings(String cookie, Callback<SettingsModel> callback) {
        settingsApi.getUserSettings(cookie).enqueue(callback);
    }

    // request for update user profile
    public void updateAvatar(String cookie, String token, RequestUpdateAvatarModel UidAvatar, int userUid, Callback<UserProfileModel> callback) {
        userApi.updateAvatar(cookie, token, UidAvatar, userUid).enqueue(callback);
    }

    public void blockUser(String cookie, String token, RequestBlockUserModel userBlockModel, int userUid, Callback<Void> callback) {
        userApi.blockUser(cookie, token, userBlockModel, userUid).enqueue(callback);
    }

    public void updatePassword(String cookie, String token, int userUid, RequestUpdateUserPassword updatePasswordModel, Callback<UserProfileModel> callback) {
        userApi.updatePassword(cookie, token, userUid, updatePasswordModel).enqueue(callback);
    }

    public void updateShowName(String cookie, String token, int userUid, String showName, Callback<Void> callback) {
        userApi.updateShowName(cookie, token, userUid, new RequestUpdateShowName(showName)).enqueue(callback);
    }

    public void updatePushNotificationToken(String cookie, String token, int userUid, RequestPushNotificationToken updatePushToken, Callback<Void> callback) {
        userApi.updatePushNotificationToken(cookie, token, userUid, updatePushToken).enqueue(callback);
    }

    public void updateSendPushNewAnswers(String cookie, String token, int userUid, RequestUpdateSentPushNewAnswers updatePushNewAnswer, Callback<Void> callback) {
        userApi.updateSentPushNewAnswers(cookie, token, userUid, updatePushNewAnswer).enqueue(callback);
    }

    public void updateShowReadPosts(String cookie, String token, int userUid, RequestUpdateShowReadPosts updateShowReadPosts, Callback<Void> callback) {
        userApi.updateShowReadPosts(cookie, token, userUid, updateShowReadPosts).enqueue(callback);
    }


    // request for Comments block
    public void getCommentToPost(String cookie, String postId, String sortBy, String sortOrder, Callback<CommentModel> callback) {
        commentApi.getCommentListByPost(cookie, postId, sortBy, sortOrder).enqueue(callback);
    }

    public void getCommentListByComment(String cookie, String commentParentId, Callback<CommentModel> callback) {
        commentApi.getCommentListByComment(cookie, commentParentId).enqueue(callback);
    }

    public void postNewCommentToPost(String cookie, String token, List<CreateNewCommentRequest.PostId> entityIdList,
                                     List<CreateCommentBody> commentBodyList, List<CreateNewCommentRequest.ParentIdComment> parentCidList,
                                     Callback<Void> callback) {
        commentApi.postNewCommentToPost(cookie, token,
                new CreateNewCommentRequest(entityIdList, commentBodyList, parentCidList)
        ).enqueue(callback);
    }

    public void postLikeToComment(String cookie, String token, String entityCommentId, Callback<Void> callback) {
        commentApi.postLikeToComment(cookie, token, new LikeCommentModelRequest(entityCommentId)).enqueue(callback);
    }

    public void postUnlikeToComment(String cookie, String token, String entityCommentId, Callback<Void> callback) {
        commentApi.postUnlikeToComment(cookie, token, new LikeCommentModelRequest(entityCommentId)).enqueue(callback);
    }

    public void getAnswersActivity(String cookie, String userId, int page, Callback<AnswersModel> callback) {
        commentApi.getAnswersActivity(cookie, userId, page).enqueue(callback);
    }

    public void getNewAnswerToUserComment(String cookie, String token, String userId, Callback<CountNewAnswersModel> callback) {
        commentApi.getNewAnswerToUserComment(cookie, token, userId).enqueue(callback);
    }

    public void markReadAllAnswersToComment(String cookie, String token, String commentId, Callback<Void> callback) {
        commentApi.markReadAllAnswersToComment(cookie, token, new ReadAnswerRequest(true, commentId)).enqueue(callback);
    }
}
