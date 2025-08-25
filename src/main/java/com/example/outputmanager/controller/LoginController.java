package com.example.outputmanager.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.outputmanager.form.LoginForm;
import com.example.outputmanager.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;

    /** GET /login：未ログインは画面表示、ログイン済みは /home へ */
    @GetMapping("/login")
    public String showLogin(Model model, HttpSession session) {
        if (session.getAttribute("loginUserId") != null) {
            return "redirect:/home"; // 既ログインはホームへ
        }
        if (!model.containsAttribute("form")) {
            model.addAttribute("form", new LoginForm()); // フォームを供給
        }
        return "login";
    }

    /** POST /login：DB認証OK→/home、NG→同画面でエラー表示 */
    @PostMapping("/login")
    public String doLogin(@Valid @ModelAttribute("form") LoginForm form,
                          BindingResult binding,
                          HttpSession session,
                          Model model) {
        // 入力エラー（必須/長さ）なら画面戻し
        if (binding.hasErrors()) {
            return "login";
        }
        // DB認証：ユーザー存在 & パスワード一致のみOK
        boolean ok = userService.isValidLogin(form.getName(), form.getPassword());
        if (!ok) {
            binding.reject("login.failed", "ユーザー名またはパスワードが正しくありません");
            return "login"; // 同画面に留まる
        }

        // セッション確立（ここ以外で loginUserId を絶対に入れない）
        Integer uid = userService.findUserId(form.getName());
        session.setAttribute("loginUserId", uid);
        session.setAttribute("loginUserName", form.getName());

        // 成功したら必ず /home へ
        return "redirect:/home";
    }
}