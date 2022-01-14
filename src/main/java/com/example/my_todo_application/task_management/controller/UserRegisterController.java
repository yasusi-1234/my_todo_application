package com.example.my_todo_application.task_management.controller;

import com.example.my_todo_application.task_management.controller.form.UserRegisterForm;
import com.example.my_todo_application.task_management.domain.model.AppUser;
import com.example.my_todo_application.task_management.domain.service.UserRegisterService;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class UserRegisterController {

    UserRegisterService userRegisterService;

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

        // リダイレクト先の指定
        String uri = MvcUriComponentsBuilder.relativeTo(uriBuilder)
                .withMethodName(LoginController.class, "getLogin")
                .build().toString();

        redirectAttributes.addFlashAttribute("complete", "登録が完了しました");

        return "redirect:" + uri;
    }


}
