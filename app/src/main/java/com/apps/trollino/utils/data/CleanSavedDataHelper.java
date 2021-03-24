package com.apps.trollino.utils.data;

public class CleanSavedDataHelper {

    public static void cleanAllDataFromApi(PrefUtils prefUtils) {
        CommentListFromApi.getInstance().removeAllDataFromList(prefUtils);
//        AnswersFromApi.getInstance().removeAllDataFromList(prefUtils);
        DataListFromApi.getInstance().removeAllDataFromList(prefUtils);
        FavoritePostListFromApi.getInstance().removeAllDataFromList(prefUtils);
        PostListByCategoryFromApi.getInstance().removeAllDataFromList(prefUtils);
        PostListBySearchFromApi.getInstance().removeAllDataFromList(prefUtils);

        prefUtils.saveCurrentPage(0);
        cleanAdapterPosition(prefUtils);
    }

    public static void cleanAllDataIfUserRemoveOrLogout(PrefUtils prefUtils) {
        cleanAllDataFromApi(prefUtils);

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

    public static void cleanAdapterPosition(PrefUtils prefUtils) {
        prefUtils.saveCurrentAdapterPositionComment(0);
        prefUtils.saveCurrentAdapterPositionAnswers(0);
        prefUtils.saveCurrentAdapterPositionFavorite(0);
    }
}
