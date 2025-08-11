package com.example.outputmanager.controller;

import com.example.outputmanager.domain.Output;
import com.example.outputmanager.domain.Category;
import com.example.outputmanager.service.FavoriteService;
import com.example.outputmanager.service.OutputService;
import com.example.outputmanager.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/favorites")
@RequiredArgsConstructor
public class FavoriteController {
    private final FavoriteService favoriteService;
    private final OutputService outputService;
    private final CategoryService categoryService;

    @GetMapping
    public String list(HttpSession session, Model model) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) return "redirect:/";
        List<Integer> ids = favoriteService.getFavoriteIdsByUser(userId);
        // N+1回避のための一括取得（Mapperに対応済）
        List<Output> favorites = ids.isEmpty() ? java.util.List.of() : outputService.searchOutputs(null, null, userId)
            .stream().filter(o -> ids.contains(o.getId())).collect(Collectors.toList());

        Map<Integer, String> categoryNameMap = categoryService.getAllCategories().stream()
            .collect(Collectors.toMap(Category::getId, Category::getName));

        model.addAttribute("favorites", favorites);
        model.addAttribute("categoryNameMap", categoryNameMap);
        return "favorites/list";
    }

    @PostMapping("/toggle")
    public String toggleFavorite(@RequestParam Integer outputId, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) return "redirect:/";
        boolean isFav = favoriteService.isFavorite(userId, outputId);
        if (isFav) favoriteService.removeFavorite(userId, outputId);
        else favoriteService.addFavorite(userId, outputId);
        return "redirect:/outputs";
    }
}
