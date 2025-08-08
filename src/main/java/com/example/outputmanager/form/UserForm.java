package com.example.outputmanager.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import com.example.outputmanager.domain.User;

import lombok.Data;

@Data
public class UserForm {
    /** ユーザー名（必須） */
	@NotBlank
	private String username;
	
    /** メールアドレス（形式・必須） */
	@Email
	private String email;
	
    /** パスワード（必須） */
	@NotBlank
	private String password;
	
    /** toEntityメソッド：Userエンティティへ変換 */
	
	public User toEntity() {
		User user=new User();
		user.setUsername(this.username);
		user.setEmail(this.email);
		user.setPassword(this.password);
		return user;
	}

}
