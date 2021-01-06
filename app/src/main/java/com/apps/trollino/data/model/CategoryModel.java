package com.apps.trollino.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryModel {
    @SerializedName("tid")
    @Expose
    private String idCategory;
    @SerializedName("name")
    @Expose
    private String nameCategory;

    public String getIdCategory() {
        return idCategory;
    }

    public String getNameCategory() {
        return nameCategory;
    }

    @Override
    public String toString() {
        return "Category id: " + getIdCategory() + " name: " + getNameCategory();
    }
}
