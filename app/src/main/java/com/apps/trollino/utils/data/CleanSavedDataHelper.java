package com.apps.trollino.utils.data;

import android.content.Context;

import com.apps.trollino.R;
import com.apps.trollino.data.model.CategoryModel;
import com.apps.trollino.db_room.category.CategoryStoreProvider;

import java.util.List;

public class CleanSavedDataHelper {

    public static void cleanAllDataFromApi(PrefUtils prefUtils) {
        CommentListFromApi.getInstance().removeAllDataFromList(prefUtils);
        DataListFromApi.getInstance().removeAllDataFromList(prefUtils);
        FavoritePostListFromApi.getInstance().removeAllDataFromList(prefUtils);
        PostListByCategoryFromApi.getInstance().removeAllDataFromList(prefUtils);
        PostListBySearchFromApi.getInstance().removeAllDataFromList(prefUtils);

        prefUtils.saveCurrentPage(0);
        cleanAdapterPosition(prefUtils);
    }

    public static void cleanAllDataIfUserRemoveOrLogout(PrefUtils prefUtils) {
        cleanAllDataFromApi(prefUtils);

        AnswersFromApi.getInstance().removeAllDataFromList(prefUtils);

        prefUtils.saveToken("");
        prefUtils.saveCookie("");
        prefUtils.saveUserUid("");
        prefUtils.savePassword("");
        prefUtils.saveLogoutToken("");
        prefUtils.saveIsUserAuthorization(false);
        prefUtils.saveIsShowReadPost(false);
        prefUtils.saveIsSendPushAboutAnswerToComment(false);
        prefUtils.saveIsUserLoginByFacebook(false);

        prefUtils.saveCommentIdForActivity("");
        prefUtils.saveCurrentActivity("");

        prefUtils.saveBannerId("");
        prefUtils.saveAdMobId("");
        prefUtils.saveCountBetweenAds(0);

        prefUtils.saveCommentIdForActivity("");
        prefUtils.saveCommentIdToAnswer("");
        prefUtils.saveAnswerToUserName("");

        prefUtils.saveSelectedCategoryId("");
        prefUtils.savePrevPostId("");
        prefUtils.saveNextPostId("");
        prefUtils.saveIsFavorite(false);
        prefUtils.saveCurrentPostId("");
    }

    private static void cleanAdapterPosition(PrefUtils prefUtils) {
        prefUtils.saveCurrentAdapterPositionComment(0);
        prefUtils.saveCurrentAdapterPositionAnswers(0);
        prefUtils.saveCurrentAdapterPositionFavorite(0);
        prefUtils.saveCurrentAdapterPositionPosts(0);
    }

    // Если список категорий не загрузился с АПИ, то сбросить сохраненные позиции постов
    public static void updateExistingCategory(Context context) {
        List<CategoryModel> categoryList = CategoryStoreProvider.getInstance(context).getCategoryList();
        if (!categoryList.isEmpty()) {
            for(CategoryModel category : categoryList) {
                category.setPostInCategory(0);
                CategoryStoreProvider.getInstance(context).updateCategory(category);
            }
        } else {
            categoryList.add(0, new CategoryModel(Const.CATEGORY_FRESH_ID,
                    context.getResources().getString(R.string.fresh_txt), "0", 0));
            categoryList.add(1, new CategoryModel(Const.CATEGORY_DISCUSSED_ID,
                    context.getResources().getString(R.string.discuss_post), "0", 0));

            CategoryStoreProvider.getInstance(context).addCategoryToList(categoryList); // Добавить категории в БД
        }
    }
}
