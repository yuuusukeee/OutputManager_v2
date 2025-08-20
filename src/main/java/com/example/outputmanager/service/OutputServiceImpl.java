package com.example.outputmanager.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.outputmanager.domain.Output;
import com.example.outputmanager.mapper.OutputMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OutputServiceImpl implements OutputService {

    private final OutputMapper outputMapper;

    // 既存：カテゴリ名版（互換用）
    @Override
    public List<Output> findByUserAndCategory(int userId, String categoryName) {
        return outputMapper.findByUserAndCategory(userId, categoryName);
    }

    // ★新規追加：カテゴリを数値IDで絞り込み（お気に入り除外）
    @Override
    public List<Output> findByCategoryExcludingFavorite(int userId, int categoryId) {
        return outputMapper.findByUserAndCategoryExcludeFav(userId, categoryId);
    }

    // ★新規追加：最近N件を取得
    @Override
    public List<Output> findRecentByUser(int userId, int limit) {
        return outputMapper.findRecentByUser(userId, limit);
    }

    // 既存：ID検索
    @Override
    public Output findById(Long id) {
        return outputMapper.findById(id);
    }

    // 既存：保存
    @Override
    public void save(Output output) {
        outputMapper.insert(output);
    }

    // 既存：更新
    @Override
    public void update(Output output) {
        outputMapper.update(output);
    }

    // 既存：削除
    @Override
    public void delete(Long id) {
        outputMapper.delete(id);
    }
}