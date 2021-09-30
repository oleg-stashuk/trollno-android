package com.app.trollno.db_room.category;

import com.app.trollno.data.model.CategoryModel;

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

    // Обновить данные в категории по id категории
    void updatePagesInCategory(String idCategory, int currentPage, int totalPages, int totalItems);

    // Обновить позицию поста в категории
    void updatePositionInCategory(String idCategory, int postPositionInCategory);

    // Получить категорию с БД по id
    CategoryModel getCategoryById(String categoryId);

    // Вычислить следующую страницу для загрузки постов
    int getNextPage(String categoryId, boolean isGetNewList);
}
