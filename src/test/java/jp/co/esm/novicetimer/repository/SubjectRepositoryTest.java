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

import jp.co.esm.novicetimer.domain.Agenda;
import jp.co.esm.novicetimer.domain.Subject;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SubjectRepositoryTest {

    @Autowired
    SubjectRepository db;

    @Before
    public void setUp() {
        db.deleteAllAgendas();
    }

    @Test
    public void findSubjectsInAgendaメソッドでアジェンダに紐づくサブジェクトを全て取得すること() throws Exception {
        db.insertSubject(1, new Subject("test1", 1, "user1"));
        List<Subject> sj = db.findSubjectsInAgenda(1);
        assertThat(sj.get(0), is(new Subject("test1", 1, "user1")));
    }

    // 類似品
    @Test
    public void insertSubjectでサブジェクトを1つ追加できること() throws Exception {
        assertTrue(db.insertSubject(1, new Subject("insert1", 1, "insert1")));
        List<Subject> sj = db.findSubjectsInAgenda(1);
        assertThat(sj.get(0), is(new Subject("insert1", 1, "insert1")));
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
        db.insertSubject(1, new Subject("delete1", 1, "delete1"));
        db.insertSubject(2, new Subject("delete2", 2, "delete2"));

        assertTrue(db.deleteOneAgenda(1));
        assertThat(db.findSubjectsInAgenda(1).size(), is(0));
        assertThat(db.findSubjectsInAgenda(2).get(0), is(new Subject("delete2", 2, "delete2")));
    }

    @Test
    public void deleteAllAgendasで全てのアジェンダを削除できること() throws Exception {
        db.insertSubject(1, new Subject("delete1", 1, "delete1"));
        db.insertSubject(2, new Subject("delete2", 2, "delete2"));

        assertTrue(db.deleteAllAgendas());
        assertThat(db.findSubjectsInAgenda(1).size(), is(0));
        assertThat(db.findSubjectsInAgenda(2).size(), is(0));
    }
}
