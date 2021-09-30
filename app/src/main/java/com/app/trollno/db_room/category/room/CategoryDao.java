package com.app.trollno.db_room.category.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CategoryDao {
    // Добавить категорию в БД
    @Insert
    void add(CategoryEntity categoryEntity);

    // Получить весь список категорий с БД
    @Query("SELECT * FROM CategoryEntity")
    List<CategoryEntity> getCategoryListFromDB();

    // Удалить все с таблицы категорий  с БД
    @Query("DELETE FROM CategoryEntity")
    void clearCategoryDB();

    // Обновить данные для одной категории
    @Update
    void updateCategoryData(CategoryEntity categoryEntity);

    // Получить категорию с БД по id
    @Query("SELECT * FROM CategoryEntity WHERE idCategory = :idCategory")
    CategoryEntity getCategoryById(String idCategory);

}
