package com.app.trollno.utils.data;

import com.app.trollno.data.model.PostsModel;

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

    public List<PostsModel.PostDetails> getFavoritePostList() {
        return favoritePostList;
    }

    public void removeAllDataFromList(PrefUtils prefUtils) {
        favoritePostList.clear();
        prefUtils.saveCurrentPage(0);
    }
}
