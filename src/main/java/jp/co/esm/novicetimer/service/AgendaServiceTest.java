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
import jp.co.esm.novicetimer.domain.Subject;
import jp.co.esm.novicetimer.domain.TimerStateCode;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AgendaServiceTest {

    @Autowired
    AgendaService agendaService;

    int i = 0;

    @Before
    public void setup() {
        List<Subject> subject = new ArrayList<>();
        subject.add(new Subject("test", 1, "user"));
        Agenda agenda = new Agenda();
        //agenda.setId(1);
        agenda.setSubjects(subject);
        agendaService.create(agenda);
        i++;
    }

    @Test
    public void タイマーが正常にstartするとtrueが返ってくる() throws Exception {
        assertTrue(agendaService.changeTimerState(i, 0, TimerStateCode.START));
    }

    @Test
    public void タイマーを正常にstopするとtrueが返ってくる() throws Exception {
        agendaService.changeTimerState(i, 0, TimerStateCode.START);
        assertTrue(agendaService.changeTimerState(i, 0, TimerStateCode.STOP));
    }

    @Test(expected = IllegalArgumentException.class)
    public void 登録されていないidを渡すとIllegalArgumentExceptionがスローされる() throws Exception {
        agendaService.changeTimerState(-1, 0, TimerStateCode.START);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void 登録されていないsubjectを渡すとIndexOutOfBoundsExceptionがスローされる() throws Exception {
        agendaService.changeTimerState(i, 1, TimerStateCode.START);
    }

    @Test
    public void id指定でアジェンダを削除できたときtrueが返ってくる() {
        assertThat(agendaService.deleteAgendaProcess(i), is(true));
    }

    @Test
    public void id指定でidのアジェンダがなかったときfalseが返ってくる() {
        assertThat(agendaService.deleteAgendaProcess(0), is(false));
    }

}
