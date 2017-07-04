package jp.co.esm.novicetimer.service;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jp.co.esm.novicetimer.domain.Configs;
import jp.co.esm.novicetimer.domain.IdobataMessage;
import jp.co.esm.novicetimer.domain.Subject;
import jp.co.esm.novicetimer.domain.TimeLimit;

@Service
public class TimerService {
    @Autowired
    private Configs config;

    private Timer timer;

    public String startTimer(TimeLimit timerLimit) {
        int minutes = timerLimit.getMinutes();
        int seconds = (int) TimeUnit.MINUTES.toSeconds(timerLimit.getMinutes());

        String idobataUser = timerLimit.getIdobataUser();
        String title = null;
        if (timerLimit instanceof Subject) {
            title = ((Subject) timerLimit).getTitle();
        }

        if (title != null) {
            sendMessage(new IdobataMessage.Builder(
                    "タイトル：" + title + "\n発表者：" + idobataUser + "\n予定時間：" + minutes + "分").build());
        } else {
            sendMessage(new IdobataMessage.Builder("予定時間：" + minutes + "分").users(idobataUser).build());
        }

        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                sendMessage(new IdobataMessage.Builder("予定時間の半分が経過しました。").users(idobataUser).build());
            }
        }, TimeUnit.SECONDS.toMillis(seconds / 2)); // 終了時間半分の通知

        if (minutes > 2) { // 1分は開始時、2分は半分経過、の通知と重なるので除外する
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    sendMessage(new IdobataMessage.Builder("残り1分です。").users(idobataUser).build());
                }
            }, TimeUnit.SECONDS.toMillis(seconds - 60)); // 終了1分前の通知
        }

        AtomicInteger overMinutes = new AtomicInteger();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (overMinutes.get() == 0) {
                    sendMessage(new IdobataMessage.Builder("終了時間です。").users(idobataUser).build());
                } else {
                    sendMessage(new IdobataMessage.Builder(overMinutes.get() + "分超過しました。").users(idobataUser).build());
                }
                overMinutes.getAndIncrement();
            }
        }, TimeUnit.SECONDS.toMillis(seconds), TimeUnit.SECONDS.toMillis(60)); // 終了時間と1分超過ごとの通知

        return String.valueOf(minutes);
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
