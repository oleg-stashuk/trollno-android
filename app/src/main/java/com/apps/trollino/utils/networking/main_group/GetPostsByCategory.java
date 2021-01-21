package com.apps.trollino.utils.networking.main_group;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.RecyclerView;

import com.apps.trollino.R;
import com.apps.trollino.adapters.PostListAdapter;
import com.apps.trollino.data.model.PostsModel;
import com.apps.trollino.data.networking.ApiService;
import com.apps.trollino.utils.SnackBarMessageCustom;
import com.apps.trollino.utils.data.PostListByCategoryFromApi;
import com.apps.trollino.utils.data.PrefUtils;
import com.apps.trollino.utils.networking_helper.ErrorMessageFromApi;
import com.apps.trollino.utils.networking_helper.ShimmerHide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.apps.trollino.utils.Const.COUNT_TRY_REQUEST;

public class GetPostsByCategory {
    private static int page;
    private static RecyclerView recyclerView;
    private static boolean scrollOnTopOrFirstRequest;

    public static void getPostsByCategory(Context context, PrefUtils prefUtils, PostListAdapter adapter,
                                          RecyclerView recycler, ShimmerFrameLayout shimmer,
                                          ProgressBar progressBar, boolean scrollOnTop) {
        page = scrollOnTop ? 0 : prefUtils.getCurrentPage();
        scrollOnTopOrFirstRequest = scrollOnTop;
        String cookie = prefUtils.getCookie();
        String categoryId = prefUtils.getSelectedCategoryId();
        recyclerView = recycler;

        ApiService.getInstance(context).getPostsByCategory(cookie, categoryId, page, new Callback<PostsModel>() {
            int countTry = 0;

            @Override
            public void onResponse(Call<PostsModel> call, Response<PostsModel> response) {
                if(response.isSuccessful()) {
                    PostsModel post = response.body();
                    List<PostsModel.PostDetails> newPostList = post.getPostDetailsList();

                    saveCurrentPage(post.getPagerModel().getTotalPages(), prefUtils);
                    updatePostListAndNotifyRecyclerAdapter(newPostList, adapter);

                } else {
                    String errorMessage = ErrorMessageFromApi.errorMessageFromApi(response.errorBody());
                    SnackBarMessageCustom.showSnackBar(recycler, errorMessage);
                }
                progressBar.setVisibility(View.GONE);
                ShimmerHide.shimmerHide(recycler, shimmer);
            }

            @Override
            public void onFailure(Call<PostsModel> call, Throwable t) {
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
                    Log.d("OkHttp", "t.getLocalizedMessage() " + t.getLocalizedMessage());
                }
            }
        });
    }

    private static void saveCurrentPage(int totalPage, PrefUtils prefUtils) {
        if(page < totalPage - 1) {
            prefUtils.saveNewPostCurrentPage(page + 1);
        } else {
            prefUtils.saveNewPostCurrentPage(totalPage - 1);
        }
    }

    private static void updatePostListAndNotifyRecyclerAdapter(List<PostsModel.PostDetails> newPostList, PostListAdapter adapter) {
        int currentListSize = PostListByCategoryFromApi.getInstance().getPostListByCategory().size();
        PostListByCategoryFromApi.getInstance().savePostByCategoryInList(newPostList);
        int newListSize = PostListByCategoryFromApi.getInstance().getPostListByCategory().size();
        if(newListSize == 0 && scrollOnTopOrFirstRequest) {
            SnackBarMessageCustom.showSnackBar(recyclerView, "В этой категории пока ничего нет");
        } else if(newListSize <= currentListSize && !scrollOnTopOrFirstRequest) {
            SnackBarMessageCustom.showSnackBar(recyclerView, "Новых постов пока нет");
        }
        adapter.notifyDataSetChanged();
    }

}
