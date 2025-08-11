package com.example.outputmanager.service;

import com.example.outputmanager.domain.Category;
import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();
    Category getCategoryById(Integer id);
    void addCategory(Category category);
    void editCategory(Category category);
    void deleteCategory(Integer id);
}
