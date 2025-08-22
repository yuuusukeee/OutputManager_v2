package com.example.outputmanager.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.outputmanager.domain.Output;

@Mapper
public interface OutputMapper {

    // --- 主キー検索（最終形は Long を採用） ---
    Output findById(@Param("id") Long id);

    // --- 自分の一覧（ユーザー別） ---
    List<Output> selectByUserId(@Param("userId") int userId);

    // --- 検索（タイトルのみ / カテゴリ任意 / ユーザー必須） ---
    List<Output> search(
            @Param("keyword") String keyword,
            @Param("categoryId") Integer categoryId,
            @Param("userId") Integer userId
    );

    // --- 登録 / 更新 / 削除 ---
    int insert(Output output);
    int update(Output output);
    int delete(@Param("id") Long id);

    // --- カテゴリ使用数（カテゴリ削除ガード用） ---
    int countByCategoryId(@Param("categoryId") int categoryId);

    // --- IDリストで取得（並びはDB側の order句で指定予定） ---
    List<Output> selectByIds(@Param("ids") List<Long> ids);

    // --- 互換：カテゴリ“名”ベース（旧API互換が必要な間だけ残す） ---
    List<Output> findByUserAndCategory(
            @Param("userId") Integer userId,
            @Param("categoryName") String categoryName
    );

    // --- 新仕様：カテゴリIDで“お気に入り除外”一覧 ---
    List<Output> findByUserAndCategoryExcludeFav(
            @Param("userId") int userId,
            @Param("categoryId") int categoryId
    );

    // --- 新仕様：最近N件（ユーザー別 / created_at DESC, id DESC で安定） ---
    List<Output> findRecentByUser(
            @Param("userId") int userId,
            @Param("limit") int limit
    );
}