package com.example.outputmanager.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class LoginForm {
    @NotBlank
    @Size(max = 50)
    private String name;      // ログインID

    @NotBlank
    @Size(max = 255)
    private String password;  // パスワード
}
