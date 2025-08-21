package com.example.outputmanager.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import com.example.outputmanager.domain.Output;

import lombok.Data;

@Data
public class OutputForm {

    // 更新時のみ利用。新規は null
    private Long id;

    @NotBlank
    @Size(max = 50)
    private String title;          // VARCHAR(50)

    @Size(max = 500)
    private String summary;        // VARCHAR(500)

    @Size(max = 2000)
    private String detail;         // VARCHAR(2000)

    @NotNull
    @Min(1)
    private Integer categoryId;    // 必須（未選択ブロック）

    // 画像アップロード（任意）: 文字列URLのiconは廃止し、実ファイルを受け取る
    // ※ 最大サイズ・拡張子チェックはコントローラ/サービス側で実施
    private MultipartFile imageFile;

    // 動画URL（任意, YouTubeのみ許可は後段のサーバ側バリデで実装）
    @Size(max = 200)
    private String videoUrl;

    /** フォーム → エンティティ（iconはアップロード処理後に別途セット） */
    public Output toEntity(Integer userId) {
        Output o = new Output();
        if (this.id != null) {
            o.setId(this.id);
        }
        o.setUserId(userId);
        o.setCategoryId(this.categoryId);
        o.setTitle(this.title);
        o.setSummary(this.summary);
        o.setDetail(this.detail);
        // icon はアップロード成否で別途 o.setIcon(...) する
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
        // 画像は再アップロード前提のため imageFile は空のまま
        f.setVideoUrl(o.getVideoUrl());
        return f;
    }
}
