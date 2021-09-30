package com.app.trollno.data.model.profile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RequestUpdateUserPassword {
    @SerializedName("pass")
    @Expose
    private List<UpdatePasswordModel> updatePasswordModelList;

    public RequestUpdateUserPassword(List<UpdatePasswordModel> updatePasswordModelList) {
        this.updatePasswordModelList = updatePasswordModelList;
    }

    public static class UpdatePasswordModel {
        @SerializedName("existing")
        @Expose
        private String existingPass;
        @SerializedName("value")
        @Expose
        private String newPassValue;

        public UpdatePasswordModel(String existingPass, String newPassValue) {
            this.existingPass = existingPass;
            this.newPassValue = newPassValue;
        }
    }

}
