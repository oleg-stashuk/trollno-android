package com.app.trollno.data.model;

import com.app.trollno.data.model.profile.AvatarImageModel;
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
    @SerializedName("field_conf_admob_id")
    @Expose
    private List<KeyModel> adMobIdList;
    @SerializedName("field_conf_banner_id")
    @Expose
    private List<KeyModel> bannerIdList;
    @SerializedName("field_conf_youtube_key")
    @Expose
    private List<KeyModel> youtubeKeyList;

    public List<AvatarImageModel> getAvatarImageList() {
        return avatarImageList;
    }

    public List<AdvertisingModel> getAdvertisingList() {
        return advertisingList;
    }

    public List<KeyModel> getAdMobIdList() {
        return adMobIdList;
    }

    public List<KeyModel> getBannerIdList() {
        return bannerIdList;
    }

    public List<KeyModel> getYoutubeKeyList() {
        return youtubeKeyList;
    }


    public class AdvertisingModel {
        @SerializedName("value")
        @Expose
        private int value;

        public int getValue() {
            return value;
        }
    }

    public class KeyModel {
        @SerializedName("value")
        @Expose
        private String value;

        public String getValue() {
            return value;
        }
    }

}
