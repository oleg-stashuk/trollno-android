package com.apps.trollino.db_room.category;

import com.apps.trollino.data.model.CategoryModel;

import java.util.List;

public interface CategoryStore {
    // Вывести весь список категорий
    List<CategoryModel> getCategoryList();

    // Добавить новый объект в список объектов типа CategoryModel
    void addCategoryToList(List<CategoryModel> category);

    // Удалить категории
    void removeAllCategory();

    // Обновить данные в категории
    void updateCategory(CategoryModel category);
}
