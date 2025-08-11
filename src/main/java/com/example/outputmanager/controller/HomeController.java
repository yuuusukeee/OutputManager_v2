package com.example.outputmanager.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.outputmanager.form.LoginForm;
import com.example.outputmanager.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final UserService userService; // 今は未使用（後で本実装に戻す用）

    /** ログイン画面表示 */
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "home";
    }

    /** ログイン処理（暫定：強制ログイン） */
    @PostMapping("/")
    public String login(@Valid @ModelAttribute("loginForm") LoginForm form,
                        Errors errors,
                        HttpSession session,
                        Model model) {

        if (errors.hasErrors()) {
            return "home";
        }

        // ★一時対処：認証をスキップして userId=1 をセッションに保存
        Integer userId = 1; // ← DBの outputs.user_id に合わせて必要なら変更
        session.setAttribute("userId", userId);

        System.out.println("[DEBUG] forced login userId=" + session.getAttribute("userId"));

        // 一覧へ
        return "redirect:/outputs";
    }
}