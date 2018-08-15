package jp.co.esm.novicetimer.repository;

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
import org.springframework.transaction.annotation.Transactional;

import jp.co.esm.novicetimer.domain.Agenda;
import jp.co.esm.novicetimer.domain.Subject;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class AgendaRepositoryTest {

    @Autowired
    AgendaRepository ar;

    @Before
    public void setUp() {
        ar.deleteAllAgenda();
    }

    @Test
    public void saveメソッドに渡したAgendaのidが0だった場合に新規登録されていること() {
        List<Subject> sj = new ArrayList<Subject>();
        sj.add(new Subject("test1", 1, "test1"));
        int agendaId = ar.save(new Agenda(0, sj)).getId();

        assertThat(ar.getAgenda(agendaId), is(new Agenda(agendaId, sj)));
    }

    @Test
    public void saveメソッドに渡したAgendaのidが既に存在している場合にAgendaの上書きが行われること() {
        List<Subject> sj = new ArrayList<Subject>();
        sj.add(new Subject("test1", 1, "test1"));
        int agendaId = ar.save(new Agenda(0, sj)).getId();
        sj.clear();
        sj.add(new Subject("test2", 2, "test2"));
        ar.save(new Agenda(agendaId, sj));

        assertThat(ar.getAgenda(agendaId), is(new Agenda(agendaId, sj)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void saveメソッドに渡したAgendaのidが0以外かつ登録されていない場合にIllegalArgsExceptionが投げられること() {
        List<Subject> sj = new ArrayList<Subject>();
        sj.add(new Subject("test1", 1, "test1"));
        ar.save(new Agenda(1, sj));
    }

    @Test
    public void isExistメソッドの引数に存在するidを渡した場合にtrueが返ってくること() {
        List<Subject> sj = new ArrayList<Subject>();
        sj.add(new Subject("test1", 1, "test1"));
        int agendaId = ar.save(new Agenda(0, sj)).getId();

        assertTrue(ar.isExist(agendaId));
    }

    @Test
    public void isExistメソッドの引数に存在しないidを渡した場合にfalseが返ってくること() {
        List<Subject> sj = new ArrayList<Subject>();
        sj.add(new Subject("test1", 1, "test1"));
        ar.save(new Agenda(0, sj)).getId();

        assertFalse(ar.isExist(-1));
    }

    @Test
    public void getAgendaメソッドの引数に存在するidを渡した場合にそのidのAgendaが返ってくること() {
        List<Subject> sj = new ArrayList<Subject>();
        sj.add(new Subject("test1", 1, "test1"));
        int agendaId = ar.save(new Agenda(0, sj)).getId();

        assertThat(ar.getAgenda(agendaId), is(new Agenda(agendaId, sj)));
    }

    @Test
    public void getAgendaメソッドの引数に存在しないidを渡した場合にnullが返ってくること() {
        List<Subject> sj = new ArrayList<Subject>();
        sj.add(new Subject("test1", 1, "test1"));
        ar.save(new Agenda(0, sj)).getId();

        assertNull(ar.getAgenda(-1));
    }

    @Test
    public void getAllAgendaメソッドの戻り値で全てのAgendaがListで返ってくること() {
        List<Agenda> ag = new ArrayList<Agenda>();
        List<Subject> sj = new ArrayList<Subject>();
        sj.add(new Subject("test1", 1, "test1"));
        int agendaId = ar.save(new Agenda(0, sj)).getId();
        ag.add(new Agenda(agendaId, sj));

        sj.clear();
        sj.add(new Subject("test1", 1, "test1"));
        agendaId = ar.save(new Agenda(0, sj)).getId();
        ag.add(new Agenda(agendaId, sj));

        assertThat(ar.getAllAgenda(), is(ag));
    }

    public void deleteAgendaメソッドの引数に存在するidを渡した場合にそのidのAgendaが削除されること() {
        List<Subject> sj = new ArrayList<Subject>();
        sj.add(new Subject("test1", 1, "test1"));
        int agendaId = ar.save(new Agenda(0, sj)).getId();
        assertTrue(ar.deleteAgenda(agendaId));
        assertFalse(ar.isExist(agendaId));
    }

    @Test
    public void deleteAllAgendaメソッドで全てのAgendaが削除されること() {
        List<Subject> sj = new ArrayList<Subject>();
        sj.add(new Subject("test1", 1, "test1"));
        int agendaId1 = ar.save(new Agenda(0, sj)).getId();

        sj.clear();
        sj.add(new Subject("test1", 1, "test1"));
        int agendaId2 = ar.save(new Agenda(0, sj)).getId();

        ar.deleteAllAgenda();
        assertFalse(ar.isExist(agendaId1));
        assertFalse(ar.isExist(agendaId2));
    }
}
