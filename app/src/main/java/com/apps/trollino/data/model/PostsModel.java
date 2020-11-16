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
    private Pager pager;

    public Pager getPager() {
        return pager;
    }

    public List<PostDetails> getPostDetailsList() {
        return postDetailsList;
    }


    public class PostDetails {
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

        public int getCommentActiveDiscus() {
            return commentActiveDiscus;
        }

        @Override
        public String toString() {
            return getPostId() + " " + getTitle() + " " + getCategoryId() + " " + getCategoryName() + " " + getCreated() + " " +
                    getCommentCount() + " " + getImageUrl() + " " + getFavorite() + " " + getRead() + " " + getCommentActiveDiscus();
        }
    }

    public class Pager {
        @SerializedName("current_page")
        @Expose
        private int currentPage;
        @SerializedName("total_pages")
        @Expose
        private int totalPages;
        @SerializedName("total_items")
        @Expose
        private int totalItems;
        @SerializedName("items_per_page")
        @Expose
        private int itemPerPage;
    }
}
