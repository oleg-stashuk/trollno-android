package com.app.trollno.utils.data;

import android.content.Context;

import com.app.trollno.R;
import com.app.trollno.data.model.CategoryModel;
import com.app.trollno.db_room.category.CategoryStoreProvider;
import com.app.trollno.db_room.posts.PostStoreProvider;

import java.util.List;

public class CleanSavedDataHelper {

    public static void cleanAllDataFromApi(PrefUtils prefUtils) {
        CommentListFromApi.getInstance().removeAllDataFromList(prefUtils);
        FavoritePostListFromApi.getInstance().removeAllDataFromList(prefUtils);
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

    // ???????? ???????????? ?????????????????? ???? ???????????????????? ?? ??????, ???? ???????????????? ?????????????????????? ?????????????? ????????????
    public static void updateExistingCategory(Context context) {
        List<CategoryModel> categoryList = CategoryStoreProvider.getInstance(context).getCategoryList();
        if (!categoryList.isEmpty()) {
            for(CategoryModel category : categoryList) {
                category.setPostInCategory(0);
                category.setCurrentPage(0);
                category.setTotalPages(0);
                category.setTotalItems(0);
                CategoryStoreProvider.getInstance(context).updateCategory(category);
            }
        } else {
            categoryList.add(0, new CategoryModel(Const.CATEGORY_FRESH,
                    context.getResources().getString(R.string.fresh_txt), "0", 0, 0, 0, 0));
            categoryList.add(1, new CategoryModel(Const.CATEGORY_DISCUSSED,
                    context.getResources().getString(R.string.discuss_post), "0", 0, 0, 0, 0));

            CategoryStoreProvider.getInstance(context).addCategoryToList(categoryList); // ???????????????? ?????????????????? ?? ????
        }
    }

    // ???????????????? ???? ?????? ???????????????? ????????????????????
    public static void cleanBD(Context context, PrefUtils prefUtils) {
        updateExistingCategory(context);
        PostStoreProvider.getInstance(context).clearPostDB();
        prefUtils.saveSelectedCategoryPosition(0);
    }
}
