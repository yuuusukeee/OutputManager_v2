package com.example.outputmanager.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DbConnectionTest implements CommandLineRunner{

	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public void run(String...args) {
		var users=jdbcTemplate.queryForList("SELECT*FROM users");
		System.out.println("【DB疎通OK】:"+users);
	}
}
