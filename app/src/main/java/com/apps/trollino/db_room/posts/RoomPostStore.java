package com.apps.trollino.db_room.posts;

import android.content.Context;

import androidx.room.Room;

import com.apps.trollino.data.model.PostsModel;
import com.apps.trollino.db_room.posts.room.PostDao;
import com.apps.trollino.db_room.posts.room.PostDataBase;
import com.apps.trollino.db_room.posts.room.PostEntity;

import java.util.ArrayList;
import java.util.List;

import static com.apps.trollino.utils.data.Const.CATEGORY_DISCUSSED;
import static com.apps.trollino.utils.data.Const.CATEGORY_FRESH;

public class RoomPostStore implements PostStore{
    private PostDao postDao;

    public RoomPostStore(Context context) {
        postDao = Room
                .databaseBuilder(context, PostDataBase.class, "post-database-sqlite")
                .allowMainThreadQueries()
                .build()
                .postDao();
    }

    @Override
    public void add(List<PostsModel.PostDetails> postList) {
        for (PostsModel.PostDetails post : postList) {
            postDao.add(PostConverter.postConverter(post));
        }
    }

    @Override
    public void addFreshPost(List<PostsModel.PostDetails> postList) {
        for (PostsModel.PostDetails post : postList) {
            post.setPostIdUnique(post.getPostId().concat(CATEGORY_FRESH));
            post.setCategoryName(CATEGORY_FRESH);
            postDao.add(PostConverter.postConverter(post));
        }
    }

    @Override
    public void addDiscussedPost(List<PostsModel.PostDetails> postList) {
        for (PostsModel.PostDetails post : postList) {
            post.setPostIdUnique(post.getPostId().concat(CATEGORY_DISCUSSED));
            post.setCategoryName(CATEGORY_DISCUSSED);
            postDao.add(PostConverter.postConverter(post));
        }
    }

    @Override
    public void addPostsByCategory(List<PostsModel.PostDetails> postList) {
        for (PostsModel.PostDetails post : postList) {
            post.setPostIdUnique(post.getPostId());
            post.setCategoryName(post.getCategoryName());
            postDao.add(PostConverter.postConverter(post));
        }
    }

    @Override
    public List<PostsModel.PostDetails> getPostsByPostId(String idCategory, String nameCategory) {
        List<PostEntity> postEntityList = postDao.getPostByCategory(idCategory, nameCategory);
        List<PostsModel.PostDetails> postList = new ArrayList<>();
        for(PostEntity postEntity : postEntityList) {
            postList.add(PostConverter.postConverter(postEntity));
        }
        return postList;
    }

    @Override
    public List<PostsModel.PostDetails> getPostByCategoryName(String nameCategory) {
        List<PostEntity> postEntityList = postDao.getPostByPostName(nameCategory);
        List<PostsModel.PostDetails> postList = new ArrayList<>();
        for(PostEntity postEntity : postEntityList) {
            postList.add(PostConverter.postConverter(postEntity));
        }
        return postList;
    }

    @Override
    public List<PostsModel.PostDetails> getPostsByPostId(String idPost) {
        List<PostEntity> postEntityList = postDao.getPostByPostId(idPost);
        List<PostsModel.PostDetails> postList = new ArrayList<>();
        for(PostEntity postEntity : postEntityList) {
            postList.add(PostConverter.postConverter(postEntity));
        }
        return postList;
    }

    @Override
    public void clearPostDB() {
        postDao.clearPostDB();
    }

    @Override
    public void removeDataFromDBbyCategoryName(String nameCategory) {
        postDao.removeDataFromDBbyCategoryName(nameCategory);
    }

    @Override
    public void updatePostInDB(PostsModel.PostDetails post) {
        postDao.updatePostInDB(PostConverter.postConverter(post));
    }

    @Override
    public void checkNewPostListAndSaveUnique(List<PostsModel.PostDetails> postListFromApi, String categoryName) {
        List<PostsModel.PostDetails> postListFromDB = getPostByCategoryName(categoryName);

        List<PostsModel.PostDetails> newPostList = new ArrayList<>();
        for(PostsModel.PostDetails post : postListFromApi) {
            if (! postListFromDB.contains(post)) {
                newPostList.add(post);
            }
        }
        if (newPostList.size() > 0) {
            if (categoryName.equals(CATEGORY_FRESH)) {
                addFreshPost(newPostList);
            } else {
                addPostsByCategory(newPostList);
            }
        }
    }
}
