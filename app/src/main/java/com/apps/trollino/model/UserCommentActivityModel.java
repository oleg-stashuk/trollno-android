package com.apps.trollino.model;

import java.util.ArrayList;
import java.util.List;

public class UserCommentActivityModel {
    private boolean isReadUserComment;
    private String titleUserComment;
    private String commentUserComment;
    private String likeCountUserComment;
    private boolean isHasNewCommentUserComment;
    private String timeUserComment;

    public UserCommentActivityModel(boolean isReadUserComment, String titleUserComment, String commentUserComment, String likeCountUserComment, boolean isHasNewCommentUserComment, String timeUserComment) {
        this.isReadUserComment = isReadUserComment;
        this.titleUserComment = titleUserComment;
        this.commentUserComment = commentUserComment;
        this.likeCountUserComment = likeCountUserComment;
        this.isHasNewCommentUserComment = isHasNewCommentUserComment;
        this.timeUserComment = timeUserComment;
    }

    public boolean isReadUserComment() {
        return isReadUserComment;
    }

    public String getTitleUserComment() {
        return titleUserComment;
    }

    public String getCommentUserComment() {
        return commentUserComment;
    }

    public String getLikeCountUserComment() {
        return likeCountUserComment;
    }

    public boolean isHasNewCommentUserComment() {
        return isHasNewCommentUserComment;
    }

    public String getTimeUserComment() {
        return timeUserComment;
    }


    public static List<UserCommentActivityModel> makeUserCommentList() {
        List<UserCommentActivityModel> userCommentList = new ArrayList<>();
        userCommentList.add(new UserCommentActivityModel(false, "Заголовок_1", "Комментарий_1", "+5",
                true, "12 ч. назад"));

        userCommentList.add(new UserCommentActivityModel(false, "Заголовок_2", "Комментарий_2", "+100",
                true, "1 д. назад"));

        userCommentList.add(new UserCommentActivityModel(true, "Заголовок_3", "Комментарий_3", "",
                false, "7 д. назад"));


        return userCommentList;
    }

}
