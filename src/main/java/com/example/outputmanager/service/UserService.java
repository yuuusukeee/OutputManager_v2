package com.example.outputmanager.service;

import com.example.outputmanager.domain.User;

public interface UserService {
    boolean isValidLogin(String name, String password);
    Integer findUserId(String name);
    void registerNewUser(User user);
    User findById(Integer id);
    User findByName(String name);
}
