package com.apps.trollino.data.model.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RegistrationResponseModel {
    @SerializedName("uid")
    @Expose
    private List<UidUser> uidList;

    public List<UidUser> getUidList() {
        return uidList;
    }


    public class UidUser {
        @SerializedName("value")
        @Expose
        private int uid;

        public int getUid() {
            return uid;
        }
    }

    /*
    "{
    ""uid"": [ {  ""value"": 7 } ],
    ""name"": [ {   ""value"": ""qa-restuser""  } ],
     */
}
