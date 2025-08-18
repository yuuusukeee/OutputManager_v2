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
 * /outputs の model に DB から取得したリストを詰める（null禁止）。
 */
@Controller
@RequiredArgsConstructor
public class OutputController {

    private final OutputService outputService;

    /** テンプレと揃えたプレースホルダ画像パス */
    private static final String PLACEHOLDER_ICON = "/img/placeholder.png";

    /** 一覧トップ（横スライド：最近/お気に入り/学習/健康/仕事/生活） */
    @GetMapping("/outputs")
    public String outputs(
            Model model,
            HttpSession session,
            @RequestParam(name = "keyword", required = false) String keyword) {

        // ログイン確認
        Integer uid = (Integer) session.getAttribute("loginUserId");
        if (uid == null) return "redirect:/login";

        // カテゴリ別（「お気に入り」を除外）
        List<Output> learn  = outputService.findByCategoryExcludingFavorite(uid, "学習");
        List<Output> health = outputService.findByCategoryExcludingFavorite(uid, "健康");
        List<Output> work   = outputService.findByCategoryExcludingFavorite(uid, "仕事");
        List<Output> life   = outputService.findByCategoryExcludingFavorite(uid, "生活");

        // null→[] と icon フォールバックを適用
        model.addAttribute("learn",  safeList(learn));
        model.addAttribute("health", safeList(health));
        model.addAttribute("work",   safeList(work));
        model.addAttribute("life",   safeList(life));

        // 「最近」「お気に入り」：今は空で安全に
        model.addAttribute("recent",    Collections.emptyList());
        model.addAttribute("favorites", Collections.emptyList());

        model.addAttribute("keyword", keyword);
        return "outputs/index"; // templates/outputs/index.html
    }

    /** 詳細ページ（categoryLabel を組み立ててテンプレを簡潔に） */
    @GetMapping("/outputs/{id}")
    public String detail(
            @PathVariable("id") Long id,
            Model model,
            HttpSession session) {

        Integer uid = (Integer) session.getAttribute("loginUserId");
        if (uid == null) return "redirect:/login";

        Output o = outputService.findById(id);
        if (o == null) return "redirect:/outputs";

        // 単体でも icon フォールバック
        if (o.getIcon() == null || o.getIcon().isBlank()) {
            o.setIcon(PLACEHOLDER_ICON);
        }
        model.addAttribute("output", o);

        // カテゴリ表示用のラベル（Map 未配線でも落ちない）
        String categoryLabel = (o.getCategoryId() == null)
                ? "未分類"
                : String.valueOf(o.getCategoryId());
        model.addAttribute("categoryLabel", categoryLabel);

        return "outputs/detail"; // templates/outputs/detail.html
    }

    /** 旧導線の互換：/outputs/new → /outputs/save へ寄せる */
    @GetMapping("/outputs/new")
    public String newToSave() {
        return "redirect:/outputs/save";
    }

    /** 新規作成フォーム */
    @GetMapping("/outputs/save")
    public String showCreateForm(Model model, HttpSession session) {
        if (session.getAttribute("loginUserId") == null) return "redirect:/login";

        if (!model.containsAttribute("outputForm")) {
            model.addAttribute("outputForm", new OutputForm());
        }
        model.addAttribute("categories", Collections.emptyList());
        return "outputs/save";
    }

    /** 新規作成（最小ダミー：保存実装は後で差し替え） */
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
        // TODO: OutputService で保存へ
        return "redirect:/outputs";
    }

    /** 編集フォーム */
    @GetMapping("/outputs/{id}/edit")
    public String showEditForm(
            @PathVariable("id") Integer id,
            Model model,
            HttpSession session) {

        if (session.getAttribute("loginUserId") == null) return "redirect:/login";
        // TODO: サービスで id の内容を取得して OutputForm に詰める
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
        // TODO: OutputService で更新へ
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
        model.addAttribute("items", items != null ? items : Collections.emptyList());
        return "outputs/category";
    }

    /** カテゴリ：健康（3カラム） */
    @GetMapping("/health")
    public String health(Model model, HttpSession session) {
        Integer uid = (Integer) session.getAttribute("loginUserId");
        if (uid == null) return "redirect:/login";
        model.addAttribute("pageTitle", "健康");
        List<Output> items = outputService.findByCategoryExcludingFavorite(uid, "健康");
        model.addAttribute("items", items != null ? items : Collections.emptyList());
        return "outputs/category";
    }

    /** カテゴリ：仕事（3カラム） */
    @GetMapping("/work")
    public String work(Model model, HttpSession session) {
        Integer uid = (Integer) session.getAttribute("loginUserId");
        if (uid == null) return "redirect:/login";
        model.addAttribute("pageTitle", "仕事");
        List<Output> items = outputService.findByCategoryExcludingFavorite(uid, "仕事");
        model.addAttribute("items", items != null ? items : Collections.emptyList());
        return "outputs/category";
    }

    /** カテゴリ：生活（3カラム） */
    @GetMapping("/life")
    public String life(Model model, HttpSession session) {
        Integer uid = (Integer) session.getAttribute("loginUserId");
        if (uid == null) return "redirect:/login";
        model.addAttribute("pageTitle", "生活");
        List<Output> items = outputService.findByCategoryExcludingFavorite(uid, "生活");
        model.addAttribute("items", items != null ? items : Collections.emptyList());
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

    /** 一覧の null→[] と icon フォールバックを行うユーティリティ */
    private List<Output> safeList(List<Output> list) {
        if (list == null) return Collections.emptyList();
        for (Output o : list) {
            if (o != null && (o.getIcon() == null || o.getIcon().isBlank())) {
                o.setIcon(PLACEHOLDER_ICON);
            }
        }
        return list;
    }
}