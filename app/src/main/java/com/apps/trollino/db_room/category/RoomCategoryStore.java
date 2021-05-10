package com.apps.trollino.db_room.category;

import android.content.Context;

import androidx.room.Room;

import com.apps.trollino.data.model.CategoryModel;
import com.apps.trollino.db_room.category.room.CategoryDao;
import com.apps.trollino.db_room.category.room.CategoryDataBase;
import com.apps.trollino.db_room.category.room.CategoryEntity;

import java.util.ArrayList;
import java.util.List;

public class RoomCategoryStore implements CategoryStore{

    private CategoryDao categoryDao;

    public RoomCategoryStore(Context context) {
        categoryDao = Room
                .databaseBuilder(context, CategoryDataBase.class, "category-database.sqlite")
                .allowMainThreadQueries()
                .build()
                .categoryDao();
    }

    @Override
    public List<CategoryModel> getCategoryList() {
        List<CategoryEntity> categoryEntityList = categoryDao.getCategoryListFromDB();
        List<CategoryModel> categoryModelList = new ArrayList<>();

        for(CategoryEntity categoryEntity : categoryEntityList) {
            categoryModelList.add(CategoryConverter.categoryConverter(categoryEntity));
        }
        return categoryModelList;
    }

    @Override
    public void addCategoryToList(List<CategoryModel> categoryList) {
        for (CategoryModel category : categoryList) {
            categoryDao.add(CategoryConverter.categoryConverter(category));
        }
    }

    @Override
    public void removeAllCategory() {
        categoryDao.clearCategoryDB();
    }

    @Override
    public void updateCategory(CategoryModel category) {
        categoryDao.updateCategoryData(CategoryConverter.categoryConverter(category));
    }
}
