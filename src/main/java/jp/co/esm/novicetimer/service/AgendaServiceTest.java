package jp.co.esm.novicetimer.service;

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
    public void タイマーが正常にスタートしてtrueが返ってくる() {
        assertTrue(agendaService.changeTimerState(1, 0, "start"));
    }

    @Test
    public void タイマーを正常にstopしてtrueが返ってくる() {
        agendaService.changeTimerState(1, 0, "start");
        assertTrue(agendaService.changeTimerState(1, 0, "stop"));
    }

    @Test
    public void 登録されていないidを渡すとfalseが返ってくる() {
        assertFalse(agendaService.changeTimerState(-1, 0, "start"));
    }

    @Test
    public void 登録されていないsubjectを渡すとfalseが返ってくる() {
        assertFalse(agendaService.changeTimerState(1, 1, "start"));
    }

    @Test
    public void stateがstartかstop以外を渡すとfalseが返ってくる() {
        assertFalse(agendaService.changeTimerState(1, 0, "crate"));
    }

    @Test
    public void stateにnullを渡すとfalseが返ってくる() {
        assertFalse(agendaService.changeTimerState(1, 0, null));
    }
}
