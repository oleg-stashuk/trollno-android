package com.app.trollno.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PagerModel {
    @SerializedName("current_page")
    @Expose
    private int currentPage;
    @SerializedName("total_pages")
    @Expose
    private int totalPages;
    @SerializedName("total_items")
    @Expose
    private int totalItems;
    @SerializedName("items_per_page")
    @Expose
    private int itemPerPage;

    public int getCurrentPage() {
        return currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public int getItemPerPage() {
        return itemPerPage;
    }

    @Override
    public String toString() {
        return "currentPage: " + getCurrentPage() + " total_pages: " + getTotalPages() +
                " totalItems: " + getTotalItems() + " itemPerPage: " +  getItemPerPage();
    }
}
