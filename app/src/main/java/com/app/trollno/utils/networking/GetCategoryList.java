package com.app.trollno.utils.networking;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.app.trollno.R;
import com.app.trollno.data.model.CategoryModel;
import com.app.trollno.data.networking.ApiService;
import com.app.trollno.db_room.category.CategoryStoreProvider;
import com.app.trollno.utils.data.CleanSavedDataHelper;
import com.app.trollno.utils.data.Const;
import com.app.trollno.utils.data.PrefUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.trollno.utils.data.Const.TAG_LOG;

public class GetCategoryList {

    public static void getCategoryList(Context context, PrefUtils prefUtils, View view) {
        String cookie = prefUtils.getCookie();

        ApiService.getInstance(context).getCategoryList(cookie, new Callback<List<CategoryModel>>() {
            @Override
            public void onResponse(Call<List<CategoryModel>> call, Response<List<CategoryModel>> response) {
                if(response.isSuccessful() && response.body().size() != 0) {
                    List<CategoryModel> list = response.body();
                    replaceCategoryInBD(context, list);
                } else {
                    CleanSavedDataHelper.updateExistingCategory(context);
                }
            }

            @Override
            public void onFailure(Call<List<CategoryModel>> call, Throwable t) {
                t.getStackTrace();
                Log.d(TAG_LOG, "t.getLocalizedMessage() " + t.getLocalizedMessage());
            }
        });
    }

    // Загрузить список категорий с АПИ в БД
    private static void replaceCategoryInBD(Context context, List<CategoryModel> list) {
        CategoryStoreProvider.getInstance(context).removeAllCategory(); // удалить все категории с БД

        list.add(0, new CategoryModel(Const.CATEGORY_FRESH,
                context.getResources().getString(R.string.fresh_txt), "0", 0, 0, 0, 0));
        list.add(1, new CategoryModel(Const.CATEGORY_DISCUSSED,
                context.getResources().getString(R.string.discuss_post), "0", 0, 0, 0, 0));

        CategoryStoreProvider.getInstance(context).addCategoryToList(list); // Добавить все категории в БД
    }
}
