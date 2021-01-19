package com.apps.trollino.data.model.single_post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MarkPostAsReadModel {
    @SerializedName("flag_id")
    @Expose
    private String flagId;
    @SerializedName("entity_type")
    @Expose
    private String entityType;
    @SerializedName("entity_id")
    @Expose
    private String postId;

    public MarkPostAsReadModel(String postId) {
        this.flagId = "read";
        this.entityType = "node";
        this.postId = postId;
    }

}
