package com.apps.trollino.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SettingsModel {
    @SerializedName("field_avatars")
    @Expose
    private List<AvatarImageModel> avatarImageList;
    @SerializedName("field_conf_num_blocks_to_adv")
    @Expose
    private List<AdvertisingModel> advertisingList;

    public List<AvatarImageModel> getAvatarImageList() {
        return avatarImageList;
    }

    public List<AdvertisingModel> getAdvertisingList() {
        return advertisingList;
    }


    public class AvatarImageModel {
        @SerializedName("target_id")
        @Expose
        private String avatarId;

        @SerializedName("url")
        @Expose
        private String avatarUrl;


        public String getAvatarId() {
            return avatarId;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }
    }


    public class AdvertisingModel {
        @SerializedName("value")
        @Expose
        private int value;

        public int getValue() {
            return value;
        }
    }

}
