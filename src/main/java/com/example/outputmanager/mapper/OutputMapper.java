package com.example.outputmanager.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.outputmanager.domain.Output;

@Mapper
public interface OutputMapper {

    // 一覧（全件）
    List<Output> selectAll();

    // 主キー検索（従来：int）
    Output selectById(@Param("id") int id);

    // ★ 主キー検索（Long統一版）
    Output findById(@Param("id") Long id);

    // 自分の一覧
    List<Output> selectByUserId(@Param("userId") int userId);

    // 検索（Implの呼び出し順に合わせる：keyword, categoryId, userId）
    List<Output> search(
        @Param("keyword") String keyword,
        @Param("categoryId") Integer categoryId,
        @Param("userId") Integer userId
    );

    // INSERT/UPDATE/DELETE
    int insert(Output output);
    int update(Output output);
    int delete(@Param("id") int id);

    // カテゴリ使用数
    int countByCategoryId(@Param("categoryId") int categoryId);

    // IDリストで取得
    List<Output> selectByIds(@Param("ids") List<Long> ids);

    // ユーザー×カテゴリ名（お気に入り除外）
    List<Output> selectByUserAndCategoryNameExcludeFav(
        @Param("userId") int userId,
        @Param("categoryName") String categoryName
    );
}