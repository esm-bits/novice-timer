package novicetimer.co.jp.novicetimer.repository;

import java.util.Timer;
import java.util.TimerTask;

import org.springframework.stereotype.Repository;

import novicetimer.co.jp.novicetimer.domain.TimerDomain;

@Repository
public class TimerRepository {
    public String timerStart(TimerDomain timerDomain) {
        int time = timerDomain.getTime();

        System.out.println("start:" + time + "秒");

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("ピピピ" + time + "秒経ちました");
            }
        }, time * 1000);

        return String.valueOf(time);
    }
}
