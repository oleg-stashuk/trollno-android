package com.apps.trollino.utils.data;

public class CleanSavedDataHelper {

    public static void cleanAllDataFromApi(PrefUtils prefUtils) {
        CommentListFromApi.getInstance().removeAllDataFromList(prefUtils);
        CommentListToUserActivityFromApi.getInstance().removeAllDataFromList(prefUtils);
        DataListFromApi.getInstance().removeAllDataFromList(prefUtils);
        FavoritePostListFromApi.getInstance().removeAllDataFromList(prefUtils);
        PostListByCategoryFromApi.getInstance().removeAllDataFromList(prefUtils);
        PostListBySearchFromApi.getInstance().removeAllDataFromList(prefUtils);
    }

    public static void cleanAllDataIfUserRemoveOrLogout(PrefUtils prefUtils) {
        cleanAllDataFromApi(prefUtils);
        prefUtils.saveToken("");
        prefUtils.saveCookie("");
        prefUtils.saveUserUid("");
        prefUtils.savePassword("");
        prefUtils.saveIsUserAuthorization(false);
        prefUtils.saveLogoutToken("");
    }
}
