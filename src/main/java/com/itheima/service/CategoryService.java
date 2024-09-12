package com.itheima.service;

import com.itheima.pojo.Category;

import java.util.List;

public interface CategoryService {
    void addCategory(Category category);

    List<Category> selectCategoryByCreateUser(Integer id);

    Category selectCategoryById(Integer id);

    void updateCategory(Category category);

    void deleteCategory(Integer id);
}
