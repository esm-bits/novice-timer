# novice-timer

### 開発環境
- IDE

    Eclipse Neon.3 (4.6.3)

- フレームワーク

    SpringBoot (1.5.4)

### 導入方法（Eclipse）
1. gitでmasterブランチをclone
2. Eclipseで、ファイル→インポート→Gradle/Gradleプロジェクト、を選択してインポート
3. プロジェクトを右クリックし、Gradle→Gradleプロジェクトのリフレッシュ、を選択

- インポートにGradleプロジェクトがない場合、Eclipseに`BuildShip`をインストールする必要がある
    * Eclipseのメニューバーからヘルプ→Eclipseマーケットプレイス→`BuildShip`で検索しインストール

- Gradleプラグインとして`Gradle IDE Pack`を使用している場合
    * `BuildShip`をインストール
    * Gradle（STS）/Gradle（STS）Projectを選択してインポートすることも可能だが、`BuildShip`を使用するので上記の方法でインポートする
    * 参考
        * [Migration guide from STS Gradle to Buildship](https://github.com/eclipse/buildship/wiki/Migration-guide-from-STS-Gradle-to-Buildship)

- Lombokを使用しているため、EclipseへのLombokのインストールが必要である
    * Lombokのjarファイルを[ダウンロード](https://projectlombok.org/download)して実行
    * 対象のIDEを聞かれるのでEclipseを選択してインストール

### 実行方法（Eclipse）

- 方法1

    プロジェクトを右クリック→実行→実行の構成→`NoviceTimerApplication`を選択して実行

- 方法2

    `NoviceTimerApplication`を表示して、右クリック→実行→Java アプリケーション、より実行

### 実行時の通知先について
- `src/main/resources/config/application.yml`ファイルに通知を送るフックのurlを記述するが、GitHubで公開したくないのでurlの記述のないダミーファイルとしている

- 実際のフックurlを記述した`application-production.yml`を作成し、`.gitignore`ファイルに記述することでGitの管理から外している

- 通常どおりに実行すると設定ファイルとして`application.yml`が読み込まれる。

- `application-production.yml`を読み込むには、実行時に環境変数`SPRING_PROFILES_ACTIVE = production`を指定する（Eclipseの場合、実行→実行の構成→環境、に記述）

- 通知を送りたくない場合、`TimerService.NoticeTimerTask`の`sendMessageメソッド`の処理を`System.out.println`に変更するとよい

### 操作方法
- アジェンダの全取得
    * パス: GET api/agendas
    
- 特定アジェンダの取得
    * パス: GET api/agendas/{id}

- アジェンダの登録
    * パス: POST api/agendas
    * リクエストボディ:
    
        ```json
        {
            "subjects": [
                {
                    "title": "title1",
                    "minutes": 5,
                    "idobata_user": "user1"
                },
                {
                    "title": "title2",
                    "minutes": 5,
                    "idobata_user": "user2"
                }
            ]
        }
        ```

- アジェンダの更新
    * パス: PUT api/agendas/{id}
    * リクエストボディ:

        ```json
        {
            "subjects": [
                {
                    "title": "new_title1",
                    "minutes": 10,
                    "idobata_user": "new_user1"
                },
                {
                    "title": "new_title2",
                    "minutes": 10,
                    "idobata_user": "new_user2"
                }
            ]
        }
        ```

- サブジェクトの更新
    * パス: PUT api/agendas/{id}/subjects/{number}
    * リクエストボディ:
    
        ```json
        {
            "title": "new_title",
            "minutes": 10,
            "idobata_user": "new_user"
        }
        ```
        
- アジェンダの全削除
    * パス: DELETE api/agendas

- 特定アジェンダの削除
    * パス: DELETE api/agendas/{id}

- タイマーの開始
    * パス: GET api/agendas/{id}/timer/start

- タイマーの開始(サブジェクト指定)
    * パス: PUT api/agendas/{id}/subjects/{number}/timers
    * リクエストボディ:
    
        ```json
        {
            "state": "start"
        }
        ```

    * 備考: タイマーの実行は同時に1つのみ、実行中のタイマーがある場合は開始されない

- タイマーの終了
    * パス: GET api/agendas/{id}/timer/stop

- タイマーの終了(サブジェクト指定)
    * パス: PUT api/agendas/{id}/subjects/{number}/timers
    * リクエストボディ:

        ```json
        {
            "state": "end"
        }
        ```

- 備考
    * 現在Viewを作成していないためRESTクライアントツールを使用して操作する
    * 使用しているツール
        * curlコマンド
        * [Restlet Client - REST API Testing](https://chrome.google.com/webstore/detail/restlet-client-rest-api-t/aejoelaoggembcahagimdiliamlcdmfm)

### Herokuへのデプロイ方法

- Herokuを使用する準備
    * [Herokuアカウント](https://signup.heroku.com/dc)を作成する
    * Heroku CLIをインストールする(参考のDownload the Heroku CLI for...から）
    * `heroku login`コマンドを使用して、登録したメールアドレスとパスワードでログインする
    * `heroku keys`コマンドでキーが追加されたか確認する。ない場合は`heroku keys:add`コマンドで追加する

- Herokuへデプロイ
    * `./gradlew build`コマンド（windowsは`gradlew.bat build`）でビルドを実行し、生成された実行可能jar（build/libs/novice-timer-0.0.1-SNAPSHOT.jar）が問題なく実行できるか確認する
    * `heroku login`コマンドを使用してログイン
    * `heroku create`コマンドで新しいHerokuアプリをプロビジョニング
    * `heroku config:set BUILDPACK_URL=https://github.com/marcoVermeulen/heroku-buildpack-gradlew.git`コマンドを実行し、使用するBuildpackを変更
    * `git push heroku master`コマンドで作成したアプリをHerokuへプッシュ
    * `heroku open`コマンドでアプリをブラウザで実行できる

- 備考
    * Herokuではmasterが実行される
    * 初回はHerokuにmasterがないので`git push heroku master:master`でプッシュする
    * アプリの名前は`heroku create`時に適当に決められるが、`heroku apps:rename`コマンドで任意の名前に変更できる
    * Herokuでの環境変数は、`heroku config:set`コマンドで登録できる
    * Heroku標準のBuildpackでは、SpringBootアプリを`gradle wrapper`でうまくビルドできない。そのためカスタムのBuildpackを利用するために`heroku config:set BUILDPACK_URL=https://github.com/marcoVermeulen/heroku-buildpack-gradlew.git`コマンドを実行する

- 参考
    * [Deploying Spring Boot Applications to Heroku](https://devcenter.heroku.com/articles/deploying-spring-boot-apps-to-heroku)
    * [Deploying Gradle Apps on Heroku](https://devcenter.heroku.com/articles/deploying-gradle-apps-on-heroku)
    * [herokuでspring-bootをgradleを使ってデプロイするときのコツ](http://qiita.com/gosshys/items/fa02b390b60ee3001dae)

### 参考書

- 槙俊明（2016）『はじめてのSpringBoot［改訂版］』工学社.
