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
    @SerializedName("weight")
    @Expose
    private String weight;

    private int postInCategory;

    public CategoryModel(String idCategory, String nameCategory, String weight, int postInCategory) {
        this.idCategory = idCategory;
        this.nameCategory = nameCategory;
        this.weight = weight;
        this.postInCategory = postInCategory;
    }


    public String getIdCategory() {
        return idCategory;
    }

    public String getNameCategory() {
        return nameCategory;
    }

    public String getWeight() {
        return weight;
    }

    public int getPostInCategory() {
        return postInCategory;
    }

    public void setPostInCategory(int postInCategory) {
        this.postInCategory = postInCategory;
    }

    @Override
    public String toString() {
        return "Category id: " + getIdCategory() + " name: " + getNameCategory();
    }
}
