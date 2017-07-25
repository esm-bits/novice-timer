package jp.co.esm.novicetimer.service;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import jp.co.esm.novicetimer.domain.Agenda;
import jp.co.esm.novicetimer.domain.Subject;
import jp.co.esm.novicetimer.domain.TimerStateCode;

@RunWith(Enclosed.class)
public class AgendaServiceTest {
    @RunWith(SpringRunner.class)
    @SpringBootTest
    public static class changeTimerStateメソッドのテスト {
        @Autowired
        AgendaService agendaService;

        @Before
        public void setup() {
            List<Subject> subject = new ArrayList<>();
            subject.add(new Subject("test", 1, "user"));
            Agenda agenda = new Agenda();
            agenda.setSubjects(subject);
            agendaService.create(agenda);
        }

        @Test
        public void タイマーが正常にstartするとtrueが返ってくる() throws Exception {
            assertTrue(agendaService.changeTimerState(1, 0, TimerStateCode.START));
        }

        @Test
        public void タイマーを正常にstopするとtrueが返ってくる() throws Exception {
            agendaService.changeTimerState(1, 0, TimerStateCode.START);
            assertTrue(agendaService.changeTimerState(1, 0, TimerStateCode.STOP));
        }

        @Test(expected = IllegalArgumentException.class)
        public void 登録されていないidを渡すとFileNotFoundExceptionがスローされる() throws Exception {
            agendaService.changeTimerState(-1, 0, TimerStateCode.START);
        }

        @Test(expected = IndexOutOfBoundsException.class)
        public void 登録されていないsubjectを渡すとFileNotFoundExceptionがスローされる() throws Exception {
            agendaService.changeTimerState(1, 1, TimerStateCode.START);
        }
    }

    @RunWith(SpringRunner.class)
    @SpringBootTest
    public static class updateメソッドのテスト {
        @Autowired
        AgendaService agendaService;

        Agenda newAgenda;

        @Before
        public void setup() {
            List<Subject> subject = new ArrayList<>();
            subject.add(new Subject("test", 1, "user"));
            Agenda agenda = new Agenda();
            agenda.setSubjects(subject);
            agendaService.create(agenda);

            List<Subject> newSubject = new ArrayList<>();
            newSubject.add(new Subject("new_test", 3, "new_user"));
            newAgenda = new Agenda();
            newAgenda.setSubjects(newSubject);
        }

        @Test
        public void updateを呼び出した場合_idに対応するアジェンダの内容が更新され_更新後のアジェンダが返される() {
            newAgenda.setId(1);
            assertThat(agendaService.update(newAgenda), is(newAgenda));
        }

        @Test(expected = IllegalArgumentException.class)
        public void updateを呼び出し_idに対応するアジェンダがない場合_IllegalArgumentExceptionがスローされる() throws Exception {
            newAgenda.setId(0);
            agendaService.update(newAgenda);
        }
    }

    @RunWith(SpringRunner.class)
    @SpringBootTest
    public static class updateSubjectメソッドのテスト {
        @Autowired
        AgendaService agendaService;

        Subject newSubject;

        @Before
        public void setup() {
            List<Subject> subject = new ArrayList<>();
            subject.add(new Subject("test", 1, "user"));
            Agenda agenda = new Agenda();
            agenda.setSubjects(subject);
            agendaService.create(agenda);

            newSubject = new Subject("new_test", 3, "new_user");
        }

        @Test
        public void updateSubjectを呼び出した場合_idに対応するアジェンダのnumber番目のサブジェクトが更新され_更新後のアジェンダが返される() {
            List<Subject> subject = new ArrayList<>();
            subject.add(newSubject);
            Agenda agenda = new Agenda();
            agenda.setSubjects(subject);
            agenda.setId(1);

            assertThat(agendaService.updateSubject(1, 0, newSubject), is(agenda));
        }

        @Test(expected = IllegalArgumentException.class)
        public void updateSubjectを呼び出し_idに対応するアジェンダがない場合_IllegalArgumentExceptionがスローされる() throws Exception {
            agendaService.updateSubject(0, 0, newSubject);
        }

        @Test(expected = IllegalArgumentException.class)
        public void updateSubjectを呼び出し_numberが不適切な場合_IllegalArgumentExceptionがスローされる() throws Exception {
            agendaService.updateSubject(1, 1, newSubject);
        }
    }
}
