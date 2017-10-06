package jp.co.esm.novicetimer.service;

import jp.co.esm.novicetimer.domain.Agenda;
import jp.co.esm.novicetimer.domain.Subject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

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
        assertTrue(timerService.isMoving());
        timerService.stopTimer();
        assertFalse(timerService.isMoving());
    }
}
