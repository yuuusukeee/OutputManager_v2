package com.example.outputmanager.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.outputmanager.domain.Favorite;

@Mapper
public interface FavoriteMapper {
    void insert(Favorite favorite);
    void delete(@Param("userId") Integer userId, @Param("outputId") Integer outputId);
    List<Favorite> selectByUserId(Integer userId);
    int exists(@Param("userId") Integer userId, @Param("outputId") Integer outputId);
    List<com.example.outputmanager.domain.Output> selectOutputsByUser(Integer userId);
}
