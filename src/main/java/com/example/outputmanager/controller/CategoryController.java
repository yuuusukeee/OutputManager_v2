package com.example.outputmanager.controller;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.outputmanager.domain.Category;
import com.example.outputmanager.service.CategoryService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories()); // 一覧をモデルに詰める
        return "categories/list";                                             // 一覧テンプレートへ
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("category", new Category()); // 空のCategoryでフォーム初期化
        return "categories/save";                       // 入力画面
    }

    @PostMapping("/add")
    public String addSubmit(@Valid @ModelAttribute("category") Category category, Errors errors) {
        if (errors.hasErrors()) return "categories/save"; // 入力エラー時は戻る
        try {
            categoryService.addCategory(category);        // 追加
        } catch (IllegalArgumentException e) {            // 重複などの業務エラー
            errors.reject("categoryError", e.getMessage());
            return "categories/save";
        }
        return "redirect:/categories"; // 成功時は一覧へ
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Integer id, Model model) {
        model.addAttribute("category", categoryService.getCategoryById(id)); // 既存データをフォームに
        return "categories/save";
    }

    @PostMapping("/edit/{id}")
    public String editSubmit(@PathVariable Integer id, @Valid @ModelAttribute("category") Category category, Errors errors) {
        if (errors.hasErrors()) return "categories/save"; // 入力エラー時は戻る
        category.setId(id);                                // パスのidを反映
        categoryService.editCategory(category);            // 更新
        return "redirect:/categories";                     // 一覧へ
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, Model model) {
        try {
            categoryService.deleteCategory(id); // 参照制約などの業務エラー対策
        } catch (IllegalStateException e) {
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("categoryDeleteError", e.getMessage());
            return "categories/list";
        }
        return "redirect:/categories";
    }
}