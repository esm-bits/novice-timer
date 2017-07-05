package jp.co.esm.novicetimer.service;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import jp.co.esm.novicetimer.domain.Agenda;
import jp.co.esm.novicetimer.domain.StatusCode;
import jp.co.esm.novicetimer.domain.Subject;
import jp.co.esm.novicetimer.domain.TimerStateCode;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AgendaServiceTest {
    @Autowired
    AgendaService agendaService;

    @Before
    public void setup() {
        List<Subject> subject = new ArrayList<>();
        subject.add(new Subject("test", 1, "user"));
        Agenda agenda = new Agenda();
        agenda.setId(0);
        agenda.setSubjects(subject);
        agendaService.create(agenda);
    }

    @Test
    public void タイマーが正常にスタートしてOKが返ってくる() {
        assertThat(agendaService.changeTimerState(1, 0, TimerStateCode.START), is(StatusCode.OK));
    }

    @Test
    public void タイマーを正常にstopしてOKが返ってくる() {
        agendaService.changeTimerState(1, 0, TimerStateCode.START);
        assertThat(agendaService.changeTimerState(1, 0, TimerStateCode.STOP), is(StatusCode.OK));
    }

    @Test
    public void 登録されていないidを渡すとNOT_FOUNDが返ってくる() {
        assertThat(agendaService.changeTimerState(-1, 0, TimerStateCode.START), is(StatusCode.NOT_FOUND));
    }

    @Test
    public void 登録されていないsubjectを渡すとNOT_FOUNDが返ってくる() {
        assertThat(agendaService.changeTimerState(1, 1, TimerStateCode.START), is(StatusCode.NOT_FOUND));
    }

    @Test
    public void stateにnullを渡すとBAD_REQUESTが返ってくる() {
        assertThat(agendaService.changeTimerState(1, 0, null), is(StatusCode.BAD_REQUEST));
    }
}
