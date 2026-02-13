package com.lesson.memo.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lesson.memo.model.Memo;
import com.lesson.memo.repository.MemoRepository;

@Service
public class MemoService {

    @Autowired
    private MemoRepository memoRepository;

    //　キーワードで検索し、優先度順にソートしたリストを返す
    //　キーワードが null または空の場合は全件を返す
    public List<Memo> searchMemos(String keyword) {
        List<Memo> memos;

        //　データの取得
        if (keyword != null && !keyword.trim().isEmpty()) {
            // キーワードがある場合
            memos = memoRepository.findByTitleContainingOrContentContaining(keyword, keyword);
        } else {
            // キーワードがない（一覧表示）場合
            memos = memoRepository.findAll();
        }

        //　ソート処理
        return sortMemos(memos);
    }

    // 内部で使うソート用のメソッド
    private List<Memo> sortMemos(List<Memo> memos) {
        return memos.stream()
                .sorted((m1, m2) -> {
                    if (m1.getPriority() == null) return 1;
                    if (m2.getPriority() == null) return -1;
                    return m1.getPriority().compareTo(m2.getPriority());
                })
                .toList();
    }
}