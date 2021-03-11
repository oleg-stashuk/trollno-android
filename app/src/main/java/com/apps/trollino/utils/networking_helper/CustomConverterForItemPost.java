package com.apps.trollino.utils.networking_helper;

import com.apps.trollino.data.model.single_post.ItemPostModel;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CustomConverterForItemPost implements JsonDeserializer<ItemPostModel> {

    @Override
    public ItemPostModel deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject items = json.getAsJsonObject();

        // Для поля объекта postId
        JsonArray postIdJsonArray = items.getAsJsonArray("nid");
        List<ItemPostModel.IdPost> postId = new ArrayList<>();
        try {
            for (JsonElement e : postIdJsonArray) {
                postId.add(context.deserialize(e, ItemPostModel.IdPost.class));
            }
        } catch (Exception e) {
            ItemPostModel.IdPost idNull = new ItemPostModel.IdPost("");
            postId.add(idNull);
        }

        // Для поля объекта title
        JsonArray titleJsonArray = items.getAsJsonArray("title");
        List<ItemPostModel.TitlePost> title = new ArrayList<>();
        try{
            for (JsonElement e : titleJsonArray) {
                title.add(context.deserialize(e, ItemPostModel.TitlePost.class));
            }
        } catch (Exception e) {
            ItemPostModel.TitlePost titleNull = new ItemPostModel.TitlePost("");
            title.add(titleNull);
        }

        // Для поля объекта body
        JsonArray bodyJsonArray = items.getAsJsonArray("body");
        List<ItemPostModel.BodyPost> body = new ArrayList<>();
        try{
            for (JsonElement e : bodyJsonArray) {
                body.add(context.deserialize(e, ItemPostModel.BodyPost.class));
            }
        } catch (Exception e) {
            ItemPostModel.BodyPost bodyNull = new ItemPostModel.BodyPost("");
            body.add(bodyNull);
        }

        // Для поля объекта banner
        JsonArray bannerJsonArray = items.getAsJsonArray("field_banner");
        List<ItemPostModel.BannerPost> banner = new ArrayList<>();
        try{
            for (JsonElement e : bannerJsonArray) {
                banner.add(context.deserialize(e, ItemPostModel.BannerPost.class));
            }
        } catch (Exception e) {
            ItemPostModel.BannerPost bannerNull = new ItemPostModel.BannerPost("");
            banner.add(bannerNull);
        }

        // Для поля объекта category
        JsonArray categoryJsonArray = items.getAsJsonArray("field_category");
        List<ItemPostModel.CategoryPost> category = new ArrayList<>();
        try{
            for (JsonElement e : categoryJsonArray) {
                category.add(context.deserialize(e, ItemPostModel.CategoryPost.class));
            }
        } catch (Exception e) {
            ItemPostModel.CategoryPost categoryNull = new ItemPostModel.CategoryPost(0);
            category.add(categoryNull);
        }

        // Для поля объекта comment
        JsonArray commentJsonArray = items.getAsJsonArray("field_comment");
        List<ItemPostModel.CommentPost> comment = new ArrayList<>();
        try{
            for (JsonElement e : commentJsonArray) {
                comment.add(context.deserialize(e, ItemPostModel.CommentPost.class));
            }
        } catch (Exception e) {
            ItemPostModel.CommentPost commentNull = new ItemPostModel.CommentPost(0);
            comment.add(commentNull);
        }

        // Для поля объекта mediaBlock
        JsonArray mediaBlockJsonArray = items.getAsJsonArray("field_mediablocks");
        List<ItemPostModel.MediaBlock> mediaBlock = addItemInMediaBlockList(mediaBlockJsonArray, context);

        // Для поля объекта nextPost -> Список -> id категории и публичный
        JsonObject nextPostJsonObject = items.getAsJsonObject("next_node");
        ItemPostModel.NeighboringPost nextPost = idNeighboringPost(nextPostJsonObject, context);

        // Для поля объекта prevPost -> Список -> id категории и публичный
        JsonObject prevPostJsonObject = items.getAsJsonObject("prev_node");
        ItemPostModel.NeighboringPost prevPost = idNeighboringPost(prevPostJsonObject, context);

        // Для поля isFavorite
        JsonElement isFavoriteJsonElement = items.get("bookmarked");
        boolean isFavorite = isFavoriteJsonElement.getAsBoolean();

        // Для поля isRead
        JsonElement isReadJsonElement = items.get("read_by_user");
        boolean isRead = isReadJsonElement.getAsBoolean();

        return new ItemPostModel(postId, title, body, banner, category, comment, mediaBlock, nextPost, prevPost, isFavorite, isRead);
    }



    // Создание объекта IdNeighboringPost, который содержит в себе id соседних постов по category и publ
    private ItemPostModel.NeighboringPost idNeighboringPost(JsonObject jsonObject, JsonDeserializationContext context) {
        List<ItemPostModel.IdNeighboringPost> idPostNextByCategory = new ArrayList<>();
        List<ItemPostModel.IdNeighboringPost> idPostNextByPubl = new ArrayList<>();

        try {
            JsonArray categoryArrayNext = jsonObject.getAsJsonArray("category");
            idPostNextByCategory = addItemsInIdNeighboringPostList(categoryArrayNext, context);
        } catch (Exception e) {
            ItemPostModel.IdNeighboringPost postNull = new ItemPostModel.IdNeighboringPost(0);
            idPostNextByCategory.add(postNull);
        }

        try {
            JsonArray publArrayNext = jsonObject.getAsJsonArray("publ");
            idPostNextByPubl = addItemsInIdNeighboringPostList(publArrayNext, context);
        } catch (Exception e) {
            ItemPostModel.IdNeighboringPost postNull = new ItemPostModel.IdNeighboringPost(0);
            idPostNextByPubl.add(postNull);
        }

        return new ItemPostModel.NeighboringPost(idPostNextByCategory, idPostNextByPubl);
    }

    // Добавление элементов в список для объекта IdNeighboringPost
    private List<ItemPostModel.IdNeighboringPost> addItemsInIdNeighboringPostList(JsonArray jsonArray, JsonDeserializationContext context) {
        List<ItemPostModel.IdNeighboringPost> idNeighboringPostList = new ArrayList<>();
        for (JsonElement e : jsonArray) {
            idNeighboringPostList.add(context.deserialize(e, ItemPostModel.IdNeighboringPost.class));
        }
        return idNeighboringPostList;
    }

    // Добавление элементов в список для объекта mediaBlock
    private List<ItemPostModel.MediaBlock> addItemInMediaBlockList(JsonArray jsonArray, JsonDeserializationContext context) {
        List<ItemPostModel.MediaBlock> mediaBlock = new ArrayList<>();
        try{
            for (JsonElement e : jsonArray) {
                mediaBlock.add(context.deserialize(e, ItemPostModel.MediaBlock.class));
            }
        } catch (Exception e) {
            ItemPostModel.ImageBlock imageBlock = new ItemPostModel.ImageBlock("", "", "");

            ItemPostModel.EntityMediaBlock entity = new ItemPostModel.EntityMediaBlock(imageBlock,"", "", "" );
            ItemPostModel.MediaBlock mediaBlockNull = new ItemPostModel.MediaBlock(0, entity);
            mediaBlock.add(mediaBlockNull);
        }

        return checkedMediaBlockList(mediaBlock);
    }

    // Проверка на null всех полей в модели  MediaBlock
    private List<ItemPostModel.MediaBlock> checkedMediaBlockList(List<ItemPostModel.MediaBlock> mediaBlockList) {
        List<ItemPostModel.MediaBlock> checkedMediaBlockList = new ArrayList<>();

        for(ItemPostModel.MediaBlock mediaBlockItem : mediaBlockList) {
            int idMediaBlock = mediaBlockItem.getIdMediaBlock();

            ItemPostModel.ImageBlock imageBlock = checkedImageBlock(mediaBlockItem);

            String titleMediaBlock = checkedString(mediaBlockItem.getEntity().getTitle());
            String youtubeMediaBlock = checkedString(mediaBlockItem.getEntity().getYoutube());
            String descriprionMediaBlock = checkedString(mediaBlockItem.getEntity().getDesc());

            ItemPostModel.EntityMediaBlock entity = new ItemPostModel.EntityMediaBlock(
                    imageBlock, titleMediaBlock, youtubeMediaBlock,
                    descriprionMediaBlock);
            checkedMediaBlockList.add(new ItemPostModel.MediaBlock(idMediaBlock, entity));
        }

        return checkedMediaBlockList;
    }

    // Проверка на null всех полей в модели MediaBlock
    private ItemPostModel.ImageBlock checkedImageBlock(ItemPostModel.MediaBlock mediaBlockItem) {
        String urlImage;
        String resourceTitle;
        String resource;
        try {
            ItemPostModel.ImageBlock imageBlock = mediaBlockItem.getEntity().getImage();
            urlImage = checkedString(imageBlock.getUrlImage());
            resourceTitle = checkedString(imageBlock.getResourceTitle());
            resource = checkedString(imageBlock.getResource());
        } catch (Exception e) {
            urlImage = "";
            resourceTitle = "";
            resource = "";
        }

        return new ItemPostModel.ImageBlock(urlImage, resourceTitle, resource);
    }

    private String checkedString(String stringToCheck) {
        return stringToCheck == null ? "" : stringToCheck;
    }
}
