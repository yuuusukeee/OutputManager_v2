package com.example.outputmanager.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.outputmanager.domain.Category;
import com.example.outputmanager.mapper.CategoryMapper;
import com.example.outputmanager.mapper.OutputMapper;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;
    private final OutputMapper outputMapper;

    @Override
    public List<Category> getAllCategories() {
        // ← まず取得してからログ → 返却、の順
        List<Category> list = categoryMapper.selectAll();
        System.out.println("[DEBUG] mapper categories size=" + (list == null ? "null" : list.size()));
        return list;
    }

    @Override
    public Category getCategoryById(Integer id) {
        return categoryMapper.selectById(id);
    }

    @Override
    public void addCategory(Category category) {
        if (categoryMapper.countByName(category.getName()) > 0) {
            throw new IllegalArgumentException("同名のカテゴリが既に存在します");
        }
        categoryMapper.insert(category);
    }

    @Override
    public void editCategory(Category category) {
        categoryMapper.update(category);
    }

    @Override
    public void deleteCategory(Integer id) {
        if (outputMapper.countByCategoryId(id) > 0) {
            throw new IllegalStateException("このカテゴリは使用中のため削除できません");
        }
        categoryMapper.delete(id);
    }
}