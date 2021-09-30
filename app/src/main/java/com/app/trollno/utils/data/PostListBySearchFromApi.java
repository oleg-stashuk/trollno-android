package com.app.trollno.utils.data;

import com.app.trollno.data.model.PostsModel;

import java.util.ArrayList;
import java.util.List;

public class PostListBySearchFromApi {
    private List<PostsModel.PostDetails> postListBySearch;

    private static volatile PostListBySearchFromApi instance = null;
    public static PostListBySearchFromApi getInstance() {
        if(instance == null) {
            instance = new PostListBySearchFromApi();
        }
        return instance;
    }

    private PostListBySearchFromApi() {
        postListBySearch = new ArrayList<>();
    }

    public void savePostBySearchInList(List<PostsModel.PostDetails> postList) {
        if (postListBySearch.isEmpty()) {
            postListBySearch.addAll(postList);
        } else {
            for (PostsModel.PostDetails postFromApi : postList) {
                if (! postListBySearch.contains(postFromApi)) {
                    postListBySearch.add(postFromApi);
                }
            }
        }
    }

    public List<PostsModel.PostDetails> getPostListBySearch() {
        return postListBySearch;
    }

    public void removeAllDataFromList(PrefUtils prefUtils) {
        postListBySearch.clear();
        prefUtils.saveCurrentPage(0);
    }
}
