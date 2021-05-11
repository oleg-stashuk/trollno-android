package com.apps.trollino.db_room.posts.room;

import androidx.room.Database;

@Database(
        entities = {PostEntity.class},
        version = 1,
        exportSchema = false
)
public abstract class PostDataBase {
    public abstract PostDao postDao();
}
