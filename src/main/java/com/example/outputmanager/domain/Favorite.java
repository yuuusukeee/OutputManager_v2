package com.example.outputmanager.domain;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Favorite {
    private Integer id;          // favorites.id（INTのままでOK）
    private Integer userId;      // favorites.user_id
    private Long outputId;       // ★ favorites.output_id → Output.id(BIGINT) に合わせて Long
    private LocalDateTime createdAt; // favorites.created_at
}