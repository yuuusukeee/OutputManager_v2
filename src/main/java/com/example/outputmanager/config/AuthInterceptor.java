package com.example.outputmanager.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private static final String[] PUBLIC_PREFIXES = {
        "/", "/home", "/login", "/logout", "/users/register",
        "/css", "/js", "/images", "/webjars", "/favicon.ico", "/error"
    };

    private boolean isPublicPath(String path) {
        for (String p : PUBLIC_PREFIXES) {
            if ("/".equals(p)) { if ("/".equals(path)) return true; }
            else if (path.startsWith(p)) { return true; }
        }
        return false;
    }

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
        final String path = req.getRequestURI();
        if (isPublicPath(path)) return true;

        HttpSession session = req.getSession(false); // 既存のみ
        Object uid = (session == null) ? null : session.getAttribute("loginUserId");
        if (uid != null) return true;

        res.sendRedirect(req.getContextPath() + "/login");
        return false;
    }
}