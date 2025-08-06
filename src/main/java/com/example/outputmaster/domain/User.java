package com.example.outputmaster.domain;

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
