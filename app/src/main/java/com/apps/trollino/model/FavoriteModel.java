package com.apps.trollino.model;

import java.util.ArrayList;
import java.util.List;

public class FavoriteModel {
    private String videoId;
    private String videoTitle;
    private int commentCount;
    private boolean inDiscuss;

    public FavoriteModel(String videoId, String videoTitle, int commentCount, boolean inDiscuss) {
        this.videoId = videoId;
        this.videoTitle = videoTitle;
        this.commentCount = commentCount;
        this.inDiscuss = inDiscuss;
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

    public boolean isInDiscuss() {
        return inDiscuss;
    }




    public static List<FavoriteModel> makeFavoriteVideoList() {
        String title = "творческих личностей, которые освоили 3D-принтер, и теперь не могут нарадоваться";

        List<FavoriteModel> makeFavoriteVideos = new ArrayList<>();
        makeFavoriteVideos.add(new FavoriteModel("1", ("10 " + title), 10, true));
        makeFavoriteVideos.add(new FavoriteModel("2", ("20 " + title), 15, false));
        makeFavoriteVideos.add(new FavoriteModel("3", ("30 " + title), 3, false));
        makeFavoriteVideos.add(new FavoriteModel("4", ("40 " + title), 100, true));
        makeFavoriteVideos.add(new FavoriteModel("5", ("50 " + title), 47, false));
        makeFavoriteVideos.add(new FavoriteModel("6", ("60 " + title), 47, false));
        makeFavoriteVideos.add(new FavoriteModel("7", ("70 " + title), 47, false));
        makeFavoriteVideos.add(new FavoriteModel("8", ("80 " + title), 47, false));
        makeFavoriteVideos.add(new FavoriteModel("9", ("90 " + title), 47, false));
        makeFavoriteVideos.add(new FavoriteModel("10", ("100 " + title), 47, false));
        makeFavoriteVideos.add(new FavoriteModel("11", ("110 " + title), 47, false));


        return makeFavoriteVideos;
    }

}
