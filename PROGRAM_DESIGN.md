# プログラム設計書

## 1. 本書の位置付け
本書は、アウトプット管理システムに関するプログラムの詳細設計を記述する。

## 2. 機能仕様
機能仕様については以下のドキュメントを参照：
- アウトプット管理システム_画面設計書
- アウトプット管理システム_データ設計書

## 3. ファイル構成
```
src/
└── main/
    ├── java/
    │   └── com/
    │       └── example/
    │           └── outputmanager/
    │               ├── controller/
    │               │   ├── AuthController.java
    │               │   ├── UserController.java
    │               │   ├── OutputController.java
    │               │   ├── FavoriteController.java
    │               │   └── CategoryController.java
    │               │
    │               ├── domain/
    │               │   ├── User.java
    │               │   ├── Output.java
    │               │   ├── Favorite.java
    │               │   └── Category.java
    │               │
    │               ├── mapper/
    │               │   ├── UserMapper.java
    │               │   ├── OutputMapper.java
    │               │   ├── FavoriteMapper.java
    │               │   └── CategoryMapper.java
    │               │
    │               ├── service/
    │               │   ├── UserService.java
    │               │   ├── UserServiceImpl.java
    │               │   ├── OutputService.java
    │               │   ├── OutputServiceImpl.java
    │               │   ├── FavoriteService.java
    │               │   ├── FavoriteServiceImpl.java
    │               │   ├── CategoryService.java
    │               │   └── CategoryServiceImpl.java
    │               │
    │               ├── form/
    │               │   ├── LoginForm.java
    │               │   ├── UserForm.java
    │               │   └── OutputForm.java
    │               │
    │               ├── filter/
    │               │   ├── AuthFilter.java
    │               │   └── CharsetFilter.java
    │               │
    │               └── util/
    │                   └── DbConnectionTest.java
    │
    └── resources/
        ├── mybatis/
        │   ├── UserMapper.xml
        │   ├── OutputMapper.xml
        │   ├── FavoriteMapper.xml
        │   └── CategoryMapper.xml
        │
        ├── templates/
        │   ├── home.html
        │   ├── users/
        │   │   └── register.html
        │   ├── outputs/
        │   │   ├── list.html
        │   │   ├── save.html
        │   │   └── detail.html
        │   ├── favorites/
        │   │   └── list.html
        │   └── categories/
        │       └── list.html
        │
        ├── static/
        │   ├── css/
        │   └── js/
        │
        └── validation.properties
```

## 4. クラス構成

### 4.1 クラス図
※ 本番はUMLで作成予定

- Output（多） -- (1) User
- Favorite（多） -- (1) User
- Favorite（多） -- (1) Output
- Output（多） -- (1) Category

### 4.2 クラス詳細

#### User クラス
パッケージ：domain

| フィールド | 説明 |
|-----------|------|
| private Integer id | ユーザーID |
| private String name | 氏名 |
| private String email | メールアドレス |
| private String password | パスワード |
| private String icon | アイコン画像パス |
| private Date createdAt | 登録日時 |

#### Output クラス
パッケージ：domain

| フィールド | 説明 |
|-----------|------|
| private Integer id | アウトプットID |
| private String title | タイトル |
| private String summary | 概要 |
| private String detail | 詳細 |
| private String icon | アイコン画像パス |
| private String videoUrl | 動画URL |
| private Date createdAt | 登録日時 |
| private Date updatedAt | 更新日時 |
| private Integer categoryId | カテゴリID |
| private Integer userId | 登録者ユーザーID |

#### Category クラス
パッケージ：domain

| フィールド | 説明 |
|-----------|------|
| private Integer id | カテゴリID |
| private String name | カテゴリ名 |

#### Favorite クラス
パッケージ：domain

| フィールド | 説明 |
|-----------|------|
| private Integer id | お気に入りID |
| private Date createdAt | 登録日時 |
| private Integer outputId | アウトプットID |
| private Integer userId | ユーザーID |

## 5. 改訂履歴

| 版 | 日付 | 改訂者 | 内容 |
|----|------|--------|------|
| 1.0 | 2025/08/03 | 川上 友輔 | 初版作成 |
| 1.1 | 2025/08/08 | 川上 友輔 | パッケージ名修正・構成統一 |
