package com.lesson.memo.model;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Entity
@Data
public class Memo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "タイトルを入力してください")
    @Column(nullable = false, length = 100)
    private String title;

    @NotBlank(message = "内容を入力してください")
    @Column(nullable = false, length = 1000)
    private String content;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    @NotNull(message = "優先順位を選択してください")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority;
    public Priority getPriority() {
    	return priority;
    }
}