package com.apps.trollino.db_room.category;

import android.content.Context;

public class CategoryStoreProvider {
    // Singleton
    private static CategoryStore instance;

    private CategoryStoreProvider() {}

    public static CategoryStore getInstance(Context context) {
        if(instance == null) {
            instance= new RoomCategoryStore(context);
        }
        return instance;
    }

    // End of singleton
}
