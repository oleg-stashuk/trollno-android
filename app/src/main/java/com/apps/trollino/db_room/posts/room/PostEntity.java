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
    public String imageUrlEntity;
    public boolean isReadPostEntity;
    public boolean isCommentActiveDiscusEntity;
}
