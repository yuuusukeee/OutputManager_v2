package com.example.outputmanager.service;

import java.util.List;

import com.example.outputmanager.domain.Output;

public interface OutputService {

    // 既存のカテゴリ名版（互換用、今後は不使用想定）
    List<Output> findByUserAndCategory(int userId, String categoryName);

    // ★新規追加：カテゴリを数値IDで絞り込み（お気に入り除外）
    List<Output> findByCategoryExcludingFavorite(int userId, int categoryId);

    // ★新規追加：最近N件を取得
    List<Output> findRecentByUser(int userId, int limit);

    // 既存のID検索
    Output findById(Long id);

    // 既存の保存
    void save(Output output);

    // 既存の更新
    void update(Output output);

    // 既存の削除
    void delete(Long id);
}