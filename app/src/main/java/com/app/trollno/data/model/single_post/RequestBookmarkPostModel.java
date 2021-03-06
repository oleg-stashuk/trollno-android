package com.app.trollno.data.model.single_post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestBookmarkPostModel {
    @SerializedName("flag_id")
    @Expose
    private String flagId;
    @SerializedName("entity_type")
    @Expose
    private String entityType;
    @SerializedName("entity_id")
    @Expose
    private String postId;

    public RequestBookmarkPostModel(String postId) {
        this.flagId = "bookmark";
        this.entityType = "node";
        this.postId = postId;
    }
}
