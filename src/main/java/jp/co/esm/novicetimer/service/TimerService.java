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

@Service
public class TimerService {
    private static final int PERIOD_SECONDS = 30;

    @Autowired
    private Configs config;

    private Timer timer;

    public String startTimer(Subject subject) {
        String idobataUser = subject.getIdobataUser();
        String title = subject.getTitle();
        int endMinutes = subject.getMinutes();

        sendMessage(new IdobataMessage.Builder(
                "タイトル：" + title + "\n発表者：" + idobataUser + "\n予定時間：" + endMinutes + "分").build());

        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();

        int endSeconds = (int) TimeUnit.MINUTES.toSeconds(endMinutes);

        timer.schedule(new TimerTask() {
            private int count = 0;
            @Override
            public void run() {
                count++;
                int elapsedSeconds = PERIOD_SECONDS * count;

                if (elapsedSeconds == endSeconds / 2) {
                    // 終了時間半分の通知
                    sendMessage(new IdobataMessage.Builder("予定時間の半分が経過しました。").users(idobataUser).build());
                } else if (elapsedSeconds == endSeconds - 60) {
                    // 終了1分前の通知
                    if (endMinutes > 2) { // 1分は開始時、2分は半分経過、の通知と重なるので除外する
                        sendMessage(new IdobataMessage.Builder("残り1分です。").users(idobataUser).build());
                    }
                } else if (elapsedSeconds == endSeconds) {
                    // 終了時間の通知
                    sendMessage(new IdobataMessage.Builder("終了時間です。").users(idobataUser).build());
                } else if (elapsedSeconds > endSeconds && count % 2 == 0) {
                    // 1分超過ごとの通知
                    sendMessage(new IdobataMessage.Builder(((elapsedSeconds - endSeconds) / 60) + "分超過しました。").users(idobataUser).build());
                }
            }
        }, TimeUnit.SECONDS.toMillis(PERIOD_SECONDS), TimeUnit.SECONDS.toMillis(PERIOD_SECONDS));

        return String.valueOf(endMinutes);
    }

    public boolean stopTimer() {
        if (timer == null) {
            return false;
        }
        timer.cancel();
        timer = null;
        return true;
    }

    private void sendMessage(IdobataMessage message) {
        new RestTemplate().postForObject(
                config.getHookUrl(),
                message,
                String.class);
    }
}
