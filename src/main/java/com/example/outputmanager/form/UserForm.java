package com.example.outputmanager.form;

import lombok.Data;
import jakarta.validation.constraints.*;
import com.example.outputmanager.domain.User;

@Data
public class UserForm {
    @NotBlank @Size(max=50)
    private String name;
    @NotBlank @Email @Size(max=100)
    private String email;
    @NotBlank @Size(min=8, max=255)
    private String password;
    @Size(max=100)
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
