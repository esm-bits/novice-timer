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
        private AgendaService agendaService;

        private int setupAgendaId;

        @Before
        public void setup() {
            List<Subject> subjects = new ArrayList<>();
            subjects.add(new Subject("test", 1, "user"));
            Agenda agenda = new Agenda();
            agenda.setSubjects(subjects);
            agendaService.create(agenda);

            setupAgendaId = agenda.getId();
        }

        @Test
        public void タイマーが正常にstartするとtrueが返ってくる() throws Exception {
            assertTrue(agendaService.changeTimerState(setupAgendaId, 0, TimerStateCode.START));
        }

        @Test
        public void タイマーを正常にstopするとtrueが返ってくる() throws Exception {
            agendaService.changeTimerState(setupAgendaId, 0, TimerStateCode.START);
            assertTrue(agendaService.changeTimerState(setupAgendaId, 0, TimerStateCode.STOP));
        }

        @Test(expected = IllegalArgumentException.class)
        public void 登録されていないidを渡すとIllegalArgumentExceptionがスローされる() throws Exception {
            agendaService.changeTimerState(-1, 0, TimerStateCode.START);
        }

        @Test(expected = IndexOutOfBoundsException.class)
        public void 登録されていないsubjectを渡すとIllegalArgumentExceptionがスローされる() throws Exception {
            agendaService.changeTimerState(setupAgendaId, 1, TimerStateCode.START);
        }
    }

    @RunWith(SpringRunner.class)
    @SpringBootTest
    public static class updateメソッドのテスト {
        @Autowired
        private AgendaService agendaService;

        private Agenda newAgenda;

        private int setupAgendaId;

        @Before
        public void setup() {
            List<Subject> subjects = new ArrayList<>();
            subjects.add(new Subject("test", 1, "user"));
            Agenda agenda = new Agenda();
            agenda.setSubjects(subjects);
            agendaService.create(agenda);

            setupAgendaId = agenda.getId();

            List<Subject> newSubjects = new ArrayList<>();
            newSubjects.add(new Subject("new_test", 3, "new_user"));
            newAgenda = new Agenda();
            newAgenda.setSubjects(newSubjects);
        }

        @Test
        public void updateを呼び出した場合_idに対応するアジェンダの内容が更新され_更新後のアジェンダが返される() {
            newAgenda.setId(setupAgendaId);
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
        private AgendaService agendaService;

        private Subject newSubjects;

        private int setupAgendaId;

        @Before
        public void setup() {
            List<Subject> subjects = new ArrayList<>();
            subjects.add(new Subject("test", 1, "user"));
            Agenda agenda = new Agenda();
            agenda.setSubjects(subjects);
            agendaService.create(agenda);

            setupAgendaId = agenda.getId();

            newSubjects = new Subject("new_test", 3, "new_user");
        }

        @Test
        public void updateSubjectを呼び出した場合_idに対応するアジェンダのnumber番目のサブジェクトが更新され_更新後のアジェンダが返される() {
            List<Subject> subjects = new ArrayList<>();
            subjects.add(newSubjects);
            Agenda agenda = new Agenda();
            agenda.setSubjects(subjects);
            agenda.setId(setupAgendaId);

            assertThat(agendaService.updateSubject(setupAgendaId, 0, newSubjects), is(agenda));
        }

        @Test(expected = IllegalArgumentException.class)
        public void updateSubjectを呼び出し_idに対応するアジェンダがない場合_IllegalArgumentExceptionがスローされる() throws Exception {
            agendaService.updateSubject(0, 0, newSubjects);
        }

        @Test(expected = IllegalArgumentException.class)
        public void updateSubjectを呼び出し_numberが不適切な場合_IllegalArgumentExceptionがスローされる() throws Exception {
            agendaService.updateSubject(setupAgendaId, 1, newSubjects);
        }
    }

    @RunWith(SpringRunner.class)
    @SpringBootTest
    public static class deleteAgendaProcessのテスト {
        @Autowired
        private AgendaService agendaService;

        private int setupAgendaId;

        @Before
        public void setup() {
            List<Subject> subjects = new ArrayList<>();
            subjects.add(new Subject("test", 1, "user"));
            Agenda agenda = new Agenda();
            agenda.setSubjects(subjects);
            agendaService.create(agenda);

            setupAgendaId = agenda.getId();
        }

        @Test
        public void id指定でアジェンダを削除できたときtrueが返ってくる() {
            assertThat(agendaService.deleteAgendaProcess(setupAgendaId), is(true));
        }

        @Test
        public void id指定でidのアジェンダがなかったときfalseが返ってくる() {
            assertThat(agendaService.deleteAgendaProcess(0), is(false));
        }
    }
}
