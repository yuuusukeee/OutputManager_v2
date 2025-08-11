package com.example.outputmanager.mapper;

import com.example.outputmanager.domain.Category;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface CategoryMapper {
    List<Category> selectAll();
    Category selectById(Integer id);
    void insert(Category category);
    void update(Category category);
    void delete(Integer id);

    int countByName(String name);
}
