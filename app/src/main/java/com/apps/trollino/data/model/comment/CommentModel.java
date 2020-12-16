package com.apps.trollino.data.model.comment;

import com.apps.trollino.R;
import com.apps.trollino.data.model.PagerModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CommentModel {
    @SerializedName("rows")
    @Expose
    private List<Comments> commentsList;
    @SerializedName("pager")
    @Expose
    private PagerModel pagerModel;


    public List<Comments> getCommentsList() {
        return commentsList;
    }

    public PagerModel getPagerModel() {
        return pagerModel;
    }



    public class Comments {
        @SerializedName("cid")
        @Expose
        private String commentId;
        @SerializedName("comment_body")
        @Expose
        private String commentBody;
        @SerializedName("changed")
        @Expose
        private String time;
        @SerializedName("flagged")
        @Expose
        private String favoriteFlag;
        @SerializedName("count")
        @Expose
        private String countLike;
        @SerializedName("name")
        @Expose
        private String authorComment;
        @SerializedName("field_user_picture")
        @Expose
        private String urlUserImage;
        @SerializedName("newsappm_comment_answers_count")
        @Expose
        private String commentAnswersCount;



        public String getCommentId() {
            return commentId;
        }

        public String getCommentBody() {
            return commentBody;
        }

        public String getFavoriteFlag() {
            return favoriteFlag;
        }

        public String getCountLike() {
            return countLike;
        }

        public String getAuthorName() {
            return authorComment;
        }

        public String getUrlUserImage() {
            return urlUserImage;
        }

        public String getCommentAnswersCount() {
            return commentAnswersCount;
        }

        public String getTime() {
            return time;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }

            if (obj.getClass() != this.getClass()) {
                return false;
            }

            final Comments other = (Comments) obj;
            if ((this.commentId == null) ? (other.commentId != null) : !this.commentId.equals(other.commentId)) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 53 * hash + (this.commentId != null ? this.commentId.hashCode() : 0);
            return hash;
        }
    }

























    private boolean isReadComment;
    private String title;
    private String comment;
    private String likeCount;
    private boolean isHasNewComment;
    private String timeUserComment;

    private int userImage;
    private String userName;
    private boolean isUserLikeIt;
    private boolean isCommentHasAnswer;

    public CommentModel(boolean isReadUserComment, String titleUserComment, String commentUserComment, String likeCountUserComment, boolean isHasNewCommentUserComment, String timeUserComment) {
        this.isReadComment = isReadUserComment;
        this.title = titleUserComment;
        this.comment = commentUserComment;
        this.likeCount = likeCountUserComment;
        this.isHasNewComment = isHasNewCommentUserComment;
        this.timeUserComment = timeUserComment;
    }

    public boolean isReadUserComment() {
        return isReadComment;
    }

    public String getTitle() {
        return title;
    }

    public String getComment() {
        return comment;
    }

    public String getLikeCount() {
        return likeCount;
    }

    public boolean isHasNewComment() {
        return isHasNewComment;
    }

    public String getTime() {
        return timeUserComment;
    }


    public static List<CommentModel> makeUserCommentList() {
        List<CommentModel> userCommentList = new ArrayList<>();
        userCommentList.add(new CommentModel(false, "Заголовок_1", "Комментарий_1", "+5",
                true, "12 ч. назад"));

        userCommentList.add(new CommentModel(false, "Заголовок_2", "Комментарий_2", "+100",
                true, "1 д. назад"));

        userCommentList.add(new CommentModel(true, "Заголовок_3", "Комментарий_3", "",
                false, "7 д. назад"));


        return userCommentList;
    }

    public CommentModel(int userImage, String userName, String time, String comment, boolean isUserLikeIt, String likeCount, boolean isCommentHasAnswer) {
        this.userImage = userImage;
        this.userName = userName;
        this.comment = comment;
        this.isUserLikeIt = isUserLikeIt;
        this.likeCount = likeCount;
        this.timeUserComment = time;
        this.isCommentHasAnswer = isCommentHasAnswer;
    }


    public static List<CommentModel> makeCommentsListToPostChild() {
        List<CommentModel> commentsListToPost = new ArrayList<>();
        String comment = "I love both but Coraline has a great book too so I voted for it.";
        String comment2 = "true life is such a masterpiece!";
        String comment3 = "LOL. The first couple of episodes of my Sweet 16 were so outrageous and I got hooked, but after about 4 episodes, it was all just the same. True Life was so much better.";

        commentsListToPost.add(new CommentModel(R.drawable.ic_person, "Даша", "12 ч. назад", comment,
                true, "3", false));
        commentsListToPost.add(new CommentModel(R.drawable.ic_person, "Маша", "12 ч. назад", comment2,
                false, "5", true));
        commentsListToPost.add(new CommentModel(R.drawable.ic_person, "Иван", "12 ч. назад", comment3,
                false, "20", true));
        commentsListToPost.add(new CommentModel(R.drawable.ic_person, "Иван", "12 ч. назад", comment2,
                false, "20", true));
        commentsListToPost.add(new CommentModel(R.drawable.ic_person, "Иван", "12 ч. назад", comment2,
                false, "20", true));

        return commentsListToPost;
    }

    public boolean isReadComment() {
        return isReadComment;
    }

    public String getTimeUserComment() {
        return timeUserComment;
    }

    public int getUserImage() {
        return userImage;
    }

    public String getUserName() {
        return userName;
    }

    public boolean isUserLikeIt() {
        return isUserLikeIt;
    }

    public boolean isCommentHasAnswer() {
        return isCommentHasAnswer;
    }
}
