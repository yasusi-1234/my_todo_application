package com.example.my_todo_application.task_management.domain.repository;

import com.example.my_todo_application.task_management.domain.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * 引数で指定されたロール名を持つ {@link Role} オブジェクトを返却
     * @param roleName ロール名
     * @return 引数で指定されたロール名を持つ {@link Role} オブジェクト
     */
    Role findByRoleName(String roleName);
}
