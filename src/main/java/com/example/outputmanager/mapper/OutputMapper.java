package com.example.outputmanager.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.outputmanager.domain.Output;

@Mapper
public interface OutputMapper {

    List<Output> selectAll();

    Output selectById(@Param("id") Integer id);

    void insert(Output output);

    void update(Output output);

    void delete(@Param("id") Integer id);

    // ★ここが重要：XMLの #{keyword} #{categoryId} #{userId} と一致させる
    List<Output> search(@Param("keyword") String keyword,
                        @Param("categoryId") Integer categoryId,
                        @Param("userId") Integer userId);

    int countByCategoryId(@Param("categoryId") Integer categoryId);

    List<Output> selectByIds(@Param("ids") List<Integer> ids);
    List<Output> selectByUserId(@Param("userId") Integer userId); 
}