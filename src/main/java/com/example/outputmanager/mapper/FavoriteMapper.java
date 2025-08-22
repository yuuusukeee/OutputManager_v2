package com.example.outputmanager.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.outputmanager.domain.Favorite;
import com.example.outputmanager.domain.Output;

@Mapper
public interface FavoriteMapper {

    // 追加（複合UNIQUE (user_id, output_id) により二重登録はDBで防止）
    void insert(Favorite favorite);

    // 解除
    void delete(@Param("userId") Integer userId, @Param("outputId") Long outputId);

    // 自分のお気に入り全件（レコード）
    List<Favorite> selectByUserId(Integer userId);

    // 存在確認（0/1）
    int exists(@Param("userId") Integer userId, @Param("outputId") Long outputId);

    // お気に入りの Output 実体一覧（JOIN で取得 / created_at DESC 優先）
    List<Output> selectOutputsByUser(Integer userId);
}
