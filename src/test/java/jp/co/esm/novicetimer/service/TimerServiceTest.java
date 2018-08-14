package jp.co.esm.novicetimer.service;

import jp.co.esm.novicetimer.domain.Subject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TimerServiceTest {
    @Autowired
    TimerService timerService;

    @Test
    public void isMovingを呼ぶとTimerが動いている場合True_動いていない場合Falseであることの確認(){
        timerService.startTimer(new Subject("test", 1, "user"));
        assertTrue(timerService.isRunning());
        timerService.stopTimer();
        assertFalse(timerService.isRunning());
    }
}
