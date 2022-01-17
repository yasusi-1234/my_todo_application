package com.example.my_todo_application.task_management.domain.repository;

import com.example.my_todo_application.task_management.domain.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    /**
     * 指定のメールアドレス・パスワードの情報を持つ {@link AppUser}を返却する
     * @param mailAddress メールアドレス
     * @param password パスワード
     * @return 引数で入力されたメールアドレス・パスワードを持つ {@link AppUser}
     */
    AppUser findByMailAddressAndPassword(String mailAddress, String password);

    /**
     * 指定のメールアドレスの情報を持つ {@link AppUser}を返却する
     * @param mailAddress メールアドレス
     * @return 引数で入力されたメールアドレスを持つ{@link AppUser}
     */
    AppUser findByMailAddress(String mailAddress);
}
