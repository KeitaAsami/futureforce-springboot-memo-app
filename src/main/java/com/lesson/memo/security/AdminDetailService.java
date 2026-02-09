package com.lesson.memo.security;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.lesson.memo.repository.AdminRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminDetailService implements UserDetailsService {
	private final com.lesson.memo.repository.AdminRepository adminRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // メールアドレスを使ってDBからユーザー情報を取得
		return adminRepository.findByEmail(email)
				.map(admin -> {
                    // adminエンティティをUserDetails形式に変換
					return new User(
							admin.getEmail(),
							admin.getPassword(),
							AuthorityUtils.createAuthorityList(admin.getRole()));
				})
				.orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりません:" + email));
	}
}
