package com.example.outputmanager.mapper;

import com.example.outputmanager.domain.Favorite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface FavoriteMapper {
    void insert(Favorite favorite);
    void delete(@Param("userId") Integer userId, @Param("outputId") Integer outputId);
    List<Favorite> selectByUserId(Integer userId);
    int exists(@Param("userId") Integer userId, @Param("outputId") Integer outputId);
}
