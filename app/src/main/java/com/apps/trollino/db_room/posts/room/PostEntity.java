package com.apps.trollino.db_room.posts.room;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class PostEntity {
    @PrimaryKey
    @NonNull
    public String postIdEntity;
    public String titleEntity;
    public String categoryIdEntity;
    public String categoryNameEntity;
    public String imageUrl;
    public int read; // 0 - пост не прочитан, 1 - постпрочитан. Для неверифицированных пользователей всегда 0
    public int commentActiveDiscus; // 0 не в осуждаемом, 1 - в обсуждаемом
}
