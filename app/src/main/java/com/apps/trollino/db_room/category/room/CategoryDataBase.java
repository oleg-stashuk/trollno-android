package com.apps.trollino.db_room.category.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(
        entities = {CategoryEntity.class},
        version = 1,
        exportSchema = false
)
public abstract class CategoryDataBase extends RoomDatabase {
    public abstract CategoryDao categoryDao();
}
