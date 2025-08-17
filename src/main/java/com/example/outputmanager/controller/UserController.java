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

    @GetMapping("/users/register")
    public String registerForm(Model model) {
        model.addAttribute("userForm", new UserForm());
        return "users/register";
    }

    @PostMapping("/users/register")
    public String registerSubmit(
            @Valid @ModelAttribute("userForm") UserForm form,
            Errors errors,
            Model model) {

        // ① パスワード一致チェック
        if (!errors.hasFieldErrors("password")    // 片方が別エラーのときは二重に出さない
                && !errors.hasFieldErrors("passwordConfirm")
                && !form.getPassword().equals(form.getPasswordConfirm())) {
            errors.rejectValue("passwordConfirm", "", "パスワードが一致しません");
        }

        // ② バリデーションNG → 入力画面に戻す
        if (errors.hasErrors()) {
            return "users/register";
        }

        // ③ 登録（重複などの業務エラーは try-catch で画面表示）
        try {
            userService.registerNewUser(form.toEntity());
        } catch (IllegalArgumentException e) {
            // 例えば「そのユーザー名/メールは既に使われています」など
            errors.reject("registerError", e.getMessage());
            return "users/register";
        }

        // ④ 成功 → ログインへ
        return "redirect:/";
    }
}