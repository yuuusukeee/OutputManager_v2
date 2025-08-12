package com.example.outputmanager.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.example.outputmanager.domain.Output;

import lombok.Data;

@Data
public class OutputForm {
    private Integer id;                         // 更新時に使う。新規はnullでOK

    @NotBlank @Size(max = 50)
    private String title;                       // VARCHAR(50) に対応（必須）

    @Size(max = 500)
    private String summary;                     // VARCHAR(500) に対応（任意）

    @Size(max = 2000)
    private String detail;                      // VARCHAR(2000) に対応（任意）

    @NotNull @Min(1)
    private Integer categoryId;                 // FK。未選択(0/null)をバリデでブロック

    @Size(max = 100)
    private String icon;                        // VARCHAR(100)（任意）

    @Size(max = 200)
    private String videoUrl;                    // VARCHAR(200)（任意）

    /** フォーム → エンティティ（userIdはサーバ側で注入） */
    public Output toEntity(Integer userId) {
        Output o = new Output();
        if (this.id != null) o.setId(this.id);  // 更新時のみIDを引き継ぐ
        o.setUserId(userId);                    // 信頼できるセッションから注入（hiddenは使わない）
        o.setCategoryId(this.categoryId);
        o.setTitle(this.title);
        o.setSummary(this.summary);
        o.setDetail(this.detail);
        o.setIcon(this.icon);
        o.setVideoUrl(this.videoUrl);
        return o;
    }

    /** エンティティ → フォーム（編集画面初期表示用） */
    public static OutputForm fromEntity(Output o) {
        OutputForm f = new OutputForm();
        f.setId(o.getId());
        f.setTitle(o.getTitle());
        f.setSummary(o.getSummary());
        f.setDetail(o.getDetail());
        f.setCategoryId(o.getCategoryId());
        f.setIcon(o.getIcon());
        f.setVideoUrl(o.getVideoUrl());
        return f;
    }
}