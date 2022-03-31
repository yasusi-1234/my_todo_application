package com.example.my_todo_application.task_management.domain.service;

import com.example.my_todo_application.task_management.domain.model.AppUser;
import com.example.my_todo_application.task_management.domain.model.Role;
import com.example.my_todo_application.task_management.domain.repository.AppUserRepository;
import com.example.my_todo_application.task_management.domain.repository.RoleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("UserRegisterServiceImplクラスのテスト")
@SpringBootTest(classes = UserRegisterServiceImpl.class)
class UserRegisterServiceImplTest {

    @Autowired
    UserRegisterService userRegisterService;

    @MockBean
    AppUserRepository appUserRepository;

    @MockBean
    RoleRepository roleRepository;

    @MockBean
    PasswordEncoder passwordEncoder;

    @DisplayName("saveAppUserメソッドはuppUserRepositoryのメソッドを呼び出しその結果を返却する")
    @Test
    void saveAppUserTest() {
        // setup
        AppUser expected = testAppUser();
        // ロール情報
        Role role = new Role();
        role.setRoleId(1);
        role.setRoleName("ROLE_GENERAL");
        expected.setRole(role);
        // パスワード情報
        String encodePassword = "change password";
        expected.setPassword(encodePassword);

        doReturn(expected).when(appUserRepository).save(any(AppUser.class));
        doReturn(role).when(roleRepository).findByRoleName(anyString());
        doReturn(encodePassword).when(passwordEncoder).encode(anyString());

        AppUser actual = userRegisterService.saveAppUser(
                "test", "test", "mail@xx.xx.xx", "password"
        );

        assertAll(
                () -> assertEquals(expected.getFirstName(), actual.getFirstName()),
                () -> assertEquals(expected.getLastName(), actual.getLastName()),
                () -> assertEquals(expected.getMailAddress(), actual.getMailAddress()),
                () -> assertEquals(expected.getRole(), actual.getRole()),
                () -> assertEquals(encodePassword, actual.getPassword())
        );

        verify(passwordEncoder, times(1)).encode(anyString());
        verify(appUserRepository, times(1)).save(any(AppUser.class));
        verify(roleRepository, times(1)).findByRoleName(anyString());
    }

    private AppUser testAppUser() {
        AppUser appUser = new AppUser();
        appUser.setFirstName("test");
        appUser.setLastName("test");
        appUser.setPassword("password");
        appUser.setMailAddress("mail@xx.xx.xx");
        return appUser;
    }
}