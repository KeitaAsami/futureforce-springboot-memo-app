package com.lesson.memo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
		    .authorizeHttpRequests(auth -> auth
                // 認証なしで許可するリクエスト
		    	.requestMatchers("/admin/signup", "/admin/signin", "/css/**", "/js/**", "/h2-console/**").permitAll()
                // それ以外は全て認証が必要
		    	.anyRequest().authenticated()
            )
		    .formLogin(login -> login
                // ログインページのURL設定
		    	.loginPage("/admin/signin")
                // ログイン処理のURL
		    	.loginProcessingUrl("/admin/signin")
                // ログインに「email」を使用するために指定
		    	.usernameParameter("email")
                // ログイン成功時の遷移先（メモ一覧画面へ）
		    	.defaultSuccessUrl("/memo", true)
		    	.permitAll()
		    )
		    .logout(logout -> logout
                // ログアウト処理のURL
		    	.logoutUrl("/admin/logout")
                // ログアウト成功時の遷移先
		    	.logoutSuccessUrl("/admin/signin?logout")
		    	.permitAll()
		    );
        // H2コンソールを使用するための開発用設定
		http.csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"));
		http.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

		return http.build();
	}

    // パスワードエンコーダーのBean定義
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
