package com.apps.trollino.utils;

import android.util.Log;

import com.apps.trollino.data.model.PostsModel;

import java.util.ArrayList;
import java.util.List;

public class DataListFromApi {
    private List<PostsModel.PostDetails> newPostList;
    private List<PostsModel.PostDetails> discussPostList;

    private static volatile DataListFromApi instance = null;
    public static DataListFromApi getInstance() {
        if(instance == null) {
            instance = new DataListFromApi();
        }
        return instance;
    }

    private DataListFromApi() {
        newPostList = new ArrayList<>();
        discussPostList = new ArrayList<>();
    }

    public void saveDataInList(List<PostsModel.PostDetails> postList) {
        if (newPostList.isEmpty()) {
            newPostList.addAll(postList);
        } else {
            for (PostsModel.PostDetails postFromApi : postList) {
                if (! newPostList.contains(postFromApi)) {
                    newPostList.add(postFromApi);
                }
            }
        }
        Log.d("OkHttp", "newPostList from SingleTon (save): size " + newPostList.size());
    }

    public List<PostsModel.PostDetails> getNewPostsList() {
        Log.d("OkHttp", "newPostList from SingleTon (get): size " + newPostList.size());
        return newPostList;
    }

    public void saveDiscussDataInList(List<PostsModel.PostDetails> postList) {
        discussPostList.clear();
        discussPostList.addAll(postList);
    }

    public List<PostsModel.PostDetails> getDiscussPostsList() {
        Log.d("OkHttp", "newPostList from SingleTon (get): size " + newPostList.size());
        return discussPostList;
    }

    public void removeAllDataFromList(PrefUtils prefUtils) {
        newPostList.clear();
        discussPostList.clear();
        prefUtils.saveNewPostCurrentPage(0);
        Log.d("OkHttp", "newPostList from SingleTon (remove): size " + newPostList.size());
    }

}
