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

    public String startTimer(TimeLimit timerLimit) {
        int seconds = timerLimit.getSeconds();

        String idobataUser = timerLimit.getIdobata_user();

        sendMessage(idobataUser, " start:" + seconds + "秒");

        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                sendMessage(idobataUser, " ピピピ" + seconds + "秒経ちました");
                timer = null;
            }
        }, TimeUnit.SECONDS.toMillis(seconds));

        return String.valueOf(seconds);
    }

    public boolean stopTimer() {
        if (timer == null) {
            return false;
        }
        timer.cancel();
        timer = null;
        return true;
    }

    private void sendMessage(String user, String source) {
        if (user != null && user != "") {
            new RestTemplate().postForObject(config.getHookUrl(), new IdobataMessage("@" + user + " " + source),
                    String.class);
        } else {
            new RestTemplate().postForObject(config.getHookUrl(), new IdobataMessage(source), String.class);
        }
    }
}
