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

    // キーワードでメモ検索
	public List<Memo> searchMemos(String keyword) {
		return memoRepository.findByTitleContainingOrContentContainingOrderByIdDesc(keyword, keyword);
	}
}
