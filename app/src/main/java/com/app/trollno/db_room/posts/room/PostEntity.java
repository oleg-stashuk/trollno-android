package com.app.trollno.db_room.posts.room;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class PostEntity {
    @PrimaryKey
    @NonNull
    public String postIdUniqueEntity;
    public String postIdFromAPIEntity;
    public String titleEntity;
    public String categoryIdEntity;
    public String categoryNameEntity;
    public String commentCountEntity;
    public String imageUrlEntity;
    public boolean isReadPostEntity;
    public boolean isCommentActiveDiscusEntity;
}
