package com.lesson.memo.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.RequestParam;

import com.lesson.memo.model.Memo;
import com.lesson.memo.repository.MemoRepository;
import com.lesson.memo.service.MemoService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/memo")
public class MemoController {

    @Autowired
    private MemoRepository memoRepository;

    @Autowired
    private MemoService memoService;

    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("memo", new Memo());
        return "memo-form";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute @Valid Memo memo,
            BindingResult result) {
        if (result.hasErrors()) {
            return "memo-form";
        }

        memo.setCreatedAt(LocalDateTime.now());
        memo.setUpdatedAt(LocalDateTime.now());
        memoRepository.save(memo);
        return "redirect:/memo";
    }

    @GetMapping("/detail/{id}")
    public String showDetail(@PathVariable Long id, Model model,
            HttpServletResponse response) {
        Optional<Memo> memo = memoRepository.findById(id);
        if (memo.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "not-found"; // エラー画面にリダイレクト
        }

        model.addAttribute("memo", memo.get());
        return "memo-detail";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, HttpServletResponse response) {
        if (model.containsAttribute("memo")) {
            return "memo-form";
        }

        return memoRepository.findById(id)
                .map(memo -> {
                    model.addAttribute("memo", memo);
                    return "memo-form";
                })
                .orElseGet(() -> {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    return "not-found";
                });
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id,
            @ModelAttribute @Valid Memo memo,
            BindingResult result,
            HttpServletResponse response,
            RedirectAttributes redirectAttributes) {

        Optional<Memo> opt = memoRepository.findById(id);
        if (opt.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "not-found"; // エラー画面表示
        }

        Memo memoToUpdate = opt.get();

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.memo", result);
            redirectAttributes.addFlashAttribute("memo", memo);
            return "redirect:/memo/edit/" + id; // editにリダイレクト
        }

        memoToUpdate.setTitle(memo.getTitle());
        memoToUpdate.setContent(memo.getContent());
        memoToUpdate.setUpdatedAt(LocalDateTime.now());
        memoRepository.save(memoToUpdate);

        return "redirect:/memo/detail/" + id;
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id,
        HttpServletResponse response) {
        if (memoRepository.existsById(id)) {
            memoRepository.deleteById(id);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "not-found";
        }

        return "redirect:/memo";
    }

    // 全文検索
    @GetMapping
    public String search(@RequestParam(name = "keyword", required = false) String keyword, Model model) {
    	List<Memo> memos = memoService.searchMemos(keyword);

        // 条件分岐
    	if (keyword != null && !keyword.isEmpty()) {
    		memos = memoService.searchMemos(keyword);
    	} else {
    		memos = memoRepository.findAll();
    	}

    	model.addAttribute("memos", memos);
    	model.addAttribute("keyword", keyword);
    	return "memo-list";
    }
}