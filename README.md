# novice-timer

### 開発環境
- IDE

    Eclipse Neon.3 (4.6.3)

- フレームワーク

    SpringBoot (1.5.4)

### 導入方法（Eclipse）
1. gitでmasterブランチをclone
2. Eclipseで、ファイル→インポート→Gradleプロジェクト、を選択してインポート
3. プロジェクトを右クリックし、Gradle→Gradleプロジェクトのリフレッシュ、を選択


- インポートにGradleプロジェクトがない場合、Eclipseに`Gradle IDE Pack`をインストールする必要がある
    * Eclipseのメニューバーからヘルプ→Eclipseマーケットプレイス→`Gradle`で検索→`Gradle IDE Pack`をインストール


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
- 現在Viewを作成していないためRESTクライアントツールを使用して操作する

- 使用しているツール
    * curlコマンド
    * [Restlet Client - REST API Testing](https://chrome.google.com/webstore/detail/restlet-client-rest-api-t/aejoelaoggembcahagimdiliamlcdmfm)

### Herokuへのデプロイ方法

- Herokuを使用する準備
    * [Herokuアカウント](https://signup.heroku.com/dc)を作成する
    * Heroku CLIをインストールする(参考のDownload the Heroku CLI for...から）
    * `heroku login`コマンドを使用して、登録したメールアドレスとパスワードでログインする
    * `heroku keys`コマンドでキーが追加されたか確認する。ない場合は`heroku keys:add`コマンドで追加する

- Herokuへデプロイ
    * `heroku login`コマンドを使用してログイン
    * `heroku create`コマンドで新しいHerokuアプリをプロビジョニング
    * `git push heroku master`コマンドで作成したアプリをHerokuへプッシュ
    * `heroku open`コマンドでアプリをブラウザで実行できる

- 備考
    * Herokuではmasterが実行される
    * 初回はHerokuにmasterがないので`git push heroku master:master`でプッシュする
    * アプリの名前は`heroku create`時に適当に決められるが、`heroku apps:rename`コマンドで任意の名前に変更できる
    * Herokuでの環境変数は、`heroku config:set`コマンドで登録できる

- 参考
    * [Deploying Spring Boot Applications to Heroku](https://devcenter.heroku.com/articles/deploying-spring-boot-apps-to-heroku)

### HerokuにGradleAppsをデプロイする準備

1. `build.gradle`に以下の記述を追加する

    `jar { baseName = 'hoge' version =  '0.1.0' }`
    
    `defaultTasks "clean", "build"`

    * `jar`を記述するとビルド時に、実行可能jarである`build/libs/hoge-0.1.0.jar`が生成される（ファイル名は`baseName`と`version`より決められる）
    * Herokuへのデプロイ時にtaskの指定がない`./gradlew`が実行される。そのため`defaultTasks`を記述してディフォルトタスクを指定する

2. `gradle/wrapper/gradle-wrapper.jar`と`gradle/wrapper/gradle-wrapper.properties`ファイルが必要である。ない場合`gradle wrapper`コマンドで生成する

3. `./gradlew build`コマンド（windowsは`gradlew.bat build`）でビルドを実行し、生成された実行可能jarが問題なく実行できるか確認する

4. Heroku標準のbuildpackでは、SpringBootアプリを`gradle wrapper`でうまくビルドできない。そのためカスタムのbuildpackを利用する。`heroku config:set BUILDPACK_URL=https://github.com/marcoVermeulen/heroku-buildpack-gradlew.git`コマンドを実行すればよい

5. Herokuでアプリを起動する際に参照されるファイル`Procfile`を作成する。ビルド時に作成される実行可能jarを起動するよう記述する。ルートに`Procfile`を作成し、`web: java -jar build/libs/hoge-0.1.0.jar --server.port=$PORT`を記述する


- 参考
    * [Deploying Gradle Apps on Heroku](https://devcenter.heroku.com/articles/deploying-gradle-apps-on-heroku)
    * [herokuでspring-bootをgradleを使ってデプロイするときのコツ](http://qiita.com/gosshys/items/fa02b390b60ee3001dae)

### 参考書

- 槙俊明（2016）『はじめてのSpringBoot［改訂版］』工学社.
