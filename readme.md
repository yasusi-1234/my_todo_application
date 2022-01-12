# マイTODOアプリケーションとは？
タスク管理アプリケーション

# アプリケーションURL
未実装

## 使用技術
### フレームワーク・言語
* Spring Boot
  * JPA ・Thymeleaf ・AOP ・ SpringSecurity
* Java
* HTML5
* CSS(Sass)
* JavaScript

### データベース
* PostgreSQL

### インフラ
* AWS(予定)

## 利用方法
※ 未実装

## 作成した理由・背景
主に勉強の過程として
<br><br>
今までは開発前に、ノートに機能要件・データベース設計・要件ごとに機能の洗い出しと言った事を何となく
行ったうえで開発に落とし込んでいた。

しかし、より実践的に開発してみたいという思いと、開発前にしっかりとした基本設計・詳細設計や(主に詳細設計)、
図の作成(ユースケース図・シーケンス図・クラス図・ER図)等をしたうえで
落とし込んでいくことは重要な事だという思いが強くなり、実践してみようと思ったことがきっかけです。

## アプリケーション機能(設計)

機能名 機能概要 機能分類 実装方法

### 要件等の洗い出し
<table>
  <tr>
    <th>機能名</th>
    <th>機能概要</th>
    <th>機能分類</th>
  </tr>
  <tr>
    <td>アカウント登録</td>
    <td>ユーザーが新規アカウントを登録する</td>
    <td>画面</td>
  </tr>
  <tr>
    <td>ログイン機能</td>
    <td>ユーザーがWebページにログインする</td>
    <td>画面</td>
  </tr>
  <tr>
    <td>ログアウト機能</td>
    <td>ユーザーがWebページからログアウトする</td>
    <td>画面</td>
  </tr>
  <tr>
    <td>タスク登録機能</td>
    <td>ユーザーが新たなタスクを登録する</td>
    <td>画面</td>
  </tr>
  <tr>
    <td>タスク更新機能</td>
    <td>ユーザーが登録されているタスクの更新を行う</td>
    <td>画面</td>
  </tr>
  <tr>
    <td>タスク削除機能</td>
    <td>ユーザーが登録されているタスクの削除を行う</td>
    <td>画面</td>
  </tr>
  <tr>
    <td>タスク閲覧機能</td>
    <td>ユーザーが登録されているタスクの閲覧を行う</td>
    <td>画面</td>
  </tr>

  <tr>
    <td>アカウント登録</td>
    <td>入力された内容を元にデータベースに登録を行う</td>
    <td>処理</td>
  </tr>
  <tr>
    <td>ログイン機能</td>
    <td>入力されたデータを元にログイン可能か判定を行う</td>
    <td>処理</td>
  </tr>
  <tr>
    <td>ログアウト機能</td>
    <td>ユーザーのログアウト処理を行う</td>
    <td>処理</td>
  </tr>
  <tr>
    <td>タスク登録機能</td>
    <td>入力された内容を元に新たなタスクを登録する</td>
    <td>処理</td>
  </tr>
  <tr>
    <td>タスク更新機能</td>
    <td>入力された内容を元にタスクの更新を行う</td>
    <td>処理</td>
  </tr>
  <tr>
    <td>タスク削除機能</td>
    <td>入力値を元にタスクの削除を行う</td>
    <td>処理</td>
  </tr>
  <tr>
    <td>タスク閲覧機能</td>
    <td>入力された内容を元にタスクデータを表示する</td>
    <td>処理</td>
  </tr>
  <tr>
    <td>タスク出力機能</td>
    <td>ユーザーが選択したタスク情報を元にExcel・CSV・PDF形式等での出力を行う</td>
    <td>処理</td>
  </tr>
  <tr>
    <td>タスク注意勧告機能</td>
    <td>ユーザーが登録したタスク期間中の完了してないタスクの注意勧告を行う</td>
    <td>処理</td>
  </tr>
  
</table>

### 各種画面

<table>
  <tr>
    <th>画面名</th>
    <th>詳細</th>
    <th>ファイル名</th>
  </tr>
  <tr>
    <td>ログイン画面</td>
    <td>ログインの画面</td>
    <td>login.html</td>
  </tr>
  <tr>
    <td>ユーザー登録画面</td>
    <td>ユーザーが新規登録する際の画面</td>
    <td>user-register.html</td>
  </tr>
  <tr>
    <td>タスクホーム画面</td>
    <td>
      アプリケーションのホーム画面
      <br>
      タスクの詳細画面
      <br>
      タスク検索画面
    </td>
    <td>task-home.html</td>
  </tr>
  <tr>
    <td>タスク登録・更新画面</td>
    <td>タスクを新規に登録及び更新する画面</td>
    <td>task-register.html</td>
  </tr>
</table>

### データベースのテーブル一覧

<table>
  <tr>
    <th>テーブル名</th>
    <th>概要</th>
    <th>関連</th>
  </tr>
  <tr>
    <td>app_user</td>
    <td>ユーザー情報</td>
    <td>role, app_user_task</td>
  </tr>
  <tr>
    <td>role</td>
    <td>ユーザーのロール情報</td>
    <td>app_user</td>
  </tr>
  
  <tr>
    <td>task</td>
    <td>ユーザーのタスク情報</td>
    <td>app_user_task</td>
  </tr>
</table>

## UML・ER

### ER図
![ER図](plantuml/db_er.png)

### ユースケース図
![ユースケース図](plantuml/uc.png)

### シーケンス図

#### ユーザー登録の流れ
![ユーザー登録シーケンス図](plantuml/signup_se.png)

### ログイン処理の流れ
![ユーザーログインシーケンス図](plantuml/login_se.png)

### タスク登録処理の流れ
![タスク登録シーケンス図](plantuml/task_register_se.png)

### タスク更新処理の流れ
![タスク更新シーケンス図](plantuml/task_update_se.png)

### タスク削除処理の流れ
![タスク削除シーケンス図](plantuml/task_delete_se.png)

### タスク参照処理の流れ
![タスク参照シーケンス図](plantuml/task_select_se.png)

## クラス図

### ログイン・ユーザー登録関連のクラス図
![ログイン・ユーザー登録関連のクラス図](plantuml/login_user_register_class.png)

### タスク管理系のクラス図
![タスク処理関連のクラス図](plantuml/task_relation_class.png)

## 画面遷移
![Webの画面遷移図](plantuml/web_state.png)

## その他