package com.apps.trollino.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.apps.trollino.data.model.PostsModel;
import com.apps.trollino.ui.main_group.PostActivity;
import com.apps.trollino.utils.data.CleanSavedDataHelper;
import com.apps.trollino.utils.data.PrefUtils;

public class OpenPostActivityHelper {

    public static void openPostActivity(Context context, PostsModel.PostDetails item, PrefUtils prefUtils, boolean isFromCategory) {
        Intent intent = new Intent(context, PostActivity.class);
        prefUtils.saveCurrentPostId(item.getPostId());
        prefUtils.saveValuePostFromCategoryList(isFromCategory);
        context.startActivity(intent);
        ((Activity) context).finish();
    }
}
