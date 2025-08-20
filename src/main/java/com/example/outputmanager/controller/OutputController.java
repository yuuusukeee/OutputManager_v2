package com.example.outputmanager.controller;

import java.util.Map;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.outputmanager.domain.Output;
import com.example.outputmanager.service.CategoryService;
import com.example.outputmanager.service.FavoriteService;
import com.example.outputmanager.service.OutputService;

import lombok.RequiredArgsConstructor;

/**
 * 一覧/カテゴリ/新規・編集フォーム/詳細を集約。
 */
@Controller
@RequiredArgsConstructor
public class OutputController {

    private final OutputService outputService;
    private final CategoryService categoryService;   // ★カテゴリ一覧供給
    private final FavoriteService favoriteService;   // ★お気に入り取得/判定

    /** 一覧トップ（最近/お気に入り/カテゴリごと） */
    @GetMapping("/outputs")
    public String outputs(Model model, HttpSession session) {
        Integer uid = (Integer) session.getAttribute("loginUserId");
        if (uid == null) {
            return "redirect:/login";
        }

        // ★最近10件
        model.addAttribute("recent", outputService.findRecentByUser(uid, 10));
        // ★お気に入り
        model.addAttribute("favorites", favoriteService.findOutputsByUser(uid));

        // 各カテゴリ
        model.addAttribute("learning", outputService.findByCategoryExcludingFavorite(uid, 1));
        model.addAttribute("health",   outputService.findByCategoryExcludingFavorite(uid, 2));
        model.addAttribute("work",     outputService.findByCategoryExcludingFavorite(uid, 3));
        model.addAttribute("life",     outputService.findByCategoryExcludingFavorite(uid, 4));

        return "outputs/list";
    }

    /** 新規作成フォーム */
    @GetMapping("/outputs/new")
    public String showCreateForm(Model model, HttpSession session) {
        Integer uid = (Integer) session.getAttribute("loginUserId");
        if (uid == null) {
            return "redirect:/login";
        }
        model.addAttribute("output", new Output());
        model.addAttribute("categories", categoryService.findAll()); // ★カテゴリ実データ
        return "outputs/new";
    }

    /** 編集フォーム */
    @GetMapping("/outputs/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model, HttpSession session) {
        Integer uid = (Integer) session.getAttribute("loginUserId");
        if (uid == null) {
            return "redirect:/login";
        }
        Output o = outputService.findById(id);
        if (o == null) {
            return "redirect:/outputs";
        }
        model.addAttribute("output", o);
        model.addAttribute("categories", categoryService.findAll()); // ★カテゴリ実データ
        return "outputs/edit";
    }

    /** 詳細 */
    @GetMapping("/outputs/{id}")
    public String detail(@PathVariable("id") Long id, Model model, HttpSession session) {
        Integer uid = (Integer) session.getAttribute("loginUserId");
        if (uid == null) {
            return "redirect:/login";
        }

        Output o = outputService.findById(id);
        if (o == null) {
            return "redirect:/outputs";
        }
        model.addAttribute("output", o);

        // ★お気に入り判定
        boolean favored = favoriteService.isFavorite(uid, o.getId().intValue());
        model.addAttribute("favored", favored);

        // ★カテゴリ名
        Map<Integer, String> nameMap = categoryService.nameMap();
        String categoryLabel = (o.getCategoryId() == null)
                ? "未分類"
                : nameMap.getOrDefault(o.getCategoryId(), "未分類");
        model.addAttribute("categoryLabel", categoryLabel);

        return "outputs/detail";
    }

    /** 保存処理 */
    @PostMapping("/outputs/save")
    public String save(@ModelAttribute Output output, HttpSession session) {
        Integer uid = (Integer) session.getAttribute("loginUserId");
        if (uid == null) {
            return "redirect:/login";
        }
        output.setUserId(uid);
        if (output.getId() == null) {
            outputService.save(output);
        } else {
            outputService.update(output);
        }
        return "redirect:/outputs";
    }

    /** 削除 */
    @PostMapping("/outputs/delete")
    public String delete(@RequestParam("id") Long id, HttpSession session) {
        Integer uid = (Integer) session.getAttribute("loginUserId");
        if (uid == null) {
            return "redirect:/login";
        }
        outputService.delete(id);
        return "redirect:/outputs";
    }
}