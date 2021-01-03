package com.apps.trollino.data.model.comment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CreateNewCommentResponse {
    @SerializedName("cid")
    @Expose
    private List<Cid> cidList;
    @SerializedName("comment_body")
    @Expose
    private List<CreateCommentBody> commentBodyList;

    public class Cid {
        @SerializedName("value")
        @Expose
        private int value;

        public int getValue() {
            return value;
        }
    }
}
