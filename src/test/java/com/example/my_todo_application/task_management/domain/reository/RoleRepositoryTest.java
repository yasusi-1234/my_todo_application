package com.example.my_todo_application.task_management.domain.reository;

import com.example.my_todo_application.task_management.domain.model.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
@DisplayName("RoleRepositoryクラスのテストクラス")
public class RoleRepositoryTest {


    @Autowired
    RoleRepository roleRepository;

    @Test
    @DisplayName("findByRoleNameメソッドで期待したロールを取得できる")
    void findByRoleNameTest() {
        String roleName = "ROLE_GENERAL";
        Role actual = roleRepository.findByRoleName(roleName);

        assertEquals(roleName, actual.getRoleName());
    }

    @Test
    @DisplayName("findByRoleNameメソッドでロール名が無い場合はNullを返却する")
    void findByRoleNameByEmptyTest() {
        String roleName = "ROLE_Null";
        Role actual = roleRepository.findByRoleName(roleName);

        assertNull(actual);
    }


}
