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
public class SubjectRepositoryTest {

    @Autowired
    SubjectRepository db;

    @Before
    public void setUp() {
        db.deleteAllAgendas();
    }

    @Test
    public void insertSubjectListでサブジェクトを複数追加できること() throws Exception {
        final List<Subject> subjects = new ArrayList<>();
        subjects.add(new Subject("inserts1", 1, "inserts1"));
        subjects.add(new Subject("inserts2", 2, "inserts2"));
        Agenda agenda = new Agenda(1, subjects);
        assertTrue(db.insertSubjectList(agenda.getId(), agenda.getSubjects()));
        List<Subject> sj = db.findSubjectsInAgenda(agenda.getId());
        assertThat(sj.get(0), is(new Subject("inserts1", 1, "inserts1")));
        assertThat(sj.get(1), is(new Subject("inserts2", 2, "inserts2")));
    }

    @Test
    public void deleteOneAgendaでアジェンダIDに紐づくサブジェクトを削除できること() throws Exception {
        final List<Subject> subjects = new ArrayList<>();
        subjects.add(new Subject("delete1", 1, "delete1"));
        Agenda agenda = new Agenda(1, subjects);
        db.insertSubjectList(agenda.getId(), agenda.getSubjects());
        subjects.clear();
        subjects.add(new Subject("delete2", 2, "delete2"));
        agenda = new Agenda(2, subjects);
        db.insertSubjectList(agenda.getId(), agenda.getSubjects());

        assertTrue(db.deleteOneAgenda(1));
        assertThat(db.findSubjectsInAgenda(1).size(), is(0));
        assertThat(db.findSubjectsInAgenda(2).get(0), is(new Subject("delete2", 2, "delete2")));
    }

    @Test
    public void deleteAllAgendasで全てのアジェンダを削除できること() throws Exception {
        final List<Subject> subjects = new ArrayList<>();
        subjects.add(new Subject("delete1", 1, "delete1"));
        subjects.add(new Subject("delete2", 2, "delete2"));
        Agenda agenda = new Agenda(1, subjects);
        db.insertSubjectList(agenda.getId(), agenda.getSubjects());

        assertTrue(db.deleteAllAgendas());
        assertThat(db.findSubjectsInAgenda(1).size(), is(0));
        assertThat(db.findSubjectsInAgenda(2).size(), is(0));
    }
}
