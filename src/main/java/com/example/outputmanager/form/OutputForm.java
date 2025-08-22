package com.example.outputmanager.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import com.example.outputmanager.domain.Output;

import lombok.Data;

/**
 * アウトプットの入力フォーム。
 * 仕様：画像はファイルアップロード（imageFile）、動画はURL（videoUrl）。
 * 画像と動画の同時入力は不可（排他）。最終チェックはサーバ側で行う。
 */
@Data
public class OutputForm {

    /** 更新時のみ利用（新規は null） */
    private Long id;

    @NotBlank
    @Size(max = 50)
    private String title;

    @Size(max = 500)
    private String summary;

    @Size(max = 2000)
    private String detail;

    /** カテゴリは必須（1:学習 / 2:健康 / 3:仕事 / 4:生活） */
    @NotNull @Min(1)
    private Integer categoryId;

    /**
     * 画像アップロード（任意）。
     * サーバ側で UUID リネームし、/img/uploads/{uuid.ext} で保存・表示する。
     */
    private MultipartFile imageFile;

    /**
     * 動画URL（任意・YouTubeのみ許可）。https限定、youtube.com / youtu.be を後段で検証。
     */
    @Size(max = 200)
    private String videoUrl;

    /**
     * 編集時の既存アイコンパス保持用（例：/img/uploads/xxxxx.jpg）。
     * 新規時は空でOK。差し替え時はサーバで旧ファイル削除を行う。
     */
    private String existingIcon;

    /** 補助：画像と動画の両方が指定されているか（排他違反の一次チェックに利用可） */
    public boolean hasBothMedia() {
        boolean hasImage = (imageFile != null && !imageFile.isEmpty());
        boolean hasVideo = (videoUrl != null && !videoUrl.isBlank());
        return hasImage && hasVideo;
    }

    /**
     * フォーム → エンティティ（アイコンは後段のアップロード処理で設定）
     * サービス層で imageFile を保存 → icon パス設定、videoUrl の正規化等を行う。
     */
    public Output toEntityBasic(Integer userId) {
        Output o = new Output();
        o.setId(this.id);
        o.setUserId(userId);
        o.setCategoryId(this.categoryId);
        o.setTitle(this.title);
        o.setSummary(this.summary);
        o.setDetail(this.detail);
        // icon はアップロード処理で設定。既存保持はサービス層で existingIcon を参照。
        o.setVideoUrl(this.videoUrl);
        return o;
    }

    /** エンティティ → フォーム（編集画面初期表示用の便宜メソッド） */
    public static OutputForm fromEntity(Output o) {
        OutputForm f = new OutputForm();
        f.setId(o.getId());
        f.setTitle(o.getTitle());
        f.setSummary(o.getSummary());
        f.setDetail(o.getDetail());
        f.setCategoryId(o.getCategoryId());
        f.setExistingIcon(o.getIcon()); // 表示プレビュー用に保持
        f.setVideoUrl(o.getVideoUrl());
        return f;
    }
}
