package com.example.outputmanager.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.outputmanager.domain.Output;

@Mapper
public interface OutputMapper {

    // ---- 基本取得系 ----
    Output findById(@Param("id") Long id);

    List<Output> selectByUserId(@Param("userId") int userId);

    List<Output> search(
            @Param("keyword") String keyword,
            @Param("categoryId") Integer categoryId,
            @Param("userId") Integer userId
    );

    // ---- 変更系 ----
    int insert(Output output);

    int update(Output output);

    int delete(@Param("id") Long id);

    // ---- 画面要件対応 ----
    List<Output> findByUserAndCategoryExcludeFav(
            @Param("userId") int userId,
            @Param("categoryId") int categoryId
    );

    List<Output> findRecentByUser(
            @Param("userId") int userId,
            @Param("limit") int limit
    );

    int countByCategoryId(@Param("categoryId") int categoryId);
}