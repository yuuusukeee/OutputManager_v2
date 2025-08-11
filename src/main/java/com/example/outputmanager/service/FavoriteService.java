package com.example.outputmanager.service;

import com.example.outputmanager.domain.Favorite;
import java.util.List;

public interface FavoriteService {
    void addFavorite(Integer userId, Integer outputId);
    void removeFavorite(Integer userId, Integer outputId);
    List<Integer> getFavoriteIdsByUser(Integer userId);
    List<Favorite> getFavoritesByUser(Integer userId);
    boolean isFavorite(Integer userId, Integer outputId);
}
