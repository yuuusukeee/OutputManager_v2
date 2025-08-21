package com.example.outputmanager.domain;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Output {
    private Long id;                // BIGINT (PK)
    private Integer userId;         // users.id
    private Integer categoryId;     // categories.id (NOT NULL)
    private String title;           // VARCHAR(50)
    private String summary;         // VARCHAR(500)
    private String detail;          // VARCHAR(2000)
    private String icon;            // /img/uploads/{uuid.ext} or external URL (legacy)
    private String videoUrl;        // YouTube URL
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}