package com.example.outputmaneger.mapper;

import com.example.outputmaneger.domain.User;

public interface UserMapper {
	 /** ユーザー名からユーザーを1件取得 */
	User selectByUsername(String username);
    /** 新規ユーザー登録 */
	void insert(User user);
    /** IDからユーザー取得 */
	User selectById(Integer id);
}
