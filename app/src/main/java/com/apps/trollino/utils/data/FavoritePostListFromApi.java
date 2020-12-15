package com.apps.trollino.utils.data;

import com.apps.trollino.data.model.PostsModel;

import java.util.ArrayList;
import java.util.List;

public class FavoritePostListFromApi {
    private List<PostsModel.PostDetails> favoritePostList;

    private static volatile FavoritePostListFromApi instance = null;
    public static FavoritePostListFromApi getInstance() {
        if(instance == null) {
            instance = new FavoritePostListFromApi();
        }
        return instance;
    }

    private FavoritePostListFromApi() {
        favoritePostList = new ArrayList<>();
    }

    public void saveFavoritePostInList(List<PostsModel.PostDetails> postList) {
        if (favoritePostList.isEmpty()) {
            favoritePostList.addAll(postList);
        } else {
            for (PostsModel.PostDetails postFromApi : postList) {
                if (! favoritePostList.contains(postFromApi)) {
                    favoritePostList.add(postFromApi);
                }
            }
        }
    }

    public List<PostsModel.PostDetails> getFavoritePostLis() {
        return favoritePostList;
    }

    public void removeAllDataFromList(PrefUtils prefUtils) {
        favoritePostList.clear();
        prefUtils.saveNewPostCurrentPage(0);
    }
}
