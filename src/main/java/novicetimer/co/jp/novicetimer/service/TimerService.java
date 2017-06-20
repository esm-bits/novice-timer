package novicetimer.co.jp.novicetimer.service;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import novicetimer.co.jp.novicetimer.domain.TimerDomain;

@Service
public class TimerService {
    public String timerStart(TimerDomain timerDomain) {
        int time = timerDomain.getTimeSeconds();

        System.out.println("start:" + time + "秒");

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("ピピピ" + time + "秒経ちました");
            }
        }, TimeUnit.SECONDS.toMillis(time));

        return String.valueOf(time);
    }
}
