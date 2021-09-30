package com.app.trollno.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.app.trollno.data.model.PostsModel;
import com.app.trollno.ui.main_group.PostActivity;
import com.app.trollno.utils.data.PrefUtils;

public class OpenPostActivityHelper {

    public static void openPostActivity(Context context, PostsModel.PostDetails item, PrefUtils prefUtils, boolean isFromCategory) {
        Intent intent = new Intent(context, PostActivity.class);
        prefUtils.saveCurrentPostId(item.getPostId());
        prefUtils.saveValuePostFromCategoryList(isFromCategory);
        context.startActivity(intent);
        ((Activity) context).finish();
    }
}
