package com.example.outputmanager.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
        HttpSession session = req.getSession(false);
        String uri = req.getRequestURI();

        // 公開パス（ログイン/ユーザー登録/静的）だけは素通し
        boolean publicPath =
                uri.equals("/") || uri.startsWith("/login") || uri.startsWith("/logout") ||
                uri.startsWith("/users/register") ||
                uri.startsWith("/css") || uri.startsWith("/js") || uri.startsWith("/images");

        if (publicPath) return true;

        // ログイン判定
        Integer uid = (session == null) ? null : (Integer) session.getAttribute("loginUserId");
        if (uid != null) return true;

        res.sendRedirect("/login");
        return false;
    }
}