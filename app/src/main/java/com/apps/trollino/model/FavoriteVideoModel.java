package com.apps.trollino.model;

import java.util.ArrayList;
import java.util.List;

public class FavoriteVideoModel {
    private String videoId;
    private String videoTitle;

    public FavoriteVideoModel(String videoId, String videoTitle) {
        this.videoId = videoId;
        this.videoTitle = videoTitle;
    }

    public String getVideoId() {
        return videoId;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public static List<FavoriteVideoModel> makeFavoriteVideoList() {
        List<FavoriteVideoModel> makeFavoriteVideos = new ArrayList<>();
        makeFavoriteVideos.add(new FavoriteVideoModel("1", "10 творческих личностей, которые освоили 3D-принтер, и теперь не могут нарадоваться"));
        makeFavoriteVideos.add(new FavoriteVideoModel("2","20 творческих личностей, которые освоили 3D-принтер, и теперь не могут нарадоваться"));
        makeFavoriteVideos.add(new FavoriteVideoModel("3","30 творческих личностей, которые освоили 3D-принтер, и теперь не могут нарадоваться"));
        makeFavoriteVideos.add(new FavoriteVideoModel("4", "40 творческих личностей, которые освоили 3D-принтер, и теперь не могут нарадоваться"));
        makeFavoriteVideos.add(new FavoriteVideoModel("5", "50 творческих личностей, которые освоили 3D-принтер, и теперь не могут нарадоваться"));

        return makeFavoriteVideos;
    }
}
