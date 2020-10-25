package com.apps.trollino.model;

import java.util.ArrayList;
import java.util.List;

public class FavoriteVideoModel {
    private String videoId;
    private String videoTitle;
    private int commentCount;

    public FavoriteVideoModel(String videoId, String videoTitle,int commentCount) {
        this.videoId = videoId;
        this.videoTitle = videoTitle;
        this.commentCount = commentCount;
    }

    public String getVideoId() {
        return videoId;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public static List<FavoriteVideoModel> makeFavoriteVideoList() {
        String title = "творческих личностей, которые освоили 3D-принтер, и теперь не могут нарадоваться";

        List<FavoriteVideoModel> makeFavoriteVideos = new ArrayList<>();
        makeFavoriteVideos.add(new FavoriteVideoModel("1", ("10" + title), 10));
        makeFavoriteVideos.add(new FavoriteVideoModel("2", ("20" + title), 15));
        makeFavoriteVideos.add(new FavoriteVideoModel("3", ("30" + title), 3));
        makeFavoriteVideos.add(new FavoriteVideoModel("4", ("40" + title), 100));
        makeFavoriteVideos.add(new FavoriteVideoModel("5", ("50" + title), 47));

        return makeFavoriteVideos;
    }

}
