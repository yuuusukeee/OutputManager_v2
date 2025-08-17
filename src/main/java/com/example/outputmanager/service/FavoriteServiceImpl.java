package com.example.outputmanager.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.outputmanager.domain.Favorite;
import com.example.outputmanager.mapper.FavoriteMapper;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteMapper favoriteMapper;

    @Override
    public void addFavorite(Integer userId, Integer outputId) {
        Favorite fav = new Favorite();
        fav.setUserId(userId);
        fav.setOutputId(outputId);
        favoriteMapper.insert(fav);
    }

    @Override
    public void removeFavorite(Integer userId, Integer outputId) {
        favoriteMapper.delete(userId, outputId);
    }

    @Override
    public List<Integer> getFavoriteIdsByUser(Integer userId) {
        List<Favorite> favs = favoriteMapper.selectByUserId(userId);
        return favs.stream().map(Favorite::getOutputId).collect(Collectors.toList());
    }

    @Override
    public List<Favorite> getFavoritesByUser(Integer userId) {
        return favoriteMapper.selectByUserId(userId);
    }

    @Override
    public boolean isFavorite(Integer userId, Integer outputId) {
        return favoriteMapper.exists(userId, outputId) > 0;
    }
    @Override
    public List<com.example.outputmanager.domain.Output> findOutputsByUser(Integer userId) {
        return favoriteMapper.selectOutputsByUser(userId);
    }
    
}
