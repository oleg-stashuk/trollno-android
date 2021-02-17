package com.apps.trollino.utils.networking.user_action;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.apps.trollino.data.model.user_action.CountNewAnswersModel;
import com.apps.trollino.data.networking.ApiService;
import com.apps.trollino.utils.data.PrefUtils;
import com.apps.trollino.utils.networking_helper.ErrorMessageFromApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.apps.trollino.utils.data.Const.TAG_LOG;

public class GetNewAnswersCount {

    public static void getNewAnswersCount(Context context, PrefUtils prefUtils, ImageView imageView) {
        String cookie = prefUtils.getCookie();
        String token = prefUtils.getToken();
        String userId = prefUtils.getUserUid();

        ApiService.getInstance(context).getNewAnswerToUserComment(cookie, token, userId, new Callback<CountNewAnswersModel>() {
            @Override
            public void onResponse(Call<CountNewAnswersModel> call, Response<CountNewAnswersModel> response) {
                if(response.isSuccessful()) {
                    int countNewAnswers = Integer.parseInt(response.body().getCountNewAnswer());
                    boolean isShowIndicator = countNewAnswers > 0 ? true : false;
                    imageView.setVisibility(isShowIndicator ? View.VISIBLE : View.GONE);
                } else {
                    String errorMessage = ErrorMessageFromApi.errorMessageFromApi(response.errorBody());
                    Log.d(TAG_LOG, "errorMessage " + errorMessage);
                }
            }

            @Override
            public void onFailure(Call<CountNewAnswersModel> call, Throwable t) {
                Log.d(TAG_LOG, "t.getLocalizedMessage() " + t.getLocalizedMessage());
            }
        });
    }
}
