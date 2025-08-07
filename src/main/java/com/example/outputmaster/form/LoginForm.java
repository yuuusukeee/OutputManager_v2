package com.example.outputmaster.form;

import jakarta.validation.constraints.NotBlank;

import lombok.Data;

/**
 * ログイン用フォームデータ
 * home.htmlのフォーム値バインドで利用
 */

@Data
public class LoginForm {
    /** ユーザー名（必須） */
	@NotBlank
	private String username;
    /** パスワード（必須） */
	@NotBlank
	private String password;
}
