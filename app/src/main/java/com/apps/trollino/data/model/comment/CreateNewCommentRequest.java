package com.apps.trollino.data.model.comment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CreateNewCommentRequest {
    @SerializedName("comment_type")
    @Expose
    private String commentType;
    @SerializedName("entity_id")
    @Expose
    private List<PostId> entityIdList;
    @SerializedName("entity_type")
    @Expose
    private List<EntityType> entityTypeList;
    @SerializedName("field_name")
    @Expose
    private List<FieldName> fieldNameList;
    @SerializedName("comment_body")
    @Expose
    private List<CreateCommentBody> commentBodyList;
    @SerializedName("pid")
    @Expose
    private List<ParentIdComment> parentCidList;

    public CreateNewCommentRequest(List<PostId> entityIdList, List<CreateCommentBody> commentBodyList, List<ParentIdComment> parentCidList) {
        List<EntityType> entityTypeList = new ArrayList<>();
        entityTypeList.add(new EntityType());

        List<FieldName> fieldNameList = new ArrayList<>();
        fieldNameList.add(new FieldName());

        this.commentType = "simple_comment";
        this.entityIdList = entityIdList;
        this.entityTypeList = entityTypeList;
        this.fieldNameList = fieldNameList;
        this.commentBodyList = commentBodyList;
        this.parentCidList = parentCidList;
    }

    public static class PostId {
        @SerializedName("target_id")
        @Expose
        private String postId;

        public PostId(String postId) {
            this.postId = postId;
        }
    }

    public class EntityType {
        @SerializedName("value")
        @Expose
        private String value;

        public EntityType() {
            this.value = "node";
        }
    }

    public class FieldName {
        @SerializedName("value")
        @Expose
        private String value;

        public FieldName() {
            this.value = "field_comment";
        }
    }

    public static class ParentIdComment {
        @SerializedName("target_id")
        @Expose
        private String parentCid;

        public ParentIdComment(String parentCid) {
            this.parentCid = parentCid;
        }
    }
}
