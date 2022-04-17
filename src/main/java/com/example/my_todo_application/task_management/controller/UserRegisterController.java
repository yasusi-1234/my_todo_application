package com.example.my_todo_application.task_management.controller;

import com.example.my_todo_application.security.model.AppUserDetails;
import com.example.my_todo_application.task_management.controller.form.UserRegisterForm;
import com.example.my_todo_application.task_management.domain.model.AppUser;
import com.example.my_todo_application.task_management.domain.service.UserRegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
@RequestMapping("user")
public class UserRegisterController {

    private final UserRegisterService userRegisterService;

    private final UserDetailsService appUserDetailsService;

    @Autowired
    public UserRegisterController(
            UserRegisterService userRegisterService,
            @Qualifier("appUserDetailsService") UserDetailsService appUserDetailsService) {
        this.userRegisterService = userRegisterService;
        this.appUserDetailsService = appUserDetailsService;
    }

    @GetMapping("register")
    public String getUserRegister(
            @ModelAttribute("userRegisterForm")UserRegisterForm userRegisterForm){
        return "user/user-register";
    }

    @PostMapping("register")
    public String postUserRegister(
            @Validated @ModelAttribute("userRegisterForm")UserRegisterForm userRegisterForm,
            BindingResult bindingResult,
            UriComponentsBuilder uriBuilder,
            RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()){
            return "user/user-register";
        }

        userRegisterService.saveAppUser(
            userRegisterForm.getFirstName(),
            userRegisterForm.getLastName(),
            userRegisterForm.getMailAddress(),
            userRegisterForm.getPassword()
        );

        setAuthentication(userRegisterForm.getMailAddress());

        // リダイレクト先の指定
        String uri = MvcUriComponentsBuilder.relativeTo(uriBuilder)
                .withMappingName("TC#getTaskHome")
                .build().toString();

        redirectAttributes.addFlashAttribute("complete", "登録が完了しました");

        return "redirect:" + uri;
    }

    private void setAuthentication(String username) {
        UserDetails userDetails = appUserDetailsService.loadUserByUsername(username);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


}
