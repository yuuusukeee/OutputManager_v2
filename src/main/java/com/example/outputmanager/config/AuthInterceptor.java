package com.example.outputmanager.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 未ログイン時のガード。
 * - 公開パス（ログイン/登録/静的ファイル等）は通す
 * - それ以外は /login にリダイレクト
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    /**
     * 認証不要のプレフィックス（アプリ内パスで判定）
     * 末尾に "/" を付けておくと startsWith 判定で下位も許可しやすい。
     */
    private static final String[] PUBLIC_PREFIXES = {
            "/",            // ルート（ちょうど "/" のみ許可）
            "/home",        // 公開ヘッダー用（CTAはガードで保護）
            "/login",       // GET/POST 両方許可
            "/logout",      // 明示ログアウト
            "/users/register", // 登録画面/POST
            // 静的配信
            "/css/", "/js/", "/images/", "/webjars/",
            "/favicon.ico", "/error",
            // アップロード画像（プレースホルダ含む）
            "/img/"         // /img/placeholder.png, /img/uploads/** を公開
    };

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
        // アプリ内相対パスに正規化（コンテキストパスを除去）
        final String context = req.getContextPath() == null ? "" : req.getContextPath();
        final String uri = req.getRequestURI();
        final String path = uri.startsWith(context) ? uri.substring(context.length()) : uri;

        if (isPublicPath(path)) {
            return true;
        }

        HttpSession session = req.getSession(false);
        Object uid = (session == null) ? null : session.getAttribute("loginUserId");
        if (uid != null) {
            return true;
        }

        res.sendRedirect(context + "/login");
        return false;
    }

    private boolean isPublicPath(String path) {
        for (String p : PUBLIC_PREFIXES) {
            if ("/".equals(p)) {
                if ("/".equals(path)) return true; // ちょうどルートのみ
            } else if (path.equals(p) || path.startsWith(p)) {
                return true;
            }
        }
        return false;
    }
}