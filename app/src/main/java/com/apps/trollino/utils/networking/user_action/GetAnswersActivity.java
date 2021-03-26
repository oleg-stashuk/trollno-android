package com.apps.trollino.utils.networking.user_action;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.apps.trollino.R;
import com.apps.trollino.adapters.AnswersAdapter;
import com.apps.trollino.data.model.comment.CommentModel;
import com.apps.trollino.data.networking.ApiService;
import com.apps.trollino.utils.SnackBarMessageCustom;
import com.apps.trollino.utils.data.AnswersFromApi;
import com.apps.trollino.utils.data.PrefUtils;
import com.apps.trollino.utils.dialogs.GuestDialog;
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

public class GetAnswersActivity {
    private static int page;
    private static int totalPage;
    private static int totalAnswers;
    private static boolean isGetNewListThis;
    private static Context cont;
    private static PrefUtils prefUt;

    public static void getAnswersActivity(Context context, PrefUtils prefUtils, AnswersAdapter adapter,
                                          RecyclerView recycler, ShimmerFrameLayout shimmer, SwipyRefreshLayout refreshLayout,
                                          boolean isGetNewList, View includeNoDataForUser,
                                          TextView noDataTextView, View bottomNavigation) {
        String cookie = prefUtils.getCookie();
        String userId = prefUtils.getUserUid();
        prefUt = prefUtils;
        page = isGetNewList ? 0 : prefUtils.getCurrentPage();
        if(isGetNewList) {
            prefUtils.saveCurrentAdapterPositionAnswers(0);
        }
        isGetNewListThis = isGetNewList;
        cont = context;

        ApiService.getInstance(context).getAnswersActivity(cookie, userId, page, new Callback<CommentModel>() {
            @Override
            public void onResponse(Call<CommentModel> call, Response<CommentModel> response) {
                if(response.isSuccessful()) {
                    CommentModel commentModel = response.body();
                    List<CommentModel.Comments> commentList = commentModel.getCommentsList();
                    totalPage = commentModel.getPagerModel().getTotalPages() - 1;
                    totalAnswers = commentModel.getPagerModel().getTotalItems();

                    if (commentList.size() == 0 || commentList.isEmpty()) {
                        includeNoDataForUser.setVisibility(View.VISIBLE);
                        noDataTextView.setText(context.getResources().getString(R.string.txt_have_no_comments));
                    } else {
                        includeNoDataForUser.setVisibility(View.GONE);
                        updatePostListAndNotifyRecyclerAdapter(commentList, adapter, bottomNavigation);
                        saveCurrentPage(prefUtils);
                    }
                } else if(response.code() == 403) {
                    GuestDialog dialog = new GuestDialog();
                    dialog.showDialog(context);
                } else {
                    String errorMessage = ErrorMessageFromApi.errorMessageFromApi(response.errorBody());
                    SnackBarMessageCustom.showSnackBarOnTheTopByBottomNavigation(bottomNavigation, errorMessage);
                }

                hideUpdateDataProgressView(recycler, shimmer, refreshLayout);
            }

            @Override
            public void onFailure(Call<CommentModel> call, Throwable t) {
                t.getStackTrace();
                boolean isHaveNotInternet = t.getLocalizedMessage().contains(context.getString(R.string.internet_error_from_api));
                String noInternetMessage = context.getResources().getString(R.string.internet_error_message);
                if (isHaveNotInternet) {
                    Snackbar snackbar  = Snackbar
                            .make(bottomNavigation, noInternetMessage, Snackbar.LENGTH_INDEFINITE)
                            .setMaxInlineActionWidth(3)
                            .setAction(R.string.refresh_button, v -> {
                                call.clone().enqueue(this);
                            });
                    snackbar.setAnchorView(bottomNavigation);
                    snackbar.show();
                } else {
                    SnackBarMessageCustom.showSnackBarOnTheTopByBottomNavigation(recycler, t.getLocalizedMessage());
                }
                hideUpdateDataProgressView(recycler, shimmer, refreshLayout);
                Log.d(TAG_LOG, "t.getLocalizedMessage() " + t.getLocalizedMessage());
            }
        });
    }

    private static void hideUpdateDataProgressView(RecyclerView recycler, ShimmerFrameLayout shimmer, SwipyRefreshLayout refreshLayout) {
        if(shimmer != null) {
            ShimmerHide.shimmerHide(recycler, shimmer);
        }
        if (refreshLayout != null) {
            refreshLayout.setRefreshing(false);
        }
    }

    private static void saveCurrentPage(PrefUtils prefUtils) {
        if(page < totalPage) {
            prefUtils.saveCurrentPage(page + 1);
        } else {
            prefUtils.saveCurrentPage(totalPage);
        }
    }

    private static void updatePostListAndNotifyRecyclerAdapter(List<CommentModel.Comments> comments, AnswersAdapter adapter, View bottomNavigation) {
        int currentListSize = AnswersFromApi.getInstance().getAnswerList().size();
        AnswersFromApi.getInstance().saveAnswersInList(comments);
        int newListSize = AnswersFromApi.getInstance().getAnswerList().size();
        if(newListSize == currentListSize && page == totalPage && ! isGetNewListThis) {
            SnackBarMessageCustom.showSnackBarOnTheTopByBottomNavigation(bottomNavigation, cont.getResources().getString(R.string.msg_user_have_not_any_answer));
        } else {
            adapter.notifyDataSetChanged();
        }

        int currentAdapterPosition =  prefUt.getCurrentAdapterPositionAnswers();
        if(currentAdapterPosition > 0 && totalAnswers - 1 > currentAdapterPosition){
            prefUt.saveCurrentAdapterPositionAnswers(currentAdapterPosition + 1);
        }
    }
}