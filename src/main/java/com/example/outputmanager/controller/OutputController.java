package com.example.outputmanager.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    private final OutputService outputService;     // addOutput / updateOutput / deleteOutput / getOutputById / searchOutputs / getOutputList
    private final CategoryService categoryService; // getAllCategories
    private final FavoriteService favoriteService; // isFavorite / getFavoriteIdsByUser

    /** 一覧（キーワード＆カテゴリ検索対応） */
    @GetMapping
    public String list(@RequestParam(required = false) String keyword,
                       @RequestParam(required = false) Integer categoryId,
                       Model model,
                       HttpSession session) {
    	Integer userId = (Integer) session.getAttribute("userId");
    	System.out.println("[DEBUG] userId=" + userId + " sessionId=" + session.getId());
        if (userId == null) return "redirect:/";

        List<Output> outputs = (keyword != null || categoryId != null)
                ? outputService.searchOutputs(keyword, categoryId, userId)
                : outputService.getOutputList(userId);

        List<Category> cats = categoryService.getAllCategories();
        Map<Integer, String> categoryNameMap = cats.stream()
                .collect(Collectors.toMap(Category::getId, Category::getName));

        model.addAttribute("outputs", outputs);
        model.addAttribute("categories", cats);
        model.addAttribute("categoryNameMap", categoryNameMap);
        model.addAttribute("favorites", favoriteService.getFavoriteIdsByUser(userId));
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryId", categoryId);
        return "outputs/list";
    }

    /** 詳細 */
    @GetMapping("/{id}")
    public String detail(@PathVariable Integer id, Model model, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) return "redirect:/";

        Output output = outputService.getOutputById(id);
        boolean isFavorite = favoriteService.isFavorite(userId, id);

        Map<Integer, String> categoryNameMap = categoryService.getAllCategories().stream()
                .collect(Collectors.toMap(Category::getId, Category::getName));

        model.addAttribute("output", output);
        model.addAttribute("isFavorite", isFavorite);
        model.addAttribute("categoryNameMap", categoryNameMap);
        return "outputs/detail";
    }

    /** 新規作成 画面 */
    @GetMapping("/add")
    public String addForm(Model model, HttpSession session) {
        if ((Integer) session.getAttribute("userId") == null) return "redirect:/";
        var cats = categoryService.getAllCategories();
        System.out.println("[DEBUG] categories size=" + (cats == null ? "null" : cats.size())); // ★これ
        model.addAttribute("outputForm", new OutputForm());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "outputs/save";
    }

    /** 新規作成 保存（/outputs/save） */
    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("outputForm") OutputForm form,
                       Errors errors,
                       Model model,
                       HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) return "redirect:/";

        if (errors.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            return "outputs/save";
        }
        outputService.addOutput(form.toEntity(userId));
        return "redirect:/outputs";
    }

    /** 編集 画面 */
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Integer id, Model model, HttpSession session) {
        if ((Integer) session.getAttribute("userId") == null) return "redirect:/";

        Output output = outputService.getOutputById(id);
        model.addAttribute("outputForm", OutputForm.fromEntity(output));
        model.addAttribute("categories", categoryService.getAllCategories());
        return "outputs/save";
    }

    /** 更新（/outputs/{id}/update） */
    @PostMapping("/{id}/update")
    public String update(@PathVariable Integer id,
                         @Valid @ModelAttribute("outputForm") OutputForm form,
                         Errors errors,
                         Model model,
                         HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) return "redirect:/";

        if (errors.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            return "outputs/save";
        }
        Output entity = form.toEntity(userId);
        entity.setId(id); // hiddenと二重保険
        outputService.updateOutput(entity);
        return "redirect:/outputs";
    }

    /** 削除（list.html が /outputs/delete/{id} をPOSTする想定） */
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, HttpSession session) {
        if ((Integer) session.getAttribute("userId") == null) return "redirect:/";
        outputService.deleteOutput(id);
        return "redirect:/outputs";
    }
}