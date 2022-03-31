package com.example.my_todo_application.security.model;

import com.example.my_todo_application.task_management.domain.model.AppUser;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


public class AppUserDetails implements UserDetails {

    /** 元になる {@link AppUser}クラス */
    @Getter
    private final AppUser appUser;

    /** パスワード */
    private final String password;

    /** ユーザー名(メールアドレス) */
    private final String userName;

    /** ロール情報 */
    private final List<GrantedAuthority> authorities;

    public AppUserDetails(AppUser appUser) {
        this.appUser = appUser;
        this.password = appUser.getPassword();
        this.userName = appUser.getMailAddress();
        this.authorities = AuthorityUtils.createAuthorityList(appUser.getRole().getRoleName());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
