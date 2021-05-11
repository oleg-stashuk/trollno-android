package com.apps.trollino.db_room.posts.room;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PostDao {
    // Получить список постов с БД по id и имени категории
    @Query("SELECT * FROM PostEntity WHERE categoryIdEntity = :idCategory AND categoryNameEntity = :nameCategory")
    List<PostEntity> getPostByCategory(String idCategory, String nameCategory);

    // Удалить все с таблицы постов с БД
    @Query("DELETE FROM PostEntity")
    void clearPostDB();

    // Удалить все с таблицы постов с БД
    @Query("DELETE FROM PostEntity WHERE categoryIdEntity = :idCategory AND categoryNameEntity = :nameCategory")
    void removeDataFromDBbyCategoryId(String idCategory, String nameCategory);

    // Обновить данные для постов по id поста ??????????????????????
    @Update()
    void updatePostInDB(PostEntity postEntity);
}

