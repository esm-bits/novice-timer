package jp.co.esm.novicetimer.service;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import jp.co.esm.novicetimer.domain.TimeLimit;

@Service
public class TimerService {

    Timer timer;

    boolean timerExists = false;

    public String startTimer(TimeLimit timerLimit) {
        int seconds = timerLimit.getSeconds();

        System.out.println("start:" + seconds + "秒");

        if (timer != null && timerExists) {
            timer.cancel();
        }

        timer = new Timer();

        timerExists = true;

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("ピピピ" + seconds + "秒経ちました");
                timerExists = false;
            }
        }, TimeUnit.SECONDS.toMillis(seconds));

        return String.valueOf(seconds);
    }

    public boolean stopTimer() {
        if (timerExists) {
            timer.cancel();
            timerExists = false;
            return true;
        }
        return false;
    }

}
