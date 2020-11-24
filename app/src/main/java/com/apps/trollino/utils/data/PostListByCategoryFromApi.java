package com.apps.trollino.utils.data;

import com.apps.trollino.data.model.PostsModel;

import java.util.ArrayList;
import java.util.List;

public class PostListByCategoryFromApi {
    private List<PostsModel.PostDetails> postListByCategory;

    private static volatile PostListByCategoryFromApi instance = null;
    public static PostListByCategoryFromApi getInstance() {
        if(instance == null) {
            instance = new PostListByCategoryFromApi();
        }
        return instance;
    }

    private PostListByCategoryFromApi() {
        postListByCategory = new ArrayList<>();
    }

    public void savePostByCategoryInList(List<PostsModel.PostDetails> postList) {
        if (postListByCategory.isEmpty()) {
            postListByCategory.addAll(postList);
        } else {
            for (PostsModel.PostDetails postFromApi : postList) {
                if (! postListByCategory.contains(postFromApi)) {
                    postListByCategory.add(postFromApi);
                }
            }
        }
    }

    public List<PostsModel.PostDetails> getPostListByCategory() {
        return postListByCategory;
    }

    public void removeAllDataFromList(PrefUtils prefUtils) {
        postListByCategory.clear();
        prefUtils.savePostByCategoryCurrentPage(0);
    }
}
