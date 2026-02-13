package com.lesson.memo.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lesson.memo.model.Admin;
import com.lesson.memo.repository.AdminRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
	private final AdminRepository adminRepository;
	private final PasswordEncoder passwordEncoder; // パスワード暗号化用

    // ログイン画面表示
    @GetMapping("/signin")
    public String showSignin() {
    	return "admin/signin";
    }

	// 登録画面表示
	@GetMapping("/signup")
	public String showSignupForm(@ModelAttribute Admin admin) {
		return "admin/signup";
	}

    // 登録処理
	@PostMapping("/signup")
	public String signup(@Validated @ModelAttribute Admin admin, BindingResult result) {
		if(result.hasErrors()) {
			return "admin/signup";
		}
        // パスワード暗号化
		admin.setPassword(passwordEncoder.encode(admin.getPassword()));
		admin.setRole("ROLE_ADMIN");
		adminRepository.save(admin);

        // 登録後ログイン画面へ
		return "redirect:/admin/signin";
	}
}
