package jp.co.esm.novicetimer.service;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jp.co.esm.novicetimer.domain.Configs;
import jp.co.esm.novicetimer.domain.IdobataMessage;
import jp.co.esm.novicetimer.domain.TimeLimit;

@Service
public class TimerService {
    @Autowired
    private Configs config;

    private Timer timer;

    public String startTimer(TimeLimit timeLimit) {
        int minutes = timeLimit.getMinutes();

        sendMessage("start:" + minutes + "分");

        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                sendMessage("ピピピ" + minutes + "分経ちました");
                timer = null;
            }
        }, TimeUnit.SECONDS.toMillis(minutes)); // テストを容易にするため秒とする、実際にはTimeUnit.MIMUTESで計る

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

    private void sendMessage(String source) {
        new RestTemplate().postForObject(config.getHookUrl(), new IdobataMessage(source), String.class);
    }
}
