package com.example.outputmaster.domain;
/**
 * ユーザー情報を管理するクラス
 * このクラスはusersテーブル1レコードを表現する
 */
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class User {
	private Integer id;
	private String username;
	private String email;
	private String password;
	private LocalDateTime createdAt;

}
