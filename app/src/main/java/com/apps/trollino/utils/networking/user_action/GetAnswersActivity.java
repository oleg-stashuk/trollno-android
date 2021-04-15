package com.apps.trollino.utils.networking.user_action;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.apps.trollino.R;
import com.apps.trollino.adapters.AnswersAdapter;
import com.apps.trollino.data.model.user_action.AnswersModel;
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
    private static PrefUtils prefUt;
    private static RecyclerView recyclerView;

    public static void getAnswersActivity(Context context, PrefUtils prefUtils, AnswersAdapter adapter,
                                          RecyclerView recycler, ShimmerFrameLayout shimmer, SwipyRefreshLayout refreshLayout,
                                          boolean isGetNewList, View includeNoDataForUser,
                                          TextView noDataTextView, View bottomNavigation, ProgressBar progressBar) {
        String cookie = prefUtils.getCookie();
        String userId = prefUtils.getUserUid();
        prefUt = prefUtils;
        page = isGetNewList ? 0 : prefUtils.getCurrentPage();
        isGetNewListThis = isGetNewList;
        recyclerView = recycler;
        if(isGetNewList) {
            AnswersFromApi.getInstance().removeAllDataFromList(prefUtils);
        }

        ApiService.getInstance(context).getAnswersActivity(cookie, userId, page, new Callback<AnswersModel>() {
            @Override
            public void onResponse(Call<AnswersModel> call, Response<AnswersModel> response) {
                if(response.isSuccessful()) {
                    AnswersModel answerModel = response.body();
                    List<AnswersModel.Answers> answerList = answerModel.getAnswerList();
                    totalPage = answerModel.getPagerModel().getTotalPages() - 1;
                    totalAnswers = answerModel.getPagerModel().getTotalItems();

                    if (answerList.size() == 0 || answerList.isEmpty()) {
                        includeNoDataForUser.setVisibility(View.VISIBLE);
                        noDataTextView.setText(context.getResources().getString(R.string.txt_have_no_comments));
                    } else {
                        includeNoDataForUser.setVisibility(View.GONE);
                        updatePostListAndNotifyRecyclerAdapter(answerList, adapter);
                        saveCurrentPage(prefUtils);
                    }
                } else if(response.code() == 403) {
                    GuestDialog dialog = new GuestDialog();
                    dialog.showDialog(context);
                } else {
                    String errorMessage = ErrorMessageFromApi.errorMessageFromApi(response.errorBody());
                    SnackBarMessageCustom.showSnackBarOnTheTopByBottomNavigation(bottomNavigation, errorMessage);
                }

                hideUpdateDataProgressView(shimmer, refreshLayout, progressBar);
            }

            @Override
            public void onFailure(Call<AnswersModel> call, Throwable t) {
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
                hideUpdateDataProgressView(shimmer, refreshLayout, progressBar);
                Log.d(TAG_LOG, "t.getLocalizedMessage() " + t.getLocalizedMessage());
            }
        });
    }

    private static void hideUpdateDataProgressView(ShimmerFrameLayout shimmer,
                                                   SwipyRefreshLayout refreshLayout, ProgressBar progressBar) {
        if(shimmer != null) {
            ShimmerHide.shimmerHide(recyclerView, shimmer);
        }
        if (refreshLayout != null) {
            refreshLayout.setRefreshing(false);
        }
        progressBar.setVisibility(View.GONE);
    }

    private static void saveCurrentPage(PrefUtils prefUtils) {
        if(page < totalPage) {
            prefUtils.saveCurrentPage(page + 1);
        } else {
            prefUtils.saveCurrentPage(totalPage);
        }
    }

    private static void updatePostListAndNotifyRecyclerAdapter(List<AnswersModel.Answers> comments, AnswersAdapter adapter) {
        int currentListSize = AnswersFromApi.getInstance().getAnswerList().size();
        AnswersFromApi.getInstance().saveAnswersInList(comments);
        int newListSize = AnswersFromApi.getInstance().getAnswerList().size();
        if(newListSize != currentListSize && page != totalPage && isGetNewListThis) {
            adapter.notifyDataSetChanged();
            recyclerView.getLayoutManager().scrollToPosition(0);
        }
        recyclerView.suppressLayout(false);

        int currentAdapterPosition =  prefUt.getCurrentAdapterPositionAnswers();
        if(currentAdapterPosition > 0 && totalAnswers - 1 > currentAdapterPosition){
            prefUt.saveCurrentAdapterPositionAnswers(currentAdapterPosition + 1);
        }
    }
}
