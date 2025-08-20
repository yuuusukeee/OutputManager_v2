package com.example.outputmanager.service;

import java.util.List;
import java.util.Map;

import com.example.outputmanager.domain.Category;

public interface CategoryService {
    List<Category> getAllCategories();
    Category getCategoryById(Integer id);
    void addCategory(Category category);
    void editCategory(Category category);
    void deleteCategory(Integer id);
    List<Category> findAll();         // 一覧取得
    Map<Integer, String> nameMap();   // 画面用：ID→名前の辞書
}
