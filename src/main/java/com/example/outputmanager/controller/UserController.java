package com.example.outputmanager.controller;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.outputmanager.form.UserForm;
import com.example.outputmanager.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // --- ここにあった GET/POST /login は削除（LoginController に一本化） ---

    /** 新規登録 画面表示 */
    @GetMapping("/users/register")
    public String registerForm(Model model) {
        model.addAttribute("userForm", new UserForm());
        return "users/register";
    }

    /** 新規登録 実行 */
    @PostMapping("/users/register")
    public String registerSubmit(
            @Valid @ModelAttribute("userForm") UserForm form,
            Errors errors,
            Model model) {

        // パスワード一致チェック
        if (!errors.hasFieldErrors("password")
                && !errors.hasFieldErrors("passwordConfirm")
                && !form.getPassword().equals(form.getPasswordConfirm())) {
            errors.rejectValue("passwordConfirm", "", "パスワードが一致しません");
        }

        // バリデーションNG
        if (errors.hasErrors()) {
            return "users/register";
        }

        try {
            userService.registerNewUser(form.toEntity());
        } catch (IllegalArgumentException e) {
            errors.reject("registerError", e.getMessage());
            return "users/register";
        }

        // 成功 → /login へ（ログイン画面は LoginController が担当）
        return "redirect:/login";
    }
}