package com.example.outputmanager.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.outputmanager.domain.Output;

@Mapper  // MyBatisにMapperとして認識させる
public interface OutputMapper {

    // 一覧（自分のデータ）
    List<Output> selectByUserId(@Param("userId") Integer userId);
    // 検索（XMLの <select id="search"> と対応）
    List<Output> search(@Param("keyword") String keyword,
                        @Param("categoryId") Integer categoryId,
                        @Param("userId") Integer userId);
    // 詳細
    Output selectById(@Param("id") Integer id);

    // ここが今回のポイント：XMLの <insert id="insert"> 等とメソッド名を合わせる
    int insert(Output out);   // ← 追加（戻り値は影響件数）
    int update(Output out);   // ← 追加
    int delete(@Param("id") Integer id); // ← 追加
    int countByCategoryId(@Param("categoryId") Integer categoryId);
}