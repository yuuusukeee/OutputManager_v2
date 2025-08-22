package com.example.outputmanager.domain;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Output {
    /** BIGINT → Java は Long に統一 */
    private Long id;

    /** users.id は現状 INT 運用のため Integer 維持 */
    private Integer userId;

    /** categories.id も INT のため Integer 維持（1:学習/2:健康/3:仕事/4:生活） */
    private Integer categoryId;

    private String title;
    private String summary;
    private String detail;

    /**
     * 画像の公開URL（例：/img/uploads/{uuid.ext}）
     * 旧データの外部URL互換はこの文字列にそのまま入る想定
     */
    private String icon;

    /** YouTube の URL（保存は元URL） */
    private String videoUrl;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
