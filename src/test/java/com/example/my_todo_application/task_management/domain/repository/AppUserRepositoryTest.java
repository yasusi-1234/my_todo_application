package com.example.my_todo_application.task_management.domain.repository;

import com.example.my_todo_application.task_management.domain.model.AppUser;
import com.example.my_todo_application.task_management.domain.model.Role;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@DataJpaTest
@DisplayName("AppUserRepositoryクラスのテスト")
class AppUserRepositoryTest {

    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("countは5を返却する")
    void countTest() throws Exception{
        long actual = appUserRepository.count();
        long expected = 5;
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("findById(1)で期待通りのAppUserを返却する")
    void findByIdTest() throws Exception{
        AppUser actual = appUserRepository.findById(1L).orElse(null);
        AppUser expected = createTestAppUser();
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("saveが正しく行われる(新規挿入)")
    void saveAppUserTest() throws Exception {
        AppUser expectedUser = createTestAppUser();
        expectedUser.setAppUserId(null);
        AppUser actualUser = appUserRepository.save(expectedUser);

        assertEquals(expectedUser, actualUser);

        entityManager.flush();

        long actualCount = appUserRepository.count();
        long expectedCount = 6L;
        assertEquals(expectedCount, actualCount);
    }

    @Test
    @DisplayName("saveで正しく行われる(更新処理)")
    void updateTest() throws Exception {
        AppUser appUser = appUserRepository.findById(1L).orElse(null);
        appUser.setFirstName("aaa");
        appUser.setLastName("bbb");
        appUser.setMailAddress("xxx@xxx.xx.xx");
        appUser.setPassword("pass");

        appUserRepository.save(appUser);
        entityManager.flush();

        long expectedCount = 5L;
        long actualCount = appUserRepository.count();

        assertEquals(expectedCount, actualCount);

        AppUser actual = appUserRepository.findById(1L).orElse(null);

        assertAll(
                () -> assertEquals("aaa", actual.getFirstName()),
                () -> assertEquals("bbb", actual.getLastName()),
                () -> assertEquals("xxx@xxx.xx.xx", actual.getMailAddress()),
                () -> assertEquals("pass", actual.getPassword())
        );
    }

    @DisplayName("findByMailAddressメソッドで期待したユーザーが取得できる")
    @ParameterizedTest
    @CsvSource({"6TM8ytI8xvJU@xxx.xx.xx",
    "L3g9Pmpu4MUyY@xxx.xx.xx",
    "MV2i0Y9acmk6cnS@xxx.xx.xx"})
    void findByMailAddressTest(String mailAddress) throws Exception {
        AppUser actual = appUserRepository.findByMailAddress(mailAddress);

        assertEquals(mailAddress, actual.getMailAddress());
    }

    @DisplayName("findByMailAddressAndPasswordメソッドは正しく1件の情報を取得できる")
    @ParameterizedTest
    @CsvSource({"6TM8ytI8xvJU@xxx.xx.xx, password",
    "L3g9Pmpu4MUyY@xxx.xx.xx, password",
    "null, password"})
    void findByMailAddressAndPasswordTest(String mail, String pass){
        AppUser actual = appUserRepository.findByMailAddressAndPassword(mail, pass);

        Assumptions.assumingThat(mail.equals("null"),
                () -> assertNull(actual));

        Assumptions.assumingThat(!mail.equals("null"),
                () -> assertAll(
                        () -> assertNotNull(actual),
                        () -> assertEquals(mail, actual.getMailAddress())
                ));

    }

    private AppUser createTestAppUser(){
        Role role = new Role();
        role.setRoleId(1L);
        role.setRoleName("ROLE_GENERAL");
        AppUser appUser = new AppUser();
        appUser.setFirstName("元気");
        appUser.setLastName("青山");
        appUser.setMailAddress("6TM8ytI8xvJU@xxx.xx.xx");
        appUser.setAppUserId(1L);
        appUser.setPassword("password");
        appUser.setRole(role);
        return appUser;
    }
}