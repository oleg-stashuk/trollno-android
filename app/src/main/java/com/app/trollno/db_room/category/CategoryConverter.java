package com.app.trollno.db_room.category;

import com.app.trollno.data.model.CategoryModel;
import com.app.trollno.db_room.category.room.CategoryEntity;

public class CategoryConverter {

    // Конвертировать объект CategoryEntity в CategoryModel
    static CategoryModel categoryConverter(CategoryEntity categoryEntity) {
      return new CategoryModel(
              categoryEntity.idCategory,
              categoryEntity.nameCategory,
              categoryEntity.weightCategory,
              categoryEntity.savedPostPosition,
              categoryEntity.currentPage,
              categoryEntity.totalPages,
              categoryEntity.totalItems);
    }

    // Конвертировать объект CategoryModel в CategoryEntity
    static CategoryEntity categoryConverter(CategoryModel categoryModel) {
        CategoryEntity categoryEntity = new CategoryEntity();

        categoryEntity.idCategory = categoryModel.getIdCategory();
        categoryEntity.nameCategory = categoryModel.getNameCategory();
        categoryEntity.weightCategory = categoryModel.getWeight();
        categoryEntity.savedPostPosition = categoryModel.getPostInCategory();
        categoryEntity.currentPage = categoryModel.getCurrentPage();
        categoryEntity.totalPages = categoryModel.getTotalPages();
        categoryEntity.totalItems = categoryModel.getTotalItems();

        return categoryEntity;
    }
}
