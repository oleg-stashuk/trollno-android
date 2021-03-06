package com.app.trollno.db_room.posts;

import android.content.Context;

import androidx.room.Room;

import com.app.trollno.data.model.PostsModel;
import com.app.trollno.db_room.posts.room.PostDao;
import com.app.trollno.db_room.posts.room.PostDataBase;
import com.app.trollno.db_room.posts.room.PostEntity;

import java.util.ArrayList;
import java.util.List;

import static com.app.trollno.utils.data.Const.CATEGORY_DISCUSSED;
import static com.app.trollno.utils.data.Const.CATEGORY_FRESH;

public class RoomPostStore implements PostStore{
    private final PostDao postDao;

    public RoomPostStore(Context context) {
        postDao = Room
                .databaseBuilder(context, PostDataBase.class, "post-database-sqlite")
                .allowMainThreadQueries()
                .build()
                .postDao();
    }

    @Override
    public void add(List<PostsModel.PostDetails> postList, String nameCategory) {
        for (PostsModel.PostDetails post : postList) {
            if(nameCategory.equals(CATEGORY_FRESH) || nameCategory.equals(CATEGORY_DISCUSSED)) {
                post.setPostIdUnique(post.getPostId().concat(nameCategory));
            } else {
                post.setPostIdUnique(post.getPostId());
            }
            post.setCategoryName(nameCategory);
            try {
                postDao.add(PostConverter.postConverter(post));
            } catch (Exception e) {
                e.printStackTrace();
                List<PostsModel.PostDetails> postDBList = getPostsByPostId(post.getPostId());
                for(PostsModel.PostDetails postDB : postDBList){
                    if(!postDB.getCategoryName().equals(CATEGORY_FRESH) && !postDB.getCategoryName().equals(CATEGORY_DISCUSSED)) {
                        postDB.setCategoryName(nameCategory);
                        updatePostInDB(postDB);
                    }
                }
            }
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
            add(newPostList, categoryName);
        }
    }
}
