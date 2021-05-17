package com.apps.trollino.db_room.posts;

import com.apps.trollino.data.model.PostsModel;
import com.apps.trollino.db_room.posts.room.PostEntity;

public class PostConverter {

    // Конвертировать объект PostEntity в PostsModel
    static PostsModel.PostDetails postConverter(PostEntity postEntity) {
        return new PostsModel.PostDetails(
                postEntity.postIdUniqueEntity,
                postEntity.postIdFromAPIEntity,
                postEntity.titleEntity,
                postEntity.categoryIdEntity,
                postEntity.categoryNameEntity,
                postEntity.commentCountEntity,
                postEntity.imageUrlEntity,
                postEntity.isReadPostEntity,
                postEntity.isCommentActiveDiscusEntity);
    }

    // Конвертировать объект PostsModel в PostEntity
    static PostEntity postConverter(PostsModel.PostDetails post) {
        PostEntity postEntity  = new PostEntity();

        postEntity.postIdUniqueEntity = post.getPostIdUnique();
        postEntity.postIdFromAPIEntity = post.getPostId();
        postEntity.titleEntity = post.getTitle();
        postEntity.categoryIdEntity = post.getCategoryId();
        postEntity.categoryNameEntity = post.getCategoryName();
        postEntity.commentCountEntity = post.getCommentCount();
        postEntity.imageUrlEntity = post.getImageUrl();
        postEntity.isReadPostEntity = post.getRead() == 1; // 0 - пост не прочитан, 1 - пост прочитан. Для неверифицированных пользователей всегда 0
        postEntity.isCommentActiveDiscusEntity = post.getCommentActiveDiscus() == 1; // 0 не в осуждаемом, 1 - в обсуждаемом

        return postEntity;
    }
}
