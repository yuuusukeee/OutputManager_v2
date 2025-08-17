package com.example.outputmanager.service;

import java.util.List;

import com.example.outputmanager.domain.Output;

public interface OutputService {

    // 既存互換：自分の一覧
    List<Output> getOutputList(Integer userId);

    // 既存互換：検索
    List<Output> searchOutputs(String keyword, Integer categoryId, Integer userId);

    // 既存互換：主キー取得（Integer版）
    Output getOutputById(Integer id);

    // 既存互換：登録/更新/削除
    int addOutput(Output out);
    int updateOutput(Output out);
    int deleteOutput(Integer id);

    // 既存実装：カテゴリ名で取得（お気に入り除外）
    List<Output> findByCategoryExcludingFavorite(Integer userId, String categoryName);

    // ★ 新規追加：主キー取得（Long版）→ Controllerの /outputs/{id} から呼ぶ
    Output findById(Long id);
}