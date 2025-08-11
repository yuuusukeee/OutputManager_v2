package com.example.outputmanager.domain;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Output {
    private Integer id;
    private Integer userId;
    private Integer categoryId;
    private String title;
    private String summary;
    private String detail;
    private String icon;
    private String videoUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
