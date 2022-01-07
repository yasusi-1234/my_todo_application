package com.example.my_todo_application.task_management.domain.service;

import com.example.my_todo_application.task_management.domain.model.AppUser;
import com.example.my_todo_application.task_management.domain.reository.AppUserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("UserRegisterServiceImplクラスのテスト")
@SpringBootTest
class UserRegisterServiceImplTest {

    @Autowired
    UserRegisterService userRegisterService;

    @MockBean
    AppUserRepository appUserRepository;

    @DisplayName("saveAppUserメソッドはuppUserRepositoryのメソッドを呼び出しその結果を返却する")
    @Test
    void saveAppUserTest() {
        // setup
        AppUser expected = testAppUser();
        doReturn(expected).when(appUserRepository).save(any(AppUser.class));

        AppUser actual = userRegisterService.saveAppUser(
                "test", "test", "mail@xx.xx.xx", "password"
        );

        assertEquals(expected, actual);
        verify(appUserRepository, times(1)).save(any(AppUser.class));
    }

    private AppUser testAppUser() {
        AppUser appUser = new AppUser();
        appUser.setAppUserId(1L);
        appUser.setFirstName("test");
        appUser.setLastName("test");
        appUser.setPassword("password");
        appUser.setMailAddress("mail@xx.xx.xx");
        return appUser;
    }
}