package com.example.outputmanager.controller;

import com.example.outputmanager.form.UserForm;
import com.example.outputmanager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/users/register")
    public String registerForm(Model model) {
        model.addAttribute("userForm", new UserForm());
        return "users/register";
    }

    @PostMapping("/users/register")
    public String registerSubmit(@Valid @ModelAttribute("userForm") UserForm form, Errors errors, Model model) {
        if (errors.hasErrors()) return "users/register";
        try {
            userService.registerNewUser(form.toEntity());
        } catch (IllegalArgumentException e) {
            errors.reject("registerError", e.getMessage());
            return "users/register";
        }
        return "redirect:/";
    }
}
