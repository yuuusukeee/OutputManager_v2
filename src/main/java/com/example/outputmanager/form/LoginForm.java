package com.example.outputmanager.form;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class LoginForm {
    @NotBlank
    private String name;      // ログインID
    @NotBlank
    private String password;  // パスワード
}
