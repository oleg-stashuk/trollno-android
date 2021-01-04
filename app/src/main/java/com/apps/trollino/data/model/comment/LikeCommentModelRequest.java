package com.apps.trollino.data.model.comment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LikeCommentModelRequest {
    @SerializedName("flag_id")
    @Expose
    private String flagId;
    @SerializedName("entity_type")
    @Expose
    private String entityType;
    @SerializedName("entity_id")
    @Expose
    private String entityCommentId;

    public LikeCommentModelRequest(String entityCommentId) {
        this.flagId = "like";
        this.entityType = "comment";
        this.entityCommentId = entityCommentId;
    }

}
