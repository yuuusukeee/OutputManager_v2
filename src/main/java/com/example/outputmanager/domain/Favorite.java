package com.example.outputmanager.domain;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Favorite {
    private Integer id;
    private Integer userId;
    private Integer outputId;
    private LocalDateTime createdAt;
}
