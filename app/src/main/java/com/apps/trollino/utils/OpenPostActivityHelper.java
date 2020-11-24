package com.apps.trollino.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.apps.trollino.data.model.PostsModel;
import com.apps.trollino.ui.main_group.PostActivity;

import static com.apps.trollino.ui.main_group.PostActivity.POST_CATEGORY_KEY;
import static com.apps.trollino.ui.main_group.PostActivity.POST_FAVORITE_VALUE;
import static com.apps.trollino.ui.main_group.PostActivity.POST_ID_KEY;

public class OpenPostActivityHelper {

    public static void openPostActivity(Context context, PostsModel.PostDetails item) {
        Intent intent = new Intent(context, PostActivity.class);
        intent.putExtra(POST_ID_KEY, item.getPostId());
        intent.putExtra(POST_CATEGORY_KEY, item.getCategoryName());
        intent.putExtra(POST_FAVORITE_VALUE, item.getFavorite());
        context.startActivity(intent);
        ((Activity) context).finish();
    }
}
