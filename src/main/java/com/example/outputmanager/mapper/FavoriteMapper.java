package com.example.outputmanager.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.outputmanager.domain.Favorite;
import com.example.outputmanager.domain.Output;

@Mapper
public interface FavoriteMapper {

    // 追加
    void insert(Favorite favorite);

    // 解除
    int delete(@Param("userId") Integer userId, @Param("outputId") Integer outputId);

    // ユーザーの「お気に入り」行一覧
    List<Favorite> selectByUserId(@Param("userId") Integer userId);

    // 登録済み判定（>0 なら登録済み）
    int exists(@Param("userId") Integer userId, @Param("outputId") Integer outputId);

    // ユーザーのお気に入り Output 一覧（画面表示用）
    List<Output> selectOutputsByUser(@Param("userId") Integer userId);
}