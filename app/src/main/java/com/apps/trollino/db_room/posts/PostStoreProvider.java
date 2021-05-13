package com.apps.trollino.db_room.posts;

import android.content.Context;

public class PostStoreProvider {
    // Singleton
    private static PostStore instance;
    private PostStoreProvider() {}

    public static PostStore getInstance(Context context) {
        if(instance == null) {
            instance = new RoomPostStore(context);
        }
        return instance;
    }
    // End of singleton
}
