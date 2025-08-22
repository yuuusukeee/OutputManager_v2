package com.example.outputmanager.domain;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Favorite {
    private Integer id;          // favorites.id は従来どおり INT 想定
    private Integer userId;

    /** favorites.output_id → BIGINT に合わせ Long へ */
    private Long outputId;

    private LocalDateTime createdAt;
}