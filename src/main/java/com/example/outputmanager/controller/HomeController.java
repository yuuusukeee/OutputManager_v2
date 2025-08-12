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

    private final UserService userService;

    /** ログイン画面表示（未ログインのみ） */
    @GetMapping({"/", "/login"})
    public String loginPage(HttpSession session, Model model) {
        if (session.getAttribute("loginUserId") != null) {
            return "redirect:/outputs"; // 既にログイン済みなら一覧へ
        }
        model.addAttribute("loginForm", new LoginForm());
        return "home"; // 既存の home.html をログイン画面として再利用
    }

    /** ログイン実行 */
    @PostMapping("/login")
    public String doLogin(@Valid @ModelAttribute("loginForm") LoginForm form,
                          Errors errors,
                          HttpSession session) {
        if (errors.hasErrors()) return "home";

        // name/passwordで認証（UserServiceは既にBCrypt対応）
        boolean ok = userService.isValidLogin(form.getName(), form.getPassword());
        if (!ok) {
            errors.reject("login.failed", "ユーザー名またはパスワードが正しくありません。");
            return "home";
        }
        // 認証OK → ユーザーIDを取得しセッションに保存
        Integer uid = userService.findUserId(form.getName());
        session.setAttribute("loginUserId", uid);   // ★セッションキーを統一
        return "redirect:/outputs";
    }

    /** ログアウト */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}