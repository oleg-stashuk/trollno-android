package com.apps.trollino.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TiktokModel {
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("html")
    @Expose
    private String video;
    @SerializedName("thumbnail_url")
    @Expose
    private String image;

    public String getTitle() {
        return title;
    }

    public String getVideo() {
        return video;
    }

    public String getImage() {
        return image;
    }
}
