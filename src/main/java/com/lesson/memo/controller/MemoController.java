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

import com.lesson.memo.model.Memo;
import com.lesson.memo.repository.MemoRepository;
import com.lesson.memo.model.Priority;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/memo")
public class MemoController {

    @Autowired
    private MemoRepository memoRepository;

    // 一覧画面表示：優先度順にソート
    @GetMapping
    public String list(Model model) {
        List<Memo> memos = memoRepository.findAll().stream()
        		.sorted((m1, m2) -> {
        			if (m1.getPriority() == null) return 1;
                    if (m2.getPriority() == null) return -1;
                    return m1.getPriority().compareTo(m2.getPriority());
                })
        		.toList();
        model.addAttribute("memos", memos);
        return "memo-list";
    }

    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("memo", new Memo());
        model.addAttribute("priorities", Priority.values());
        return "memo-form";
    }

    // 新規保存：バリデーションエラー時にprioritiesを渡す
    @PostMapping("/create")
    public String create(@ModelAttribute @Valid Memo memo,
            BindingResult result, Model model) {
        if (result.hasErrors()) {
        	model.addAttribute("priorities", Priority.values());
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
    	model.addAttribute("priorities", Priority.values());
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
            Model model,
            HttpServletResponse response) {

        Optional<Memo> opt = memoRepository.findById(id);
        if (opt.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return "not-found"; // エラー画面表示
        }

        Memo memoToUpdate = opt.get();

        if (result.hasErrors()) {
            model.addAttribute("priorities", Priority.values());
            return "memo-form";
        }

        memoToUpdate.setTitle(memo.getTitle());
        memoToUpdate.setContent(memo.getContent());
        memoToUpdate.setPriority(memo.getPriority());
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
}
