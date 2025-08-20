package com.example.outputmanager.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    private final CategoryMapper categoryMapper;  // ← カテゴリのSQL呼び出し
    private final OutputMapper outputMapper;      // ← 使用中判定（削除時）

    @Override
    public List<Category> getAllCategories() {
        // ★ 統一：selectAll() ではなく findAll() を使用
        List<Category> list = categoryMapper.findAll();
        System.out.println("[DEBUG] categories size=" + (list == null ? "null" : list.size()));
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
        // 出力に紐づいていれば削除不可にする安全策
        if (outputMapper.countByCategoryId(id) > 0) {
            throw new IllegalStateException("このカテゴリは使用中のため削除できません");
        }
        categoryMapper.delete(id);
    }

    @Override
    public List<Category> findAll() {
        // 画面のセレクトや一覧用に共通利用
        return categoryMapper.findAll();
    }

    @Override
    public Map<Integer, String> nameMap() {
        // 詳細画面でカテゴリ名ラベルを出すための辞書
        return findAll().stream()
                .collect(Collectors.toMap(Category::getId, Category::getName));
    }
}