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
    @Autowired
    private Configs config;

    private Timer timer;

    public String startTimer(Subject subject) {
        int minutes = subject.getMinutes();

        String idobataUser = subject.getIdobataUser();
        String title = subject.getTitle();

        sendMessage(new IdobataMessage.Builder(
                "タイトル：" + title + "\n発表者：" + idobataUser + "\n予定時間：" + minutes + "分").build());

        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();

        int seconds = (int) TimeUnit.MINUTES.toSeconds(minutes);

        // 終了時間半分の通知
        startTimer(newTimerTask("予定時間の半分が経過しました。", idobataUser), TimeUnit.SECONDS.toMillis(seconds / 2));

        // 終了1分前の通知（1分は開始時、2分は半分経過、の通知と重なるので除外する）
        if (minutes > 2) {
            startTimer(newTimerTask("残り1分です。", idobataUser), TimeUnit.SECONDS.toMillis(seconds - 60));
        }

        // 終了時間と1分超過ごとの通知
        startTimer(new TimerTask() {
            private int overMinutes = 0;
            @Override
            public void run() {
                if (overMinutes == 0) {
                    sendMessage(new IdobataMessage.Builder("終了時間です。").users(idobataUser).build());
                } else {
                    sendMessage(new IdobataMessage.Builder(overMinutes + "分超過しました。").users(idobataUser).build());
                }
                overMinutes++;
            }
        }, TimeUnit.SECONDS.toMillis(seconds), TimeUnit.SECONDS.toMillis(60));

        return String.valueOf(minutes);
    }

    private TimerTask newTimerTask(String message, String idobataUser) {
        return new TimerTask()  {
            @Override
            public void run() {
                sendMessage(new IdobataMessage.Builder(message).users(idobataUser).build());
            }
        };
    }

    private void startTimer(TimerTask task, long startSeconds) {
        if (timer != null) {
            timer.schedule(task, startSeconds);
        }
    }

    private void startTimer(TimerTask task, long startSeconds, long periodSeconds) {
        if (timer != null) {
            timer.schedule(task, startSeconds, periodSeconds);
        }
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
