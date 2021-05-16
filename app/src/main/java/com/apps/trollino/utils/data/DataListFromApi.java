package com.apps.trollino.utils.data;

import android.util.Log;

import com.apps.trollino.data.model.PostsModel;

import java.util.ArrayList;
import java.util.List;

import static com.apps.trollino.utils.data.Const.TAG_LOG;

public class DataListFromApi {
    private List<PostsModel.PostDetails> discussPostList;

    private static volatile DataListFromApi instance = null;
    public static DataListFromApi getInstance() {
        if(instance == null) {
            instance = new DataListFromApi();
        }
        return instance;
    }

    private DataListFromApi() {
        discussPostList = new ArrayList<>();
    }

    public void saveDiscussDataInList(List<PostsModel.PostDetails> postList) {
        discussPostList.clear();
        discussPostList.addAll(postList);
    }

    public List<PostsModel.PostDetails> getDiscussPostsList() {
        return discussPostList;
    }

    public void removeAllDataFromList(PrefUtils prefUtils) {
        discussPostList.clear();
        try {
            prefUtils.saveCurrentPage(0);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG_LOG, "!!!!!!!!!!!!!! " + e.getLocalizedMessage());
        }
    }
}
