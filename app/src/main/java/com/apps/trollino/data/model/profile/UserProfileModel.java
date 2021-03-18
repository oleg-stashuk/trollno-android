package com.apps.trollino.data.model.profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserProfileModel {
    @SerializedName("uid")
    @Expose
    private List<UserUidData> uidList;
    @SerializedName("name")
    @Expose
    private List<UserData> nameList;
    @SerializedName("mail")
    @Expose
    private List<UserData> mailList;
    @SerializedName("field_user_picture")
    @Expose
    private List<UserImage> userImageList;

    @SerializedName("field_show_read_posts")
    @Expose
    private List<UserBooleanData> userShowReadPostList;
    @SerializedName("field_user_comment_answers")
    @Expose
    private List<UserBooleanData> userSendPushNewAnswerList;


    public List<UserUidData> getUidList() {
        return uidList;
    }

    public List<UserData> getNameList() {
        return nameList;
    }

    public List<UserData> getMailList() {
        return mailList;
    }

    public List<UserImage> getUserImageList() {
        return userImageList;
    }

    public List<UserBooleanData> getUserShowReadPostList() {
        return userShowReadPostList;
    }

    public List<UserBooleanData> getUserSendPushNewAnswerList() {
        return userSendPushNewAnswerList;
    }


    public class UserUidData {
        @SerializedName("value")
        @Expose
        private int value;

        public int getValue() {
            return value;
        }
    }

    public class UserData {
        @SerializedName("value")
        @Expose
        private String value;

        public String getValue() {
            return value;
        }
    }


    public class UserImage {
        @SerializedName("target_id")
        @Expose
        private int imageId;
        @SerializedName("url")
        @Expose
        private String imageUrl;

        public int getImageId() {
            return imageId;
        }

        public String getImageUrl() {
            return imageUrl;
        }
    }

    public static class UserBooleanData {
        @SerializedName("value")
        @Expose
        private boolean value;

        public UserBooleanData(boolean value) {
            this.value = value;
        }

        public boolean isValue() {
            return value;
        }
    }
}
