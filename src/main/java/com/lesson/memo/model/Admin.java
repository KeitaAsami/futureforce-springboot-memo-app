package com.lesson.memo.model;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.Data;

@Entity
@Table(name = "admins")
@Data
public class Admin {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

    // 姓
	@NotBlank
	@Column(name = "last_name", nullable = false)
	private String lastName;
    // 名
	@NotBlank
	@Column(name = "first_name", nullable = false)
	private String firstName;
    // メールアドレス（UNIQUE制約）
	@Email
	@Column(nullable = false, unique = true)
	private String email;
    // パスワード（暗号化必須）
	@NotBlank
	@Column(nullable = false)
	private String password;
	private String role;
    // 作成日時
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;
    // 更新日時
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

    // 保存前に日時を自動設定
	@PrePersist
	protected void onCreate() {
		createdAt = LocalDateTime.now();
		updatedAt = LocalDateTime.now();
	}
    // 更新前に日時の自動設定
	@PreUpdate
	protected void onUpdate() {
		updatedAt = LocalDateTime.now();
	}
}
