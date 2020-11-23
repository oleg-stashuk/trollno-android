package com.apps.trollino.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryModel {
    @SerializedName("tid")
    @Expose
    private String idCategory;
    @SerializedName("vid")
    @Expose
    private String categories;
    @SerializedName("name")
    @Expose
    private String nameCategory;

    public String getIdCategory() {
        return idCategory;
    }

    public String getCategories() {
        return categories;
    }

    public String getNameCategory() {
        return nameCategory;
    }

    @Override
    public String toString() {
        return "Category id: " + getIdCategory() + " vid: " + getCategories() + " name: " + getNameCategory();
    }
}
