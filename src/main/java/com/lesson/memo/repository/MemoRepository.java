package com.lesson.memo.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.lesson.memo.model.Memo;

@Repository
public interface MemoRepository extends JpaRepository<Memo, Long> {
    // Service側でソートを行うため、Repository側では単純な検索メソッドのみ定義
    List<Memo> findByTitleContainingOrContentContaining(String title, String content);
}