package com.example.my_todo_application.task_management.domain.service;

import com.example.my_todo_application.task_management.domain.model.AppUser;
import com.example.my_todo_application.task_management.domain.reository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserRegisterServiceImpl implements UserRegisterService{

    private final AppUserRepository appUserRepository;

    /**
     * 新規にユーザーをデータベースに登録するメソッド
     * @param firstName ユーザーの名前
     * @param lastName ユーザーの苗字
     * @param mailAddress メールアドレス
     * @param password パスワード
     * @return データベースに新規登録された {@link AppUser}の情報
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public AppUser saveAppUser(String firstName, String lastName, String mailAddress, String password){
        AppUser appUser = new AppUser();
        appUser.setFirstName(firstName);
        appUser.setLastName(lastName);
        appUser.setMailAddress(mailAddress);
        // 後にパスワードエンコーダ―で変更する
        appUser.setPassword(password);

        return appUserRepository.save(appUser);
    }
}