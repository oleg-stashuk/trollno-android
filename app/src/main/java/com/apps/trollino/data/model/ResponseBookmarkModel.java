package com.apps.trollino.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseBookmarkModel {
    @SerializedName("global")
    @Expose
    private List<GlobalBookmark> globalBookmarkList;

    public List<GlobalBookmark> getGlobalBookmarkList() {
        return globalBookmarkList;
    }

    public class GlobalBookmark {
        @SerializedName("value")
        @Expose
        private boolean valueGlobalBookmark;

        public boolean isValueGlobalBookmark() {
            return valueGlobalBookmark;
        }
    }

}
