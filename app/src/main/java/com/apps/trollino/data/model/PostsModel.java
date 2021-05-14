package com.apps.trollino.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PostsModel {
    @SerializedName("rows")
    @Expose
    private List<PostDetails> postDetailsList;
    @SerializedName("pager")
    @Expose
    private PagerModel pagerModel;

    public PagerModel getPagerModel() {
        return pagerModel;
    }

    public List<PostDetails> getPostDetailsList() {
        return postDetailsList;
    }


    public static class PostDetails {
        @SerializedName("nid")
        @Expose
        private String postId;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("body")
        @Expose
        private String body;
        @SerializedName("field_category")
        @Expose
        private String categoryId;
        @SerializedName("field_category_1")
        @Expose
        private String categoryName;
        @SerializedName("created")
        @Expose
        private String created;
        @SerializedName("comment_count")
        @Expose
        private String commentCount;
        @SerializedName("field_banner")
        @Expose
        private String imageUrl;
        @SerializedName("flagged")
        @Expose
        private int favorite; // 0 - post not favorite, 1 - post added to favorite. For unverified user is always 0
        @SerializedName("flagged_1")
        @Expose
        private int read; // 0 - post did not read, 1 - post read. For unverified user is always 0
        @SerializedName("newsappm_node_active_discus")
        @Expose
        private int commentActiveDiscus; // 0 - post did not read, 1 - post read. For unverified user is always 0

        public PostDetails(String postId, String title, String categoryId, String categoryName,
                           String imageUrl, boolean isRead, boolean isCommentActiveDiscus) {
            this.postId = postId;
            this.title = title;
            this.categoryId = categoryId;
            this.categoryName = categoryName;
            this.imageUrl = imageUrl;
            this.read = isRead ? 1 : 0; // 0 - пост не прочитан, 1 - пост прочитан. Для неверифицированных пользователей всегда 0
            this.commentActiveDiscus = isCommentActiveDiscus ? 1 : 0; // 0 не в осуждаемом, 1 - в обсуждаемом
        }

        public String getPostId() {
            return postId;
        }

        public String getTitle() {
            return title;
        }

        public String getBody() {
            return body;
        }

        public String getCategoryId() {
            return categoryId;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public String getCreated() {
            return created;
        }

        public String getCommentCount() {
            return commentCount;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public int getFavorite() {
            return favorite;
        }

        public int getRead() {
            return read;
        }

        public void setRead(boolean isReadPost) {
            // 0 - пост не прочитан, 1 - постпрочитан. Для неверифицированных пользователей всегда 0
            this.read = isReadPost ? 1 : 0;
        }

        public int getCommentActiveDiscus() {
            return commentActiveDiscus;
        }

        @Override
        public String toString() {
            return getPostId() + " " + getTitle() + " " + getCategoryId() + " " + getCategoryName()
                    + " " + getCreated() + " " + getCommentCount() + " " + getImageUrl() + " " +
                    getFavorite() + " " + getRead() + " " + getCommentActiveDiscus();
        }


        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }

            if (obj.getClass() != this.getClass()) {
                return false;
            }

            final PostDetails other = (PostDetails) obj;
            if ((this.postId == null) ? (other.postId != null) : !this.postId.equals(other.postId)) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 53 * hash + (this.postId != null ? this.postId.hashCode() : 0);
            return hash;
        }
    }

}
