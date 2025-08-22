package com.example.outputmanager.service;

import java.util.List;

import com.example.outputmanager.domain.Favorite;
import com.example.outputmanager.domain.Output;

public interface FavoriteService {

    void addFavorite(Integer userId, Long outputId);

    void removeFavorite(Integer userId, Long outputId);

    /** お気に入り Output の ID(BIGINT→Long) 一覧 */
    List<Long> getFavoriteIdsByUser(Integer userId);

    List<Favorite> getFavoritesByUser(Integer userId);

    boolean isFavorite(Integer userId, Long outputId);

    List<Output> findOutputsByUser(Integer userId);
}