package com.app.trollno.data.model.user_action;

import com.app.trollno.data.model.PagerModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AnswersModel {
    @SerializedName("rows")
    @Expose
    private List<Answers> answerList;
    @SerializedName("pager")
    @Expose
    private PagerModel pagerModel;


    public List<Answers> getAnswerList() {
        return answerList;
    }

    public PagerModel getPagerModel() {
        return pagerModel;
    }

    public class Answers {
        @SerializedName("cid")
        @Expose
        private String commentId;
        @SerializedName("nid")
        @Expose
        private String postId;
        @SerializedName("title")
        @Expose
        private String postTitle;
        @SerializedName("comment_body")
        @Expose
        private String commentBody;
        @SerializedName("created")
        @Expose
        private String time;
        @SerializedName("newsappm_comment_like_count")
        @Expose
        private String countLike;
        @SerializedName("newsappm_comment_answers_count")
        @Expose
        private String commentAnswersCount;
        @SerializedName("newsappm_comment_new_answers_count")
        @Expose
        private String commentNewAnswersCount;


        public String getCommentId() {
            return commentId;
        }

        public String getCommentBody() {
            return commentBody;
        }

        public String getCountLike() {
            return countLike;
        }

        public String getCommentAnswersCount() {
            return commentAnswersCount;
        }

        public String getTime() {
            return time;
        }

        public String getPostId() {
            return postId;
        }

        public String getPostTitle() {
            return postTitle;
        }

        public String getCommentNewAnswersCount() {
            return commentNewAnswersCount;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }

            if (obj.getClass() != this.getClass()) {
                return false;
            }

            final Answers other = (Answers) obj;
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
