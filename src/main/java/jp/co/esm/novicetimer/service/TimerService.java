package jp.co.esm.novicetimer.service;

<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> 時間計測処理の場所と内容を修正、time変数の名前を変更、不要なコードを削除
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

<<<<<<< HEAD
import org.springframework.stereotype.Service;

import jp.co.esm.novicetimer.domain.TimeLimit;

@Service
public class TimerService {
    public String startTimer(TimeLimit timeLimit) {
        int seconds = timeLimit.getSeconds();

        System.out.println("start:" + seconds + "秒");

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("ピピピ" + seconds + "秒経ちました");
            }
        }, TimeUnit.SECONDS.toMillis(seconds));

        return String.valueOf(seconds);
=======
import org.springframework.beans.factory.annotation.Autowired;
=======
>>>>>>> 時間計測処理の場所と内容を修正、time変数の名前を変更、不要なコードを削除
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

<<<<<<< HEAD
    public String create(TimerDomain timerDomain) {
        return timerRepository.timerStart(timerDomain);
>>>>>>> POSTした値を計るタイマー作成
=======
        return String.valueOf(time);
>>>>>>> 時間計測処理の場所と内容を修正、time変数の名前を変更、不要なコードを削除
    }
}
