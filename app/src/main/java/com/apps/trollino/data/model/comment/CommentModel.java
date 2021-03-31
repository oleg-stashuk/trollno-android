package com.apps.trollino.data.model.comment;

import com.apps.trollino.data.model.PagerModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
        @SerializedName("pid")
        @Expose
        private String parentId;
        @SerializedName("comment_body")
        @Expose
        private String commentBody;
        @SerializedName("changed")
        @Expose
        private String time;
        @SerializedName("flagged")
        @Expose
        private String favoriteFlag;
        @SerializedName("name")
        @Expose
        private String authorComment;
        @SerializedName("field_user_picture")
        @Expose
        private String urlUserImage;
        @SerializedName("newsappm_comment_answers_count")
        @Expose
        private String commentAnswersCount;
        @SerializedName("newsappm_comment_like_count")
        @Expose
        private String countLikeToComment;


        public String getCommentId() {
            return commentId;
        }

        public String getParentId() {
            return parentId;
        }

        public String getCommentBody() {
            return commentBody;
        }

        public String getFavoriteFlag() {
            return favoriteFlag;
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

        public String getCountLikeToComment() {
            return countLikeToComment;
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
}
