package com.example.outputmanager.controller;

import com.example.outputmanager.domain.Category;
import com.example.outputmanager.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        return "categories/list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("category", new Category());
        return "categories/save";
    }

    @PostMapping("/add")
    public String addSubmit(@Valid @ModelAttribute("category") Category category, Errors errors) {
        if (errors.hasErrors()) return "categories/save";
        try {
            categoryService.addCategory(category);
        } catch (IllegalArgumentException e) {
            errors.reject("categoryError", e.getMessage());
            return "categories/save";
        }
        return "redirect:/categories";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Integer id, Model model) {
        model.addAttribute("category", categoryService.getCategoryById(id));
        return "categories/save";
    }

    @PostMapping("/edit/{id}")
    public String editSubmit(@PathVariable Integer id, @Valid @ModelAttribute("category") Category category, Errors errors) {
        if (errors.hasErrors()) return "categories/save";
        category.setId(id);
        categoryService.editCategory(category);
        return "redirect:/categories";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, Model model) {
        try {
            categoryService.deleteCategory(id);
        } catch (IllegalStateException e) {
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("categoryDeleteError", e.getMessage());
            return "categories/list";
        }
        return "redirect:/categories";
    }
}
