package com.apps.trollino.utils.data;

import com.apps.trollino.data.model.CategoryModel;

import java.util.ArrayList;
import java.util.List;

public class CategoryListFromApi {
    private List<CategoryModel> categoryModelList;

    private static volatile CategoryListFromApi instance = null;
    public static CategoryListFromApi getInstance() {
        if(instance == null) {
            instance = new CategoryListFromApi();
        }
        return instance;
    }

    private CategoryListFromApi() {
        categoryModelList = new ArrayList<>();
    }

    public void saveCategoryList(List<CategoryModel> categoryList) {
        categoryModelList.clear();
        categoryModelList.addAll(categoryList);
    }

    public List<CategoryModel> getCategoryList() {
        return categoryModelList;
    }
}
