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
	@Size(max = 50)   // 専用文言が不要なら message 省略 → 汎用 Size が出る
	private String name;

	@NotBlank(message = "{user.email.notblank}")
	@Email(message = "{user.email.format}")
	@Size(max = 100)
	private String email;

	@NotBlank(message = "{user.password.length}") // 空のときも同じ文言でOKならこれで一本化
	@Size(min = 8, max = 255, message = "{user.password.length}")
	private String password;

	@NotBlank(message = "確認のためパスワードを再入力してください")
	private String passwordConfirm;

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