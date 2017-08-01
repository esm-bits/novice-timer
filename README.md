# novice-timer


### 開発環境
- IDE
Eclipse Neon.3 (4.6.3)

- フレームワーク
SpringBoot (1.5.4)

### 導入方法(Eclipse)
1.gitでmasterブランチをcloneする
2.Eclipseで ファイル→インポート→Gradleプロジェクト を選択してインポートを行う。
3.プロジェクトを右クリックし、Gradle→Gradleプロジェクトのリフレッシュ、を選択する。

### 実行方法
- `src/main/resources/config/application.yml`ファイルに通知を送るフックのurlを記述するが、
GitHubで公開したくないのでurlの記述のないダミーファイルを代替している

- `application-production.yml`を作成し実際のフックurlを記述し、`.gitignore`ファイルに記述することでGitの管理から外している

- 通常どおりに実行すると設定ファイルとして`application.yml`が読み込まれる。
- `application-production.yml`を読み込むは実行時に環境変数`SPRING_PROFILES_ACTIVE = production`を指定する。
※eclipseの場合、実行→実行の構成→環境、に記述

- 通知を送りたくない場合、TimerService.NoticeTimerTaskのsendMessageメソッドの処理をSystem.out.printlnに変更するとよい

### 操作方法
現在Viewを作成していないためRESTクライアントツールを使用して操作する
- 使用しているツール
curlコマンド
[Restlet Client - REST API Testing](https://chrome.google.com/webstore/detail/restlet-client-rest-api-t/aejoelaoggembcahagimdiliamlcdmfm)

### デプロイ先
- Heroku

### 参考書
槙俊明（2016）『はじめてのSpringBoot［改訂版］』工学社.
