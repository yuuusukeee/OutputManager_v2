package com.example.outputmanager.controller;

import java.util.LinkedHashMap;          // ★追加
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.outputmanager.domain.Category;
import com.example.outputmanager.domain.Output;
import com.example.outputmanager.form.OutputForm;
import com.example.outputmanager.service.CategoryService;
import com.example.outputmanager.service.FavoriteService;
import com.example.outputmanager.service.OutputService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/outputs")
@RequiredArgsConstructor
public class OutputController {

    private final OutputService outputService;
    private final CategoryService categoryService;
    private final FavoriteService favoriteService;

    /** 一覧（検索・カテゴリ・お気に入り対応 & カテゴリ別ブロック描画用データ供給） */
    @GetMapping
    public String list(@RequestParam(required = false) String keyword,
                       @RequestParam(required = false) Integer categoryId,
                       Model model,
                       HttpSession session) {

        Integer userId = (Integer) session.getAttribute("loginUserId");
        if (userId == null) return "redirect:/";

        // 自分のお気に入りID一覧（★表示／並べ替え／フィルタに使用）
        List<Integer> favIds = favoriteService.getFavoriteIdsByUser(userId);

        // データ取得：カテゴリ=-1 なら「お気に入りだけ」、他は通常検索/一覧
        List<Output> outputs;
        if (categoryId != null && categoryId == -1) {   // 「お気に入り」擬似カテゴリ
            outputs = outputService.getOutputList(userId).stream()
                    .filter(o -> favIds.contains(o.getId()))
                    .toList();
        } else if (keyword != null || categoryId != null) {
            outputs = outputService.searchOutputs(keyword, categoryId, userId);
        } else {
            outputs = outputService.getOutputList(userId);
        }

        // お気に入りを上位に寄せる（true<false の性質を利用）
        outputs = outputs.stream()
                .sorted((a, b) -> Boolean.compare(
                        !favIds.contains(a.getId()),
                        !favIds.contains(b.getId())))
                .toList();

        // カテゴリ情報（見出しや名称表示用）
        List<Category> cats = categoryService.getAllCategories();
        Map<Integer, String> categoryNameMap = cats.stream()
                .collect(Collectors.toMap(Category::getId, Category::getName));

        // ★カテゴリ別ブロックUI用：順序が安定するよう LinkedHashMap でグルーピング
        Map<Integer, List<Output>> grouped = outputs.stream()
                .collect(Collectors.groupingBy(
                        Output::getCategoryId,
                        LinkedHashMap::new,                 // ←ココがポイント
                        Collectors.toList()
                ));

        model.addAttribute("outputs", outputs);
        model.addAttribute("grouped", grouped);
        model.addAttribute("favorites", favIds);
        model.addAttribute("categories", cats);
        model.addAttribute("categoryNameMap", categoryNameMap); // list.html で名前解決に使う
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryId", categoryId);
        return "outputs/list";
    }

    /** 詳細 */
    @GetMapping("/{id}")
    public String detail(@PathVariable Integer id, Model model, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("loginUserId");
        if (userId == null) return "redirect:/";

        Output output = outputService.getOutputById(id);
        boolean isFavorite = favoriteService.isFavorite(userId, id);

        Map<Integer, String> categoryNameMap = categoryService.getAllCategories().stream()
                .collect(Collectors.toMap(Category::getId, Category::getName));

        boolean owner = output != null && output.getUserId() != null && output.getUserId().equals(userId);

        model.addAttribute("output", output);
        model.addAttribute("isFavorite", isFavorite);
        model.addAttribute("owner", owner);
        model.addAttribute("categoryNameMap", categoryNameMap);
        return "outputs/detail";
    }

    /** 新規作成 画面 */
    @GetMapping("/add")
    public String addForm(Model model, HttpSession session) {
        if ((Integer) session.getAttribute("loginUserId") == null) return "redirect:/";
        model.addAttribute("outputForm", new OutputForm());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "outputs/save";
    }

    /** 登録 */
    @PostMapping("/save")
    public String save(@Valid @ModelAttribute OutputForm form,
                       BindingResult br,
                       HttpSession session,
                       RedirectAttributes ra,
                       Model model) {
        Integer loginUserId = (Integer) session.getAttribute("loginUserId");
        if (loginUserId == null) return "redirect:/";

        if (br.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            return "outputs/save";
        }
        Output out = form.toEntity(loginUserId);
        outputService.addOutput(out);
        ra.addFlashAttribute("msg", "保存しました");
        return "redirect:/outputs";
    }

    /** 編集 画面 */
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Integer id, Model model, HttpSession session) {
        if ((Integer) session.getAttribute("loginUserId") == null) return "redirect:/";
        Output output = outputService.getOutputById(id);
        model.addAttribute("outputForm", OutputForm.fromEntity(output));
        model.addAttribute("categories", categoryService.getAllCategories());
        return "outputs/save";
    }

    /** 更新（“増殖”防止のため id+userId で update） */
    @PostMapping("/{id}/update")
    public String update(@PathVariable Integer id,
                         @Valid @ModelAttribute("outputForm") OutputForm form,
                         Errors errors,
                         Model model,
                         HttpSession session,
                         RedirectAttributes ra) {
        Integer userId = (Integer) session.getAttribute("loginUserId");
        if (userId == null) return "redirect:/";

        if (errors.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            return "outputs/save";
        }
        Output entity = form.toEntity(userId);
        entity.setId(id);
        int updated = outputService.updateOutput(entity); // 0/1
        if (updated == 0) {
            ra.addFlashAttribute("error", "更新できませんでした（権限/同時更新の可能性）");
        }
        return "redirect:/outputs";
    }

    /** 削除 */
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, HttpSession session) {
        if ((Integer) session.getAttribute("loginUserId") == null) return "redirect:/";
        outputService.deleteOutput(id);
        return "redirect:/outputs";
    }
}