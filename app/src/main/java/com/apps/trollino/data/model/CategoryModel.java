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
    private int currentPage;
    private int totalPages;
    private int totalItems;

    public CategoryModel(String idCategory, String nameCategory, String weight, int postInCategory,
                         int currentPage, int totalPages, int totalItems) {
        this.idCategory = idCategory;
        this.nameCategory = nameCategory;
        this.weight = weight;
        this.postInCategory = postInCategory;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalItems = totalItems;
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

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }
}
