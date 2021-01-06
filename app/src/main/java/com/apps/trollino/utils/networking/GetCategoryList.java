package com.apps.trollino.utils.networking;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.apps.trollino.data.model.CategoryModel;
import com.apps.trollino.data.networking.ApiService;
import com.apps.trollino.utils.data.PrefUtils;
import com.apps.trollino.utils.networking_helper.ErrorMessageFromApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.apps.trollino.utils.Const.COUNT_TRY_REQUEST;

public class GetCategoryList {
    private static Context cont;

    public static void getCategoryList(Context context, PrefUtils prefUtils) {
        cont = context;
        String cookie = prefUtils.getCookie();

        ApiService.getInstance(context).getCategoryList(cookie, new Callback<List<CategoryModel>>() {
            int countTry = 0;

            @Override
            public void onResponse(Call<List<CategoryModel>> call, Response<List<CategoryModel>> response) {
                if(response.isSuccessful()) {
                    if (response.body().size() != 0) {
                        prefUtils.saveCategoryList(response.body());
                    }
                } else {
                    String errorMessage = ErrorMessageFromApi.errorMessageFromApi(response.errorBody());
                    showToast(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<List<CategoryModel>> call, Throwable t) {
                t.getStackTrace();
                if (countTry <= COUNT_TRY_REQUEST) {
                    call.clone().enqueue(this);
                    countTry++;
                } else {
                    showToast(t.getLocalizedMessage());
                    Log.d("OkHttp_1", "t.getLocalizedMessage() " + t.getLocalizedMessage());
                }
            }
        });
    }

    private static void showToast(String message) {
        Toast.makeText(cont, message, Toast.LENGTH_SHORT).show();
    }


}
