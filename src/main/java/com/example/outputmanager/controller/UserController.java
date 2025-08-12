package com.example.outputmanager.controller;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.outputmanager.form.UserForm;
import com.example.outputmanager.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /** 新規登録フォーム表示 */
    @GetMapping("/register")
    public String registerForm(@ModelAttribute("userForm") UserForm form, Model model) {
        // 既にフラッシュスコープから userForm が入っている場合はそのまま使われる
        return "users/register";
    }

    /** 新規登録処理 */
    @PostMapping("/register")
    public String registerSubmit(@Valid @ModelAttribute("userForm") UserForm form,
                                 BindingResult br,
                                 RedirectAttributes ra) {

        // ▼パスワード一致チェック
        if (form.getPassword() != null && form.getPasswordConfirm() != null
                && !form.getPassword().equals(form.getPasswordConfirm())) {
            br.rejectValue("passwordConfirm", "", "パスワードが一致しません");
        }

        // ▼入力エラーがあれば PRG で戻す（値とエラーをフラッシュ）
        if (br.hasErrors()) {
            ra.addFlashAttribute("org.springframework.validation.BindingResult.userForm", br);
            ra.addFlashAttribute("userForm", form);
            return "redirect:/users/register";
        }

        try {
            userService.registerNewUser(form.toEntity());
        } catch (IllegalArgumentException e) {
            // 例：名前/メール重複などサービス層のチェック
            br.reject("registerError", e.getMessage());
            ra.addFlashAttribute("org.springframework.validation.BindingResult.userForm", br);
            ra.addFlashAttribute("userForm", form);
            return "redirect:/users/register";
        }

        // 成功したらログイン画面へ
        ra.addFlashAttribute("msg", "登録しました。ログインしてください。");
        return "redirect:/";
    }
}