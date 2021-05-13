package com.apps.trollino.db_room.posts;

import androidx.room.Insert;

import com.apps.trollino.data.model.PostsModel;

import java.util.List;

public interface PostStore {
    // Добавить пост в БД
    @Insert
    void add(List<PostsModel.PostDetails> post);

    // Получить список постов с БД по id и имени категории
    List<PostsModel.PostDetails> getPostByPostId(String idCategory, String nameCategory);

    // Получить список постов с БД по id поста
    List<PostsModel.PostDetails> getPostByPostId(String idPost);

    // Удалить все с таблицы постов с БД
    void clearPostDB();

    // Удалить все с таблицы постов с БД по имени категории
    void removeDataFromDBbyCategoryName(String nameCategory);

    // Обновить данные для поста
    void updatePostInDB(PostsModel.PostDetails post);
}
