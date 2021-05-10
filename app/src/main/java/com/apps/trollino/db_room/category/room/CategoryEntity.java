package com.apps.trollino.db_room.category.room;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class CategoryEntity {
    @PrimaryKey
    @NonNull
    public String idCategory;
    public String nameCategory;
    public String weightCategory;
    public int savedPostPosition;
}
