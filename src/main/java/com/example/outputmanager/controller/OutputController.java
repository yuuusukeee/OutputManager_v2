package com.example.outputmanager.controller;

import java.util.Collections;
import java.util.List;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.NotBlank;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.outputmanager.domain.Output;
import com.example.outputmanager.service.OutputService;

import lombok.RequiredArgsConstructor;

/**
 * 一覧/カテゴリ/新規・編集フォームを集約。
 * ★ポイント：/outputs の model に DB から取得したリストを詰める（null禁止）。
 */
@Controller
@RequiredArgsConstructor
public class OutputController {

    private final OutputService outputService;

    /** 一覧トップ（横スライド：最近/お気に入り/学習/健康/仕事/生活） */
    @GetMapping("/outputs")
    public String outputs(
            Model model,
            HttpSession session,
            @RequestParam(name = "keyword", required = false) String keyword) {

        // ログイン確認
        Integer uid = (Integer) session.getAttribute("loginUserId");
        if (uid == null) return "redirect:/login";

        // 既存サービスでカテゴリ別を取得（「お気に入り」を除外）
        List<Output> learn  = outputService.findByCategoryExcludingFavorite(uid, "学習");
        List<Output> health = outputService.findByCategoryExcludingFavorite(uid, "健康");
        List<Output> work   = outputService.findByCategoryExcludingFavorite(uid, "仕事");
        List<Output> life   = outputService.findByCategoryExcludingFavorite(uid, "生活");

        model.addAttribute("learn",  learn  != null ? learn  : Collections.emptyList());
        model.addAttribute("health", health != null ? health : Collections.emptyList());
        model.addAttribute("work",   work   != null ? work   : Collections.emptyList());
        model.addAttribute("life",   life   != null ? life   : Collections.emptyList());

        // 「最近」「お気に入り」：実装未確認のため安全に空
        model.addAttribute("recent",    Collections.emptyList());
        model.addAttribute("favorites", Collections.emptyList());

        // 表示用
        model.addAttribute("keyword", keyword);

        return "outputs/index"; // ← templates/outputs/index.html
    }

    /** ★ 詳細ページ（categoryNameMap を必ず詰める） */
    @GetMapping("/outputs/{id}")
    public String detail(
            @PathVariable("id") Long id,
            Model model,
            HttpSession session) {

        Integer uid = (Integer) session.getAttribute("loginUserId");
        if (uid == null) return "redirect:/login";

        Output o = outputService.findById(id);
        if (o == null) return "redirect:/outputs";

        model.addAttribute("output", o);

        // ★ null で落ちないよう、空Mapを必ず詰める（実際のMapを持っているならそちらをaddAttributeする）
        if (!model.containsAttribute("categoryNameMap")) {
            model.addAttribute("categoryNameMap", Collections.<Integer, String>emptyMap());
        }

        return "outputs/detail"; // ← templates/outputs/detail.html
    }

    /** 旧導線の互換：/outputs/new → /outputs/save へ寄せる */
    @GetMapping("/outputs/new")
    public String newToSave() {
        return "redirect:/outputs/save";
    }

    /** 新規作成フォーム（templates/outputs/save.html を表示） */
    @GetMapping("/outputs/save")
    public String showCreateForm(Model model, HttpSession session) {
        if (session.getAttribute("loginUserId") == null) return "redirect:/login";

        if (!model.containsAttribute("outputForm")) {
            model.addAttribute("outputForm", new OutputForm());
        }
        model.addAttribute("categories", Collections.emptyList());
        return "outputs/save";
    }

    /** 新規作成（最小ダミー：保存実装は後で既存サービスに差し替え） */
    @PostMapping("/outputs/save")
    public String create(
            @ModelAttribute("outputForm") OutputForm form,
            BindingResult bindingResult,
            HttpSession session,
            Model model) {

        if (session.getAttribute("loginUserId") == null) return "redirect:/login";
        if (form.getTitle() == null || form.getTitle().isBlank()) {
            bindingResult.rejectValue("title", "required", "タイトルを入力してください。");
            model.addAttribute("categories", Collections.emptyList());
            return "outputs/save";
        }
        // TODO: 既存の OutputService で保存へ
        return "redirect:/outputs";
    }

