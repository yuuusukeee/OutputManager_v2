package com.example.outputmanager.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    /**
     * 認証不要でアクセス可能なパスの先頭プレフィックス
     * 注意: "/" は完全一致、それ以外は startsWith 判定。
     */
    private static final String[] PUBLIC_PREFIXES = {
            "/login", "/users/register",
            "/css", "/js", "/images", "/webjars",
            "/favicon.ico", "/error",
            "/img" // アップロード画像 & プレースホルダ（/img/**）を公開
    };

    private boolean isPublicPath(String path) {
        for (String p : PUBLIC_PREFIXES) {
            if ("/".equals(p)) {
                if ("/".equals(path)) return true;
            } else if (path.startsWith(p)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
        final String path = req.getRequestURI();
        if (isPublicPath(path)) return true;

        HttpSession session = req.getSession(false); // 既存のみ取得
        Object uid = (session == null) ? null : session.getAttribute("loginUserId");
        if (uid != null) return true;

        res.sendRedirect(req.getContextPath() + "/login");
        return false;
    }
}