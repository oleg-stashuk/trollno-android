package com.apps.trollino.db_room.posts.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PostDao {
    // Добавить пост в БД
    @Insert
    void add(PostEntity postEntity);

    // Добавить пост в БД из категории "Свежее"
    @Insert
    void addFreshPost(PostEntity postEntity);

    // Получить список постов с БД по id и имени категории
    @Query("SELECT * FROM PostEntity WHERE categoryIdEntity = :idCategory AND categoryNameEntity = :nameCategory")
    List<PostEntity> getPostByCategory(String idCategory, String nameCategory);

    // Получить список постов с БД по имени категории
    @Query("SELECT * FROM PostEntity WHERE categoryNameEntity = :nameCategory")
    List<PostEntity> getPostByPostName(String nameCategory);

    // Получить список постов с БД по id поста
    @Query("SELECT * FROM PostEntity WHERE postIdFromAPIEntity = :postId")
    List<PostEntity> getPostByPostId(String postId);

    // Удалить все с таблицы постов с БД
    @Query("DELETE FROM PostEntity")
    void clearPostDB();

    // Удалить все с таблицы постов с БД по имени категории
    @Query("DELETE FROM PostEntity WHERE categoryNameEntity = :nameCategory")
    void removeDataFromDBbyCategoryName(String nameCategory);

    // Обновить данные поста
    @Update()
    void updatePostInDB(PostEntity postEntity);
}

