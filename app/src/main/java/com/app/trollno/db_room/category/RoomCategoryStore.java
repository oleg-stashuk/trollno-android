package com.app.trollno.db_room.category;

import android.content.Context;

import androidx.room.Room;

import com.app.trollno.data.model.CategoryModel;
import com.app.trollno.db_room.category.room.CategoryDao;
import com.app.trollno.db_room.category.room.CategoryDataBase;
import com.app.trollno.db_room.category.room.CategoryEntity;

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

    @Override
    public void updatePagesInCategory(String idCategory, int currentPage, int totalPages, int totalItems) {
        CategoryModel category = getCategoryById(idCategory);
        category.setCurrentPage(currentPage);
        category.setTotalPages(totalPages);
        category.setTotalItems(totalItems);
        updateCategory(category);
    }

    @Override
    public void updatePositionInCategory(String idCategory, int postPositionInCategory) {
        CategoryModel category = getCategoryById(idCategory);
        category.setPostInCategory(postPositionInCategory);
        updateCategory(category);
    }

    @Override
    public CategoryModel getCategoryById(String categoryId) {
        return CategoryConverter.categoryConverter(categoryDao.getCategoryById(categoryId));
    }

    @Override
    public int getNextPage(String categoryId, boolean isGetNewList) {
        if (isGetNewList) {
            return 0;
        } else {
            CategoryModel category = getCategoryById(categoryId);
            int currentPage = category.getCurrentPage(); // начинается с 0
            int totalPage = category.getTotalPages(); // начинается с 1
            return currentPage == totalPage - 1 ? currentPage : currentPage + 1;
        }
    }
}
