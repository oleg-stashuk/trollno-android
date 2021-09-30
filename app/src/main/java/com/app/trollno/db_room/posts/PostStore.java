package com.app.trollno.db_room.posts;

import com.app.trollno.data.model.PostsModel;

import java.util.List;

public interface PostStore {
    // Добавить пост в БД
    void add(List<PostsModel.PostDetails> postList, String nameCategory);

    // Получить список постов с БД по id и имени категории
    List<PostsModel.PostDetails> getPostsByPostId(String idCategory, String nameCategory);

    // Получить список постов с БД по имени категории
    List<PostsModel.PostDetails> getPostByCategoryName(String nameCategory);

    // Получить список постов с БД по id поста
    List<PostsModel.PostDetails> getPostsByPostId(String idPost);

    // Удалить все с таблицы постов с БД
    void clearPostDB();

    // Удалить все с таблицы постов с БД по имени категории
    void removeDataFromDBbyCategoryName(String nameCategory);

    // Обновить данные для поста
    void updatePostInDB(PostsModel.PostDetails post);

    // Проверить уникальность подгруженных постов с АПИ и новые добавить в БД по имени категории
    void checkNewPostListAndSaveUnique(List<PostsModel.PostDetails> postListFromApi, String nameCategory);
}
