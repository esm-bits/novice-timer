package jp.co.esm.novicetimer.api;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import jp.co.esm.novicetimer.domain.Agenda;
import jp.co.esm.novicetimer.domain.Subject;
import jp.co.esm.novicetimer.repository.AgendaRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AgendaRestControllerGetTest {
    @Autowired
    AgendaRepository agendaRepository;
    @Autowired
    TestRestTemplate restTemplate;
    Agenda agenda1;
    Agenda agenda2;

    @Before
    public void テスト準備() {
        List<Subject> subjects1 = new ArrayList<>();
        subjects1.add(new Subject("subject1", 11, "user1"));
        subjects1.add(new Subject("subject2", 12, "user2"));
        agenda1 = new Agenda();
        agenda1.setSubjects(subjects1);

        List<Subject> subjects2 = new ArrayList<>();
        subjects2.add(new Subject("サブジェクト", 13, "ユーザー"));
        agenda2 = new Agenda();
        agenda2.setSubjects(subjects2);

        if (agendaRepository.getAgendas().size() == 0) {
            // agendaRepositoryへの登録はテストごとにリセットされないので最初だけ行う
            agendaRepository.save(agenda1);
            agendaRepository.save(agenda2);
        } else {
            // agendaRepositoryへ登録しない場合、idが自動付与されないので直接セット
            agenda1.setId(1);
            agenda2.setId(2);
        }
    }

    @Test
    public void api_agendasにGETでリクエストするとAgendaのリストを取得できる() {
        ResponseEntity<List<Agenda>> response = restTemplate.exchange(
                "/api/agendas", HttpMethod.GET, null, new ParameterizedTypeReference<List<Agenda>>(){});
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().size(), is(2));

        Agenda getAgenda1 = response.getBody().get(0);
        assertThat(getAgenda1.getId(), is(agenda1.getId()));
        assertThat(getAgenda1.getSubjects().size(), is(agenda1.getSubjects().size()));
        assertThat(getAgenda1.getSubjects().get(0).getTitle(), is(agenda1.getSubjects().get(0).getTitle()));
        assertThat(getAgenda1.getSubjects().get(0).getMinutes(), is(agenda1.getSubjects().get(0).getMinutes()));
        assertThat(getAgenda1.getSubjects().get(0).getIdobataUser(), is(agenda1.getSubjects().get(0).getIdobataUser()));
        assertThat(getAgenda1.getSubjects().get(1).getTitle(), is(agenda1.getSubjects().get(1).getTitle()));
        assertThat(getAgenda1.getSubjects().get(1).getMinutes(), is(agenda1.getSubjects().get(1).getMinutes()));
        assertThat(getAgenda1.getSubjects().get(1).getIdobataUser(), is(agenda1.getSubjects().get(1).getIdobataUser()));

        Agenda getAgenda2 = response.getBody().get(1);
        assertThat(getAgenda2.getId(), is(agenda2.getId()));
        assertThat(getAgenda2.getSubjects().size(), is(agenda2.getSubjects().size()));
        assertThat(getAgenda2.getSubjects().get(0).getTitle(), is(agenda2.getSubjects().get(0).getTitle()));
        assertThat(getAgenda2.getSubjects().get(0).getMinutes(), is(agenda2.getSubjects().get(0).getMinutes()));
        assertThat(getAgenda2.getSubjects().get(0).getIdobataUser(), is(agenda2.getSubjects().get(0).getIdobataUser()));
    }

    @Test
    public void api_agendas_1にGETリクエストするとidが1のアジェンダを取得できる() {
        ResponseEntity<Agenda> response = restTemplate.exchange(
                "/api/agendas/1", HttpMethod.GET, null, new ParameterizedTypeReference<Agenda>(){});
        assertThat(response.getStatusCode(), is(HttpStatus.OK));

        Agenda getAgenda = response.getBody();
        assertThat(getAgenda.getId(),
                is(agenda1.getId()));
        assertThat(getAgenda.getSubjects().size(), is(agenda1.getSubjects().size()));
        assertThat(getAgenda.getSubjects().get(0).getTitle(), is(agenda1.getSubjects().get(0).getTitle()));
        assertThat(getAgenda.getSubjects().get(0).getMinutes(), is(agenda1.getSubjects().get(0).getMinutes()));
        assertThat(getAgenda.getSubjects().get(0).getIdobataUser(), is(agenda1.getSubjects().get(0).getIdobataUser()));
        assertThat(getAgenda.getSubjects().get(1).getTitle(), is(agenda1.getSubjects().get(1).getTitle()));
        assertThat(getAgenda.getSubjects().get(1).getMinutes(), is(agenda1.getSubjects().get(1).getMinutes()));
        assertThat(getAgenda.getSubjects().get(1).getIdobataUser(), is(agenda1.getSubjects().get(1).getIdobataUser()));
    }

    @Test
    public void api_agendas_2にGETリクエストするとidが2のアジェンダを取得できる() {
        ResponseEntity<Agenda> response = restTemplate.exchange(
                "/api/agendas/2", HttpMethod.GET, null, new ParameterizedTypeReference<Agenda>(){});
        assertThat(response.getStatusCode(), is(HttpStatus.OK));

        Agenda getAgenda = response.getBody();
        assertThat(getAgenda.getId(), is(agenda2.getId()));
        assertThat(getAgenda.getSubjects().size(), is(agenda2.getSubjects().size()));
        assertThat(getAgenda.getSubjects().get(0).getTitle(), is(agenda2.getSubjects().get(0).getTitle()));
        assertThat(getAgenda.getSubjects().get(0).getMinutes(), is(agenda2.getSubjects().get(0).getMinutes()));
        assertThat(getAgenda.getSubjects().get(0).getIdobataUser(), is(agenda2.getSubjects().get(0).getIdobataUser()));
    }

    @Test
    public void api_agendas_3にGETリクエストすると404NotFoundが返される() {
        ResponseEntity<Agenda> response = restTemplate.exchange(
                "/api/agendas/3", HttpMethod.GET, null, new ParameterizedTypeReference<Agenda>(){});
        assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }
}
