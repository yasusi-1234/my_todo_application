package com.example.my_todo_application.security.config;

import com.example.my_todo_application.security.service.AppUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
public class ToDoSecurity extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) {
        // セキュリティーを無視する静的リソースリクエストの設定
        web.ignoring().antMatchers("/style/**", "/js/**", "/img/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // アクセス権限の設定
        http.authorizeRequests()
                // login画面は許可
                .antMatchers("/login").permitAll()
                // その他は不可
                .anyRequest().authenticated();

        // ログインの設定
        http.formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                // ユーザーのパラメータ名
                .usernameParameter("username")
                // ユーザーのパスワード
                .passwordParameter("password")
                // ログイン成功時の遷移先
                .defaultSuccessUrl("/task/home", true)
                // ログイン失敗
                .failureUrl("/login?error=true");

        // ログアウトの設定
        http.logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                // ログアウト成功時の遷移先
                .logoutSuccessUrl("/login/login")
                // 削除するクッキー
                .deleteCookies("JSESSIONID")
                // ログアウト時のセッション破棄を有効化
                .invalidateHttpSession(true).permitAll();
    }

    @Autowired
    public void configureGlobal(
            AuthenticationManagerBuilder builder,
            @Qualifier("appUserDetailsService") UserDetailsService userDetailsService,
            PasswordEncoder encoder) throws Exception {
        // ログイン処理時にユーザー情報を、DBから取得
        builder.eraseCredentials(true).userDetailsService(userDetailsService).passwordEncoder(encoder);
    }
}
