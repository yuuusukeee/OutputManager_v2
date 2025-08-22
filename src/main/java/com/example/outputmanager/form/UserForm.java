package com.example.outputmanager.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import com.example.outputmanager.domain.User;

import lombok.Data;

@Data
public class UserForm {

    @NotBlank(message = "{user.name.notblank}")
    @Size(max = 50)
    private String name;

    @NotBlank(message = "{user.email.notblank}")
    @Email(message = "{user.email.format}")
    @Size(max = 100)
    private String email;

    @NotBlank(message = "{user.password.length}") // 空時も同一文言
    @Size(min = 8, max = 255, message = "{user.password.length}")
    private String password;

    @NotBlank(message = "確認のためパスワードを再入力してください")
    private String passwordConfirm;

    // 旧運用互換：任意の外部URLを許容。新規は uploads を推奨（将来非表示化可）
    @Pattern(regexp = "^$|https?://.+", message = "{user.icon.url}")
    @Size(max = 100)
    private String icon;

    public User toEntity() {
        User u = new User();
        u.setName(this.name);
        u.setEmail(this.email);
        u.setPassword(this.password);
        u.setIcon(this.icon);
        return u;
    }
}
