package jp.co.esm.novicetimer.service;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jp.co.esm.novicetimer.domain.Configs;
import jp.co.esm.novicetimer.domain.IdobataMessage;
import jp.co.esm.novicetimer.domain.Subject;

/**
 * タイマーを操作するクラス。<p>
 * タイマーの開始、停止が行える。
 */
@Service
public class TimerService {
    private static final int PERIOD_SECONDS = 30;

    @Autowired
    private Configs config;

    private Timer timer;

    /**
     * タイマーを開始するメソッド。
     * <p>
     * 与えられたSubject情報に沿った設定のタイマーを開始する。
     * 既にタイマーが開始されていた場合は何もせずreturnする。
     * @param subject startしたいsubjectを受け取る
     * @return 設定時間を表した文字列<br>
     * 既にタイマーが動いていた場合は "0" の文字列
     */
    public String startTimer(Subject subject) {
        if (timer != null) {
            return String.valueOf(0);
        }

        timer = new Timer();

        timer.schedule(new NoticeTimerTask(subject, config.getHookUrl()), 0, TimeUnit.SECONDS.toMillis(PERIOD_SECONDS));

        return String.valueOf(subject.getMinutes());
    }

    /**
     * タイマーを止めるメソッド。
     * <p>
     * タイマーを止める。タイマーは1つしか存在できないので複数回呼び出す必要はない。
     * @return タイマーを止める時に<br>
     * タイマーが動いていた場合はtrue<br>
     * タイマーが動いていなかった場合はfalse<br>
     */
    public boolean stopTimer() {
        if (timer == null) {
            return false;
        }
        timer.cancel();
        timer = null;
        return true;
    }

    /**
     * タイマーをThreadとして生成し、指定条件でidobataへメッセージを送信する処理を行うクラス<br>
     * TimerServiceの内部クラス
     */
    public static class NoticeTimerTask extends TimerTask {
        private int count;
        private Messenger messenger;
        private Factory factory = new MessengerFactory();

        private String idobataUser;
        private String title;
        private int endMinutes;
        private int endSeconds;

        public NoticeTimerTask(Subject subject, String hookUrl) {
            this.count = 0;
            messenger = factory.createMessenger(hookUrl);

            this.idobataUser = subject.getIdobataUser();
            this.title = subject.getTitle();
            this.endMinutes = subject.getMinutes();
            this.endSeconds = (int) TimeUnit.MINUTES.toSeconds(endMinutes);
        }

        @Override
        public void run() {
            int elapsedSeconds = PERIOD_SECONDS * count;

            if (elapsedSeconds == 0) {
                // 開始時の通知
                messenger.sendMessage(new IdobataMessage.Builder(
                    "タイトル：" + title + "\n発表者：" + idobataUser + "\n予定時間：" + endMinutes + "分").build());
            } else if (elapsedSeconds == endSeconds / 2) {
                // 終了時間半分の通知
                messenger.sendMessage(new IdobataMessage.Builder("予定時間の半分が経過しました。").users(idobataUser).build());
            } else if (elapsedSeconds == endSeconds - 60) {
                // 終了1分前の通知
                if (endMinutes > 2) { // 1分は開始時、2分は半分経過、の通知と重なるので除外する
                    messenger.sendMessage(new IdobataMessage.Builder("残り1分です。").users(idobataUser).build());
                }
            } else if (elapsedSeconds == endSeconds) {
                // 終了時間の通知
                messenger.sendMessage(new IdobataMessage.Builder("終了時間です。").users(idobataUser).build());
            } else if (elapsedSeconds > endSeconds && count % 2 == 0) {
                // 1分超過ごとの通知
                messenger.sendMessage(new IdobataMessage.Builder(((elapsedSeconds - endSeconds) / 60) + "分超過しました。")
                    .users(idobataUser).build());
            }
            count++;
        }
    }
}

/**
 * FactoryMethodパターンのProductの抽象クラス。
 */
abstract class Messenger {
    public abstract void sendMessage(IdobataMessage message);
}

/**
 * FactoryMethodパターンのFactoryの抽象クラス。生成処理を切り替える場合のために一応用意。
 * <p>
 */
abstract class Factory {
    /**
     * 出力先を決め、必要な処理を行うメソッド。
     * <p>
     * 通知先を決めたときに処理が必要ならここに記述すること。
     * @param url 出力先URL
     * @return 出力用インスタンス
     */
    public final Messenger create(String url) {
        return createMessenger(url);
    }

    /**
     * 引数として受け取った文字列によって出力先を決める抽象メソッド。
     * <p>
     * @param url 出力先URL
     * @return 出力用インスタンス
     */
    protected abstract Messenger createMessenger(String url);
}

/**
 * FactoryMethodパターンのFactoryの具象クラス。
 * <p>
 * このFactoryクラスでは、
 * 標準出力に出力するインスタンス、
 * idobataに出力するインスタンスが生成できる。
 */
class MessengerFactory extends Factory {

    /**
     * URLによって出力先の違うインスタンスを生成するメソッド。
     * URLが記述されていたらidobataに出力する。
     * 文字列の長さが0だった場合は標準出力に出力する。
     * nullだった場合は例外になる。
     * @param url 出力先URL
     * return 出力用インスタンス
     */
    @Override
    protected Messenger createMessenger(String url) {
        if (url.isEmpty()) {
            return new StandardOutMessenger();
        }
        return new IdobataMessenger(url);
    }
}

/**
 * FactoryMethodパターンのProductの具象クラス。出力先はidobata。
 * <p>
 * idobataに出力するクラス。
 */
class IdobataMessenger extends Messenger {
    String hookUrl;

    /**
     * コンストラクタ。
     * 出力先に対する設定を行う。
     * @param url 出力先のURL
     */
    IdobataMessenger(String url) {
        hookUrl = url;
    }

    /**
     * idobataに出力するメソッド。
     * @param message 出力するデータ
     */
    @Override
    public void sendMessage(IdobataMessage message) {
        new RestTemplate().postForObject(
            hookUrl,
            message,
            String.class);
    }
}

/**
 * FactoryMethodパターンのProductの具象クラス。出力先は標準出力。
 * <p>
 * 標準出力に出力するクラス。
 */
class StandardOutMessenger extends Messenger {
    /**
     * 空のコンストラクタ。
     * 一応明示化した。
     */
    StandardOutMessenger() {
    }

    /**
     * 標準出力に出力するメソッド。
     * @param message 出力するデータ
     */
    public void sendMessage(IdobataMessage message) {
        System.out.println(message.getSource());
    }
}
