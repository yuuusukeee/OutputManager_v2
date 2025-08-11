package com.example.outputmanager.service;

import com.example.outputmanager.domain.User;
import com.example.outputmanager.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.mindrot.jbcrypt.BCrypt;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Override
    public boolean isValidLogin(String name, String password) {
        User user = userMapper.selectByName(name);
        return user != null && BCrypt.checkpw(password, user.getPassword());
    }

    @Override
    public Integer findUserId(String name) {
        User user = userMapper.selectByName(name);
        return user != null ? user.getId() : null;
    }

    @Override
    public void registerNewUser(User user) {
        if (userMapper.countByEmail(user.getEmail()) > 0) {
            throw new IllegalArgumentException("このメールアドレスは既に登録されています");
        }
        if (userMapper.countByName(user.getName()) > 0) {
            throw new IllegalArgumentException("このユーザー名は既に使われています");
        }
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        if (user.getIcon() == null) user.setIcon("default.png");
        userMapper.insert(user);
    }

    @Override
    public User findById(Integer id) {
        return userMapper.selectById(id);
    }

    @Override
    public User findByName(String name) {
        return userMapper.selectByName(name);
    }
}
