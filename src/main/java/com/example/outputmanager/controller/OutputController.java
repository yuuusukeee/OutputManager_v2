package com.example.outputmanager.controller;

import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.outputmanager.domain.Output;
import com.example.outputmanager.form.OutputForm;
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

        // ★重複OK仕様：お気に入りに含まれていてもカテゴリに表示する
        model.addAttribute("learn",  outputService.findByUserAndCategory(uid, "学習"));
        model.addAttribute("health", outputService.findByUserAndCategory(uid, "健康"));
        model.addAttribute("work",   outputService.findByUserAndCategory(uid, "仕事"));
        model.addAttribute("life",   outputService.findByUserAndCategory(uid, "生活"));

        // 最終採用テンプレート
        return "outputs/index";
    }

    /** 新規作成フォーム（共通フォーム：outputs/save.html） */
    @GetMapping("/outputs/new")
    public String showCreateForm(Model model, HttpSession session) {
        Integer uid = (Integer) session.getAttribute("loginUserId");
        if (uid == null) {
            return "redirect:/login";
        }
        model.addAttribute("outputForm", new OutputForm());
        model.addAttribute("categories", categoryService.findAll()); // ★カテゴリ実データ
        return "outputs/save";
    }

    /** 編集フォーム（共通フォーム：outputs/save.html） */
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
        model.addAttribute("outputForm", OutputForm.fromEntity(o));
        model.addAttribute("categories", categoryService.findAll()); // ★カテゴリ実データ
        return "outputs/save";
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

        // ★お気に入り判定（Longに統一）
        boolean favored = favoriteService.isFavorite(uid, o.getId());
        model.addAttribute("favored", favored);

        // ★カテゴリ名
        Map<Integer, String> nameMap = categoryService.nameMap();
        String categoryLabel = (o.getCategoryId() == null)
                ? "未分類"
                : nameMap.getOrDefault(o.getCategoryId(), "未分類");
        model.addAttribute("categoryLabel", categoryLabel);

        return "outputs/detail";
    }

    /** 保存処理（新規/更新 兼用） */
    @PostMapping("/outputs/save")
    public String save(@Valid @ModelAttribute("outputForm") OutputForm form,
                       BindingResult binding,
                       Model model,
                       HttpSession session) {
        Integer uid = (Integer) session.getAttribute("loginUserId");
        if (uid == null) {
            return "redirect:/login";
        }
        // 画像と動画の排他チェック
        if (form.hasBothMedia()) {
            binding.reject("output.media.exclusive", "画像と動画は同時に登録できません");
        }
        if (binding.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll());
            return "outputs/save";
        }
        Output entity = form.toEntityBasic(uid);
        // 画像保存は④本実装で対応。今は既存アイコンを保持（新規は空OK）
        entity.setIcon(form.getExistingIcon());
        if (entity.getId() == null) {
            outputService.save(entity);
        } else {
            outputService.update(entity);
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

    // ===== ここからカテゴリ別一覧とお気に入り一覧（18件/ページ固定）=====

    @GetMapping("/favorites")
    public String favorites(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                            @RequestParam(value = "size", required = false, defaultValue = "18") int size,
                            Model model, HttpSession session) {
        Integer uid = (Integer) session.getAttribute("loginUserId");
        if (uid == null) return "redirect:/login";
        size = 18; // 常に18固定
        List<Output> all = favoriteService.findOutputsByUser(uid);
        return paginateTo("favorites/list", all, page, size, model);
    }

    @GetMapping("/learn")
    public String learn(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                        @RequestParam(value = "size", required = false, defaultValue = "18") int size,
                        Model model, HttpSession session) {
        Integer uid = (Integer) session.getAttribute("loginUserId");
        if (uid == null) return "redirect:/login";
        size = 18;
        List<Output> all = outputService.findByUserAndCategory(uid, "学習");
        return paginateTo("learn/list", all, page, size, model);
    }

    @GetMapping("/health")
    public String health(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                         @RequestParam(value = "size", required = false, defaultValue = "18") int size,
                         Model model, HttpSession session) {
        Integer uid = (Integer) session.getAttribute("loginUserId");
        if (uid == null) return "redirect:/login";
        size = 18;
        List<Output> all = outputService.findByUserAndCategory(uid, "健康");
        return paginateTo("health/list", all, page, size, model);
    }

    @GetMapping("/work")
    public String work(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                       @RequestParam(value = "size", required = false, defaultValue = "18") int size,
                       Model model, HttpSession session) {
        Integer uid = (Integer) session.getAttribute("loginUserId");
        if (uid == null) return "redirect:/login";
        size = 18;
        List<Output> all = outputService.findByUserAndCategory(uid, "仕事");
        return paginateTo("work/list", all, page, size, model);
    }

    @GetMapping("/life")
    public String life(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                       @RequestParam(value = "size", required = false, defaultValue = "18") int size,
                       Model model, HttpSession session) {
        Integer uid = (Integer) session.getAttribute("loginUserId");
        if (uid == null) return "redirect:/login";
        size = 18;
        List<Output> all = outputService.findByUserAndCategory(uid, "生活");
        return paginateTo("life/list", all, page, size, model);
    }

    // 共通ページング（前/次ナビと items サブリストを詰める）
    private String paginateTo(String view, List<Output> all, int page, int size, Model model) {
        int total = all == null ? 0 : all.size();
        int totalPages = (total + size - 1) / size;
        if (totalPages <= 0) totalPages = 1; // 空でも1ページ扱い
        if (page < 1) page = 1;
        if (page > totalPages) page = totalPages;
        int from = (page - 1) * size;
        int to = Math.min(from + size, total);
        List<Output> items = total == 0 ? List.of() : all.subList(from, to);
        model.addAttribute("items", items);
        model.addAttribute("hasPrev", page > 1);
        model.addAttribute("hasNext", page < totalPages);
        model.addAttribute("prevPage", Math.max(1, page - 1));
        model.addAttribute("nextPage", Math.min(totalPages, page + 1));
        return view;
    }
}