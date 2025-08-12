package com.example.outputmanager.controller;

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
@RequestMapping("/outputs") // ★クラスに付ける（メソッド側は二重にしない）
@RequiredArgsConstructor
public class OutputController {

    private final OutputService outputService;
    private final CategoryService categoryService;
    private final FavoriteService favoriteService;

    /** 一覧（検索対応） */
    @GetMapping
    public String list(@RequestParam(required = false) String keyword,
                       @RequestParam(required = false) Integer categoryId,
                       Model model, HttpSession session) {
        Integer loginUserId = (Integer) session.getAttribute("loginUserId");
        if (loginUserId == null) return "redirect:/login";

        List<Output> outputs = (keyword != null || categoryId != null)
                ? outputService.searchOutputs(keyword, categoryId, loginUserId)
                : outputService.getOutputList(loginUserId);

        List<Category> cats = categoryService.getAllCategories();
        Map<Integer, String> categoryNameMap = cats.stream()
                .collect(Collectors.toMap(Category::getId, Category::getName));

        model.addAttribute("outputs", outputs);
        model.addAttribute("categories", cats);
        model.addAttribute("categoryNameMap", categoryNameMap);
        model.addAttribute("favorites", favoriteService.getFavoriteIdsByUser(loginUserId));
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryId", categoryId);
        return "outputs/list";
    }

    /** 詳細 */
    @GetMapping("/{id}")
    public String detail(@PathVariable Integer id, Model model, HttpSession session) {
        Integer loginUserId = (Integer) session.getAttribute("loginUserId");
        if (loginUserId == null) return "redirect:/login";

        Output output = outputService.getOutputById(id);
        boolean owner = output != null && output.getUserId() != null
                && output.getUserId().equals(loginUserId);

        Map<Integer, String> categoryNameMap = categoryService.getAllCategories().stream()
                .collect(Collectors.toMap(Category::getId, Category::getName));

        model.addAttribute("output", output);
        model.addAttribute("owner", owner);
        model.addAttribute("categoryNameMap", categoryNameMap);
        model.addAttribute("isFavorite", output != null && favoriteService.isFavorite(loginUserId, id));
        model.addAttribute("favorites", favoriteService.getFavoriteIdsByUser(loginUserId));
        return "outputs/detail";
    }

    /** 新規作成 画面 */
    @GetMapping("/add")
    public String addForm(Model model, HttpSession session) {
        Integer loginUserId = (Integer) session.getAttribute("loginUserId");
        if (loginUserId == null) return "redirect:/login";

        model.addAttribute("outputForm", new OutputForm());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "outputs/save";
    }

    /** 新規保存（INSERT） */
    @PostMapping("/save") // ★/outputs + /save = /outputs/save
    public String save(@Valid @ModelAttribute OutputForm form,
                       BindingResult br,
                       HttpSession session,
                       RedirectAttributes ra,
                       Model model) {
        Integer loginUserId = (Integer) session.getAttribute("loginUserId");
        if (loginUserId == null) return "redirect:/login";

        if (br.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            return "outputs/save";
        }

        outputService.addOutput(form.toEntity(loginUserId)); // INSERT
        ra.addFlashAttribute("msg", "保存しました");
        return "redirect:/outputs";
    }

    /** 編集 画面 */
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Integer id, Model model, HttpSession session) {
        Integer loginUserId = (Integer) session.getAttribute("loginUserId");
        if (loginUserId == null) return "redirect:/login";

        Output output = outputService.getOutputById(id);
        model.addAttribute("outputForm", OutputForm.fromEntity(output)); // ★idもセットされる
        model.addAttribute("categories", categoryService.getAllCategories());
        return "outputs/save";
    }

    /** 更新（UPDATE） */
    @PostMapping("/{id}/update")
    public String update(@PathVariable Integer id,
                         @Valid @ModelAttribute("outputForm") OutputForm form,
                         Errors errors,
                         Model model,
                         HttpSession session,
                         RedirectAttributes ra) {
        Integer loginUserId = (Integer) session.getAttribute("loginUserId");
        if (loginUserId == null) return "redirect:/login";

        if (errors.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            return "outputs/save";
        }

        Output entity = form.toEntity(loginUserId);
        entity.setId(id); // ★URLのidで最終確定（hiddenと二重保険）

        int updated = outputService.updateOutput(entity); // ★更新件数で判定
        if (updated == 0) {
            ra.addFlashAttribute("error", "更新対象がないか、権限がありません。");
        } else {
            ra.addFlashAttribute("msg", "更新しました");
        }
        return "redirect:/outputs";
    }

    /** 削除 */
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, HttpSession session, RedirectAttributes ra) {
        Integer loginUserId = (Integer) session.getAttribute("loginUserId");
        if (loginUserId == null) return "redirect:/login";

        outputService.deleteOutput(id);
        ra.addFlashAttribute("msg", "削除しました");
        return "redirect:/outputs";
    }
}