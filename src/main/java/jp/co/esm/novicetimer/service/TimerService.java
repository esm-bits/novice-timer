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

        String idobataUser = timerLimit.getIdobataUser();

        sendMessage(" start:" + seconds + "秒");

        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                sendMessage(" ピピピ" + seconds + "秒経ちました", idobataUser);
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

    private void sendMessage(String message, String... users) {
        new RestTemplate().postForObject(config.getHookUrl(),
                new IdobataMessage(new MessageDirector(new MessageBuilder(message)).addUser(users).getResult()),
                String.class);
    }
}

interface IdobataMessageBuilder {
    void makeUser(String[] users);

    String getResult();
}

class MessageDirector {
    private IdobataMessageBuilder builder;

    MessageDirector(IdobataMessageBuilder builder) {
        this.builder = builder;
    }

    IdobataMessageBuilder addUser(String... users) {
        builder.makeUser(users);
        return builder;
    }
}

class MessageBuilder implements IdobataMessageBuilder {
    private StringBuilder idobataMessage;
    private String message;

    MessageBuilder(String message) {
        idobataMessage = new StringBuilder();
        this.message = message;
    }

    @Override
    public void makeUser(String[] users) {
        for (String str : users) {
            idobataMessage.append("@" + str + " ");
        }
    }

    @Override
    public String getResult() {
        if (message.isEmpty()) {
            throw new IllegalArgumentException();
        }
        idobataMessage.append(message);
        return idobataMessage.toString();
    }
}
