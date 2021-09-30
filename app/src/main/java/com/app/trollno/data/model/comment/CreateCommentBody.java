package com.app.trollno.data.model.comment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateCommentBody {
    @SerializedName("value")
    @Expose
    private String commentText;

    public CreateCommentBody(String commentText) {
        this.commentText = commentText;
    }

    public String getCommentText() {
        return commentText;
    }
}
