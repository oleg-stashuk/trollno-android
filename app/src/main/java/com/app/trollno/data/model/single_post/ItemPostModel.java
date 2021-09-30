package com.app.trollno.data.model.single_post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ItemPostModel {
    @SerializedName("nid")
    @Expose
    private List<IdPost> postId;
    @SerializedName("title")
    @Expose
    private List<TitlePost> title;
    @SerializedName("body")
    @Expose
    private List<BodyPost> body;
    @SerializedName("field_banner")
    @Expose
    private List<BannerPost> banner;
    @SerializedName("field_category")
    @Expose
    private List<CategoryPost> category;
    @SerializedName("field_comment")
    @Expose
    private List<CommentPost> comment;
    @SerializedName("field_mediablocks")
    @Expose
    private List<MediaBlock> mediaBlock;
    @SerializedName("next_node")
    @Expose
    private NeighboringPost nextPost;
    @SerializedName("prev_node")
    @Expose
    private NeighboringPost prevPost;
    @SerializedName("bookmarked")
    @Expose
    private boolean isFavorite;
    @SerializedName("read_by_user")
    @Expose
    private boolean IsReadByUser;

    public ItemPostModel(List<IdPost> postId, List<TitlePost> title, List<BodyPost> body,
                         List<BannerPost> banner, List<CategoryPost> category, List<CommentPost> comment,
                         List<MediaBlock> mediaBlock, NeighboringPost nextPost, NeighboringPost prevPost, boolean isFavorite, boolean IsReadByUser) {
        this.postId = postId;
        this.title = title;
        this.body = body;
        this.banner = banner;
        this.category = category;
        this.comment = comment;
        this.mediaBlock = mediaBlock;
        this.nextPost = nextPost;
        this.prevPost = prevPost;
        this.isFavorite = isFavorite;
        this.IsReadByUser = IsReadByUser;
    }


    public List<IdPost> getPostId() {
        return postId;
    }

    public List<TitlePost> getTitle() {
        return title;
    }

    public List<BodyPost> getBody() {
        return body;
    }

    public List<BannerPost> getBanner() {
        return banner;
    }

    public List<CategoryPost> getCategory() {
        return category;
    }

    public List<CommentPost> getComment() {
        return comment;
    }

    public List<MediaBlock> getMediaBlock() {
        return mediaBlock;
    }

    public NeighboringPost getNextPost() {
        return nextPost;
    }

    public NeighboringPost getPrevPost() {
        return prevPost;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public boolean isReadByUser() {
        return IsReadByUser;
    }

    public static class IdPost {
        @SerializedName("value")
        @Expose
        private String idPost;

        public IdPost(String idPost) {
            this.idPost = idPost;
        }

        public String getIdPost() {
            return idPost;
        }
    }

    public static class TitlePost {
        @SerializedName("value")
        @Expose
        private String title;

        public TitlePost(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }
    }

    public static class BodyPost {
        @SerializedName("value")
        @Expose
        private String textPostBody;

        public BodyPost(String textPostBody) {
            this.textPostBody = textPostBody;
        }

        public String getTextPostBody() {
            return textPostBody;
        }
    }

    public static class BannerPost {
        @SerializedName("target_id")
        @Expose
        private int idBanner;
        @SerializedName("title")
        @Expose
        private String titleBanner;
        @SerializedName("url")
        @Expose
        private String urlBanner;

        public BannerPost(String urlBanner) {
            this.urlBanner = urlBanner;
        }

        public int getIdBanner() {
            return idBanner;
        }

        public String getTitleBanner() {
            return titleBanner;
        }

        public String getUrlBanner() {
            return urlBanner;
        }
    }

    public static class CategoryPost {
        @SerializedName("target_id")
        @Expose
        private int idCategory;

        public CategoryPost(int idCategory) {
            this.idCategory = idCategory;
        }

        public int getIdCategory() {
            return idCategory;
        }
    }

    public static class CommentPost {
        @SerializedName("comment_count")
        @Expose
        private int commentCont;

        public CommentPost(int commentCont) {
            this.commentCont = commentCont;
        }

        public int getCommentCont() {
            return commentCont;
        }
    }

    public static class MediaBlock {
        @SerializedName("target_id")
        @Expose
        private int idMediaBlock;
        @SerializedName("entity")
        @Expose
        private EntityMediaBlock entity;

        public MediaBlock(int idMediaBlock, EntityMediaBlock entity) {
            this.idMediaBlock = idMediaBlock;
            this.entity = entity;
        }

        public int getIdMediaBlock() {
            return idMediaBlock;
        }

        public EntityMediaBlock getEntity() {
            return entity;
        }
    }

    public static class EntityMediaBlock {
        @SerializedName("field_block_image")
        @Expose
        private ImageBlock image;
        @SerializedName("field_app_title")
        @Expose
        private String title;
        @SerializedName("field_block_youtube")
        @Expose
        private String youtube;
        @SerializedName("field_block_desc")
        @Expose
        private String desc;

        public EntityMediaBlock(ImageBlock image, String title, String youtube, String desc) {
            this.image = image;
            this.title = title;
            this.youtube = youtube;
            this.desc = desc;
        }


        public ImageBlock getImage() {
            return image;
        }

        public String getTitle() {
            return title;
        }

        public String getYoutube() {
            return youtube;
        }

        public String getDesc() {
            return desc;
        }
    }

    public static class ImageBlock {
        @SerializedName("url")
        @Expose
        private String urlImage;
        @SerializedName("title")
        @Expose
        private String resourceTitle;
        @SerializedName("alt")
        @Expose
        private String resource;

        public ImageBlock(String urlImage, String resourceTitle, String resource) {
            this.urlImage = urlImage;
            this.resourceTitle = resourceTitle;
            this.resource = resource;
        }

        public String getUrlImage() {
            return urlImage;
        }

        public String getResourceTitle() {
            return resourceTitle;
        }

        public String getResource() {
            return resource;
        }
    }

    public static class NeighboringPost {
        @SerializedName("category")
        @Expose
        private List<IdNeighboringPost> category;
        @SerializedName("publ")
        @Expose
        private List<IdNeighboringPost> publ;


        public NeighboringPost(List<IdNeighboringPost> category, List<IdNeighboringPost> publ) {
            this.category = category;
            this.publ = publ;
        }

        public List<IdNeighboringPost> getCategory() {
            return category;
        }

        public List<IdNeighboringPost> getPubl() {
            return publ;
        }
    }

    public static class IdNeighboringPost {
        @SerializedName("target_id")
        @Expose
        private int idPost;

        public IdNeighboringPost(int idPost) {
            this.idPost = idPost;
        }

        public int getIdPost() {
            return idPost;
        }
    }

}
