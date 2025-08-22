package com.example.outputmanager.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.outputmanager.domain.Category;

@Mapper
public interface CategoryMapper {
    // 命名は findAll に統一
    List<Category> findAll();

    Category selectById(Integer id);

    void insert(Category category);

    void update(Category category);

    void delete(Integer id);

    int countByName(String name);
}