package com.example.outputmanager.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.NotBlank;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * ログイン画面の表示と、ログインPOSTを受ける最小実装。
 * UIフロー成立を優先し、バリデーションOKならセッションに loginUserId を入れて /outputs へ。
 * （後で実DB照合に置き換え可能：TODO印を参照）
 */
@Controller
public class LoginController {

    /** GET /login : ログイン画面の表示 */
    @GetMapping("/login")
    public String showLogin(Model model) {
        if (!model.containsAttribute("form")) {
            model.addAttribute("form", new LoginForm()); // login.html の th:object="${form}" に合わせる
        }
        return "login";
    }

    /** POST /login : ログイン処理（最小） */
    @PostMapping("/login")
    public String doLogin(
            @ModelAttribute("form") LoginForm form,
            BindingResult bindingResult,
            HttpSession session,
            Model model
    ) {
        // 簡易バリデーション（空欄NG）
        if (form.getName() == null || form.getName().isBlank()
                || form.getPassword() == null || form.getPassword().isBlank()) {
            bindingResult.reject("login.invalid", "ユーザー名とパスワードを入力してください。");
            return "login"; // エラー時はそのまま画面再表示
        }

        // TODO: ここをDB照合に置き換える（例：userService.authenticate(...)）
        // 成功時のユーザーIDを取得して session に格納する想定。
        // いまは UI を進めるためにダミーで固定IDをセット。
        Integer userId = 1;

        // セッションにログイン情報を保存
        session.setAttribute("loginUserId", userId);
        session.setAttribute("loginUserName", form.getName());

        // ログイン後の着地先 → /outputs に修正
        return "redirect:/outputs";
    }

    /** ログインフォームの最小DTO（既存DTOがあれば置き換え可） */
    public static class LoginForm {
        @NotBlank
        private String name;
        @NotBlank
        private String password;

        // --- getter / setter ---
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}