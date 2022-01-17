package com.example.my_todo_application.security.service;

import com.example.my_todo_application.security.model.AppUserDetails;
import com.example.my_todo_application.task_management.domain.model.AppUser;
import com.example.my_todo_application.task_management.domain.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component("appUserDetailsService")
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

    private AppUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(!StringUtils.hasText(username)){
            throw new UsernameNotFoundException("ユーザー名が指定されていません");
        }

        AppUser appUser = userRepository.findByMailAddress(username);


        if(appUser == null){
            throw new UsernameNotFoundException("指定されたユーザーが存在しません");
        }
        return new AppUserDetails(appUser);
    }
}
