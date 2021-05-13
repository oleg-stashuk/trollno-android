package com.apps.trollino.db_room.posts.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(
        entities = {PostEntity.class},
        version = 1,
        exportSchema = false
)
public abstract class PostDataBase extends RoomDatabase {
    public abstract PostDao postDao();
}
