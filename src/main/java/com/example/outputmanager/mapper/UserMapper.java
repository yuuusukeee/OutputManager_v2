package com.example.outputmanager.mapper;

import com.example.outputmanager.domain.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    User selectByName(String name);
    void insert(User user);
    User selectById(Integer id);

    int countByEmail(String email);
    int countByName(String name);
}
