package com.example.outputmanager.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import com.example.outputmanager.domain.User;

import lombok.Data;

@Data
public class UserForm {
    // 表示名（必須・最大50）
    @NotBlank @Size(max = 50)
    private String name;

    // メール（必須・形式チェック・最大100）
    @NotBlank @Email @Size(max = 100)
    private String email;

    // パスワード（必須・8〜255）
    @NotBlank @Size(min = 8, max = 255)
    private String password;

    // パスワード確認（必須ではないが、Controllerで一致チェックを行う前提）
    @Size(min = 8, max = 255)
    private String passwordConfirm;

    // アイコンURL（任意・最大100）
    @Size(max = 100)
    private String icon;

    // ★確認用は保存しない：Entityへは反映させない
    public User toEntity() {
        User u = new User();
        u.setName(this.name);
        u.setEmail(this.email);
        u.setPassword(this.password);
        u.setIcon(this.icon);
        return u;
    }
}