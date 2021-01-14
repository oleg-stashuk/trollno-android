package com.apps.trollino.utils;

import android.content.Context;
import android.content.Intent;

import com.apps.trollino.ui.main_group.ActivityInPostActivity;
import com.apps.trollino.ui.main_group.CommentToPostActivity;
import com.apps.trollino.ui.main_group.FavoriteActivity;
import com.apps.trollino.ui.main_group.PostActivity;
import com.apps.trollino.ui.main_group.ProfileActivity;
import com.apps.trollino.ui.main_group.TapeActivity;
import com.apps.trollino.utils.data.PrefUtils;

public class OpenActivityHelper {
    public static final String ACTIVITY_USER_ACTIVITY = "ACTIVITY_USER_ACTIVITY";
    public static final String FAVORITE_ACTIVITY = "FAVORITE_ACTIVITY";
    public static final String PROFILE_ACTIVITY = "PROFILE_ACTIVITY";
    public static final String COMMENT_ACTIVITY = "COMMENT_ACTIVITY";
    public static final String POST_ACTIVITY = "POST_ACTIVITY";

    public static Intent openActivity(Context context, PrefUtils prefUtils){
        String currentOpenActivity = prefUtils.getCurrentActivity();
        Intent intent = null;

        switch (currentOpenActivity) {
            case ACTIVITY_USER_ACTIVITY:
                intent = new Intent(context, ActivityInPostActivity.class);
                break;
            case FAVORITE_ACTIVITY:
                intent = new Intent(context, FavoriteActivity.class);
                break;
            case PROFILE_ACTIVITY:
                intent = new Intent(context, ProfileActivity.class);
                break;
            case COMMENT_ACTIVITY:
                intent = new Intent(context, CommentToPostActivity.class);
                break;
            case POST_ACTIVITY:
                intent = new Intent(context, PostActivity.class);
                break;
            case "":
                intent = new Intent(context, TapeActivity.class);
                break;

        }

        return intent;
    }
}