    /** 編集フォーム（templates/outputs/save.html を表示） */
    @GetMapping("/outputs/{id}/edit")
    public String showEditForm(
            @PathVariable("id") Integer id,
            Model model,
            HttpSession session) {

        if (session.getAttribute("loginUserId") == null) return "redirect:/login";
        // TODO: 既存のサービスで id の内容を取得して OutputForm に詰める
        OutputForm form = new OutputForm();
        form.setId(id);
        model.addAttribute("outputForm", form);
        model.addAttribute("categories", Collections.emptyList());
        return "outputs/save";
    }

    /** 更新（最小ダミー） */
    @PostMapping("/outputs/{id}/update")
    public String update(
            @PathVariable("id") Integer id,
            @ModelAttribute("outputForm") OutputForm form,
            BindingResult bindingResult,
            HttpSession session,
            Model model) {

        if (session.getAttribute("loginUserId") == null) return "redirect:/login";
        if (form.getTitle() == null || form.getTitle().isBlank()) {
            bindingResult.rejectValue("title", "required", "タイトルを入力してください。");
            model.addAttribute("categories", Collections.emptyList());
            return "outputs/save";
        }
        // TODO: 既存の OutputService で更新へ
        return "redirect:/outputs";
    }

    /** カテゴリ：お気に入り（3カラム） */
    @GetMapping("/favorites")
    public String favorites(Model model, HttpSession session) {
        Integer uid = (Integer) session.getAttribute("loginUserId");
        if (uid == null) return "redirect:/login";
        model.addAttribute("pageTitle", "お気に入り");
        model.addAttribute("items", Collections.emptyList()); // TODO 差し替え
        return "outputs/category";
    }

    /** カテゴリ：学習（3カラム） */
    @GetMapping("/learn")
    public String learn(Model model, HttpSession session) {
        Integer uid = (Integer) session.getAttribute("loginUserId");
        if (uid == null) return "redirect:/login";
        model.addAttribute("pageTitle", "学習");
        List<Output> items = outputService.findByCategoryExcludingFavorite(uid, "学習");
        model.addAttribute("items", items != null ? items : Collections.emptyList()); // ★ null防御
        return "outputs/category";
    }

    /** カテゴリ：健康（3カラム） */
    @GetMapping("/health")
    public String health(Model model, HttpSession session) {
        Integer uid = (Integer) session.getAttribute("loginUserId");
        if (uid == null) return "redirect:/login";
        model.addAttribute("pageTitle", "健康");
        List<Output> items = outputService.findByCategoryExcludingFavorite(uid, "健康");
        model.addAttribute("items", items != null ? items : Collections.emptyList()); // ★ null防御
        return "outputs/category";
    }

    /** カテゴリ：仕事（3カラム） */
    @GetMapping("/work")
    public String work(Model model, HttpSession session) {
        Integer uid = (Integer) session.getAttribute("loginUserId");
        if (uid == null) return "redirect:/login";
        model.addAttribute("pageTitle", "仕事");
        List<Output> items = outputService.findByCategoryExcludingFavorite(uid, "仕事");
        model.addAttribute("items", items != null ? items : Collections.emptyList()); // ★ null防御
        return "outputs/category";
    }

    /** カテゴリ：生活（3カラム） */
    @GetMapping("/life")
    public String life(Model model, HttpSession session) {
        Integer uid = (Integer) session.getAttribute("loginUserId");
        if (uid == null) return "redirect:/login";
        model.addAttribute("pageTitle", "生活");
        List<Output> items = outputService.findByCategoryExcludingFavorite(uid, "生活");
        model.addAttribute("items", items != null ? items : Collections.emptyList()); // ★ null防御
        return "outputs/category";
    }

    /* -------- save.html 用の最小フォームDTO -------- */
    public static class OutputForm {
        private Integer id;
        @NotBlank private String title;
        private String summary;
        private String detail;
        private Integer categoryId;
        private String icon;
        private String videoUrl;

        public Integer getId() { return id; } public void setId(Integer id) { this.id = id; }
        public String getTitle() { return title; } public void setTitle(String title) { this.title = title; }
        public String getSummary() { return summary; } public void setSummary(String summary) { this.summary = summary; }
        public String getDetail() { return detail; } public void setDetail(String detail) { this.detail = detail; }
        public Integer getCategoryId() { return categoryId; } public void setCategoryId(Integer categoryId) { this.categoryId = categoryId; }
        public String getIcon() { return icon; } public void setIcon(String icon) { this.icon = icon; }
        public String getVideoUrl() { return videoUrl; } public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }
    }
}