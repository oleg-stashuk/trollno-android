package com.apps.trollino.utils.castom_converter;

import android.util.Log;

import com.apps.trollino.data.model.ItemPostModel;
import com.fasterxml.jackson.databind.ObjectMapper;
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

//        int postId = Integer.getInteger(String.valueOf(items.getAsJsonPrimitive("target_id")));

        ObjectMapper mapper = new ObjectMapper();



        JsonArray postId = items.getAsJsonArray("nid");
        JsonArray title = items.getAsJsonArray("title");
        JsonArray body = items.getAsJsonArray("body");
        JsonArray banner = items.getAsJsonArray("field_banner");
        JsonArray category = items.getAsJsonArray("field_category");
        JsonArray comment = items.getAsJsonArray("field_comment");
        JsonArray mediaBlock = items.getAsJsonArray("field_mediablocks");

        JsonObject nextPost = items.getAsJsonObject("next_node");
        JsonArray categoryArrayNext = nextPost.getAsJsonArray("category");
        JsonElement element = categoryArrayNext.get(0);
//        String dddddd = String.valueOf(element.getAsJsonPrimitive());


        List<ItemPostModel.IdNeighboringPost> idPostNextByCategory = new ArrayList<>();
        for(int i = 0; i < categoryArrayNext.size(); i++) {

        }
//        idPostNextByCategory.add(categoryArrayNext.get(0));

//        try {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                categoryArrayNext.forEach(jsonElement -> {
//                    postIdCategory =  String.valueOf(categoryArrayNext.getAsJsonPrimitive("target_id"));
//                });
//            }
//
//            addressJsonArray.forEach(address -> {
//                String idAddress = String.valueOf(addressesJsonObject.getAsJsonPrimitive("target_id"));
//                Log.d("OkHttp", "WE HAVE ADDRESS - uid: " + idAddress + " " + addressesJsonObject.size());
//                addressList.add(new UserModel.Address(idAddress));
//            });
//        } catch (Exception e){
//            addressList.add(null);
//        }

        JsonArray publArrayNext = nextPost.getAsJsonArray("publ");



//        @SerializedName("target_id")
//        @Expose
//        private int idPost;


        JsonObject prevPost = items.getAsJsonObject("prev_node");
//        ItemPostModel.NeighboringPost nextPost = items.getAsJsonObject("next_node");



//        @SerializedName("category")
//        @Expose
//        private List<CategoryPost> category; // если пост открыт с
//        @SerializedName("publ")
//        @Expose
//        private List<CategoryPost> publ;


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

        Log.d("OkHttp", "!!!!!!!!!!!!!!!!! items " + items);
        Log.d("OkHttp", "!!!!!!!!!!!!!!!!! postId " + postId);
        Log.d("OkHttp", "!!!!!!!!!!!!!!!!! nextPost " + nextPost);
        Log.d("OkHttp", "!!!!!!!!!!!!!!!!! categoryArrayNext " + categoryArrayNext);
        Log.d("OkHttp", "!!!!!!!!!!!!!!!!! publArrayNext " + publArrayNext + " " + publArrayNext.size());
        Log.d("OkHttp", "!!!!!!!!!!!!!!!!! element " + element);
//        Log.d("OkHttp", "!!!!!!!!!!!!!!!!! dddddd " + dddddd);




//        Log.d("OkHttp", "!!!!!!!!!!!!!!!!! prevPost " + prevPost);


        return new ItemPostModel();
    }



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
