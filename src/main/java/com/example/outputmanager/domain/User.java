package com.example.outputmanager.domain;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class User {
    private Integer id;
    private String name;        // users.name
    private String email;       // users.email
    private String password;    // ハッシュ
    private String icon;        // users.icon
    private LocalDateTime createdAt; // users.created_at
}
