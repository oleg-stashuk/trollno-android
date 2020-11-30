package com.apps.trollino.utils.castom_converter;

import android.util.Log;

import com.apps.trollino.data.model.ItemPostModel;
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
        JsonArray mediaBlock = items.getAsJsonArray("field_mediablocks");


        // Для поля объекта nextPost -> Список -> id категории и публичный
        JsonObject nextPostJsonObject = items.getAsJsonObject("next_node");
        ItemPostModel.NeighboringPost nextPost = idNeighboringPost(nextPostJsonObject, context);

        // Для поля объекта prevPost -> Список -> id категории и публичный
        JsonObject prevPostJsonObject = items.getAsJsonObject("prev_node");
        ItemPostModel.NeighboringPost prevPost = idNeighboringPost(prevPostJsonObject, context);

        Log.d("OkHttp", "!!!!!!!!!!!!!!!!! items " + items);
        return new ItemPostModel(postId, title, body, banner, category, comment, nextPost, prevPost);
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




//        ObjectMapper mapper = new ObjectMapper();
//        idPostNextByCategory.add(categoryArrayNext.get(0));

//        List<ItemPostModel.CategoryPost> getCategory = null;
//        List<ItemPostModel.CategoryPost> getCategory2 = null;
//        ItemPostModel.NeighboringPost nextPost222 = null;
//        ItemPostModel.NeighboringPost prevPost222 = null;

//        try {
//            nextPost222 = mapper.readValue(nextPost.toString(), ItemPostModel.NeighboringPost.class);
//            prevPost222 = mapper.readValue(prevPost.toString(), ItemPostModel.NeighboringPost.class);
//
//            getCategory = nextPost222.getCategory();
//            getCategory2 = nextPost222.getPubl();
//
//            String str = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(nextPost222);
//            Log.d("OkHttp", "!!!!!!!!!!!!!!!!! str " + str + " " + nextPost222.getCategory().size());
//            Log.d("OkHttp", "!!!!!!!!!!!!!!!!! nextPost222 " + nextPost222.toString());
//            Log.d("OkHttp", "!!!!!!!!!!!!!!!!! prevPost222 " + prevPost222.toString());
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//            Log.d("OkHttp", "!!!!!!!!!!!!!!!!! catch " + e.getLocalizedMessage());
//        }





    /*
      @Override
    public UserModel deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject items = json.getAsJsonObject();

        String uid = String.valueOf(items.getAsJsonPrimitive("uid")).replaceAll("[^0-9]", "");
        String loginName = String.valueOf(items.getAsJsonPrimitive("name"));
        String mail = String.valueOf(items.getAsJsonPrimitive("mail"));
        JsonObject userRolesJsonObject = items.getAsJsonObject("roles");

        Map<Integer, String> userRoles = new HashMap<>();
        Set<Map.Entry<String, JsonElement>> set = userRolesJsonObject.entrySet();
        Iterator<Map.Entry<String, JsonElement>> iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, JsonElement> entry = iterator.next();
            Integer key = Integer.parseInt(entry.getKey());
            String value = String.valueOf(entry.getValue());
            if (value != null) {
                userRoles.put(key, value);
            }
        }

        List<UserModel.Address> addressList = new ArrayList<>();
        try {
            JsonObject addressesJsonObject = items.getAsJsonObject("field_user_addresses");
            JsonArray addressJsonArray = addressesJsonObject.getAsJsonArray("und");
            addressJsonArray.forEach(address -> {
                String idAddress = String.valueOf(addressesJsonObject.getAsJsonPrimitive("target_id"));
                Log.d("OkHttp", "WE HAVE ADDRESS - uid: " + idAddress + " " + addressesJsonObject.size());
                addressList.add(new UserModel.Address(idAddress));
            });
        } catch (Exception e){
            addressList.add(null);
        }

        UserModel.UndAddress addresses = new UserModel.UndAddress(addressList);

        return new UserModel(uid, loginName, mail, userRoles, addresses);
    }
     */
}
