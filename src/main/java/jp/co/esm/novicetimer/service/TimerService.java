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
    @Autowired
    private IdobataMessage idobataMessage;

    public String startTimer(TimeLimit timerLimit) {
        int seconds = timerLimit.getSeconds();

        sendMessage("start:" + seconds + "秒");

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                sendMessage("ピピピ" + seconds + "秒経ちました");
            }
        }, TimeUnit.SECONDS.toMillis(seconds));

        return String.valueOf(seconds);
    }

    private void sendMessage(String source) {
        message.setSource(source);
        new RestTemplate().postForObject(config.getHookUrl(), idobataMessage, String.class);
    }
}
