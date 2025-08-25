package com.example.outputmanager.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	// "/" はまず /login に飛ばす。ログイン済みなら LoginController 側で /home へ返す
	@GetMapping("/")
	public String root() { 
	    return "redirect:/login"; 
	}

    @GetMapping("/home")
    public String home(Model model, HttpSession session) {
        // 将来：最近の画像などを model に詰める
        return "home";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) session.invalidate();
        return "redirect:/login";
    }
}
