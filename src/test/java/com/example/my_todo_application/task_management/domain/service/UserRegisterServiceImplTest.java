package com.example.my_todo_application.task_management.domain.service;

import com.example.my_todo_application.task_management.domain.model.AppUser;
import com.example.my_todo_application.task_management.domain.model.Role;
import com.example.my_todo_application.task_management.domain.reository.AppUserRepository;
import com.example.my_todo_application.task_management.domain.reository.RoleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

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

    @DisplayName("saveAppUserメソッドはuppUserRepositoryのメソッドを呼び出しその結果を返却する")
    @Test
    void saveAppUserTest() {
        // setup
        AppUser expected = testAppUser();
        Role role = new Role();
        role.setRoleId(1L);
        role.setRoleName("ROLE_GENERAL");
        expected.setRole(role);

        doReturn(expected).when(appUserRepository).save(any(AppUser.class));
        doReturn(role).when(roleRepository).findByRoleName(anyString());

        AppUser actual = userRegisterService.saveAppUser(
                "test", "test", "mail@xx.xx.xx", "password"
        );

        assertEquals(expected, actual);
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