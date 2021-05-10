package com.apps.trollino.db_room.category;

import com.apps.trollino.data.model.CategoryModel;
import com.apps.trollino.db_room.category.room.CategoryEntity;

public class CategoryConverter {

    // Конвертировать объект CategoryEntity в CategoryModel
    static CategoryModel categoryConverter(CategoryEntity categoryEntity) {
      return new CategoryModel(
              categoryEntity.idCategory,
              categoryEntity.nameCategory,
              categoryEntity.weightCategory,
              categoryEntity.savedPostPosition);
    }

    // Конвертировать объект CategoryModel в CategoryEntity
    static CategoryEntity categoryConverter(CategoryModel categoryModel) {
        CategoryEntity categoryEntity = new CategoryEntity();

        categoryEntity.idCategory = categoryModel.getIdCategory();
        categoryEntity.nameCategory = categoryModel.getNameCategory();
        categoryEntity.weightCategory = categoryModel.getWeight();
        categoryEntity.savedPostPosition = categoryModel.getPostInCategory();

        return categoryEntity;
    }
}
