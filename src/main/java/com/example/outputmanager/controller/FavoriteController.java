package com.example.outputmanager.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.outputmanager.service.FavoriteService;
import com.example.outputmanager.service.OutputService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final OutputService outputService; // 対象存在チェック（任意）

    /** お気に入り追加 */
    @PostMapping("/favorites/add")
    public String add(@RequestParam("outputId") Long outputId,
                      HttpSession session,
                      HttpServletRequest request,
                      RedirectAttributes ra) {
        Integer uid = (Integer) session.getAttribute("loginUserId");
        if (uid == null) return "redirect:/login"; // ★未ログインガード

        // （任意）対象が存在しないIDなら一覧に戻す
        try {
            if (outputService.findById(outputId) == null) {
                ra.addFlashAttribute("error", "対象のアウトプットが見つかりません。");
                return "redirect:/outputs";
            }
        } catch (Exception ignored) {}

        try {
            favoriteService.addFavorite(uid, outputId); // ★お気に入り登録
            ra.addFlashAttribute("msg", "お気に入りに追加しました");
        } catch (DuplicateKeyException e) {
            // 既に登録済みなら黙って成功扱い
            ra.addFlashAttribute("msg", "すでにお気に入りに追加済みです");
        }
        return redirectBack(request, outputId);
    }

    /** お気に入り解除 */
    @PostMapping("/favorites/remove")
    public String remove(@RequestParam("outputId") Long outputId,
                         HttpSession session,
                         HttpServletRequest request,
                         RedirectAttributes ra) {
        Integer uid = (Integer) session.getAttribute("loginUserId");
        if (uid == null) return "redirect:/login"; // ★未ログインガード

        favoriteService.removeFavorite(uid, outputId); // ★お気に入り解除
        ra.addFlashAttribute("msg", "お気に入りを解除しました");
        return redirectBack(request, outputId);
    }

    /** 直前ページへ戻す（Refererが無ければ一覧へ / 明細からなら詳細に戻す） */
    private String redirectBack(HttpServletRequest req, Long outputId) {
        String ref = req.getHeader("Referer");
        if (ref != null) {
            // 明細から来ていれば詳細へ戻す
            if (ref.contains("/outputs/") && !ref.contains("/outputs?")) {
                return "redirect:/outputs/" + outputId;
            }
            // それ以外は基本的に一覧へ
        }
        return "redirect:/outputs";
    }
}