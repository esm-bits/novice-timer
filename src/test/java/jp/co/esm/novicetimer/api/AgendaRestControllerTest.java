package jp.co.esm.novicetimer.api;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import jp.co.esm.novicetimer.domain.Agenda;
import jp.co.esm.novicetimer.domain.Subject;
import jp.co.esm.novicetimer.domain.TimerStateCode;
import jp.co.esm.novicetimer.service.AgendaService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AgendaRestControllerTest {
    private MockMvc mvc;

    @InjectMocks
    private AgendaRestController agendaRestController;

    @Mock
    private AgendaService agendaService;

    private ObjectMapper mapper = new ObjectMapper();

    private Agenda agenda;

    @Before
    public void setup() {
        mvc = MockMvcBuilders.standaloneSetup(this.agendaRestController).build();

        agenda = new Agenda();
        agenda.setId(1);
        List<Subject> subjects = new ArrayList<>();
        subjects.add(new Subject("タイトル", 5, "user1"));
        agenda.setSubjects(subjects);
    }

    @Test
    public void api_agendasにGETリクエストすると_200OKとアジェンダのリストが返される() throws Exception {
        when(this.agendaService.findAll()).thenReturn(new ArrayList<>());
        mvc
            .perform(get("/api/agendas"))
            .andExpect(status().isOk())
            .andExpect(content().json(mapper.writeValueAsString(new ArrayList<>())));
    }

    @Test
    public void api_agendas_idにGETリクエストし_対応するアジェンダが返された場合_200OKとアジェンダが返される() throws Exception {
        when(this.agendaService.findOne(1)).thenReturn(agenda);
        mvc
            .perform(get("/api/agendas/1"))
            .andExpect(status().isOk())
            .andExpect(content().json(mapper.writeValueAsString(agenda)));
    }

    @Test
    public void api_agendas_idにGETリクエストし_IllegalArgumentExceptionが投げられた場合_404NotFoundが返される() throws Exception {
        doThrow(new IllegalArgumentException()).when(this.agendaService).findOne(1);
        mvc
            .perform(get("/api/agendas/1"))
            .andExpect(status().isNotFound());
    }

    @Test
    public void api_agendasにPOSTリクエストし_ボディにアジェンダを持たせた場合_201Createdとアジェンダが返される() throws Exception {
        when(this.agendaService.create(agenda)).thenReturn(agenda);
        mvc
            .perform(post("/api/agendas")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(agenda)))
            .andExpect(status().isCreated())
            .andExpect(content().json(mapper.writeValueAsString(agenda)));
    }

    @Test
    public void api_agendas_idにPUTリクエストし_ボディにアジェンダを持たせ_そのアジェンダが返された場合_200OKとアジェンダが返される() throws Exception {
        when(this.agendaService.update(agenda)).thenReturn(agenda);
        mvc
            .perform(put("/api/agendas/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(agenda)))
            .andExpect(status().isOk())
            .andExpect(content().json(mapper.writeValueAsString(agenda)));
    }

    @Test
    public void api_agendas_idにPUTリクエストし_ボディにアジェンダを持たせ_IllegalArgumentExceptionが投げられた場合_404NotFoundが返される() throws Exception {
        doThrow(new IllegalArgumentException()).when(this.agendaService).update(agenda);
        mvc
            .perform(put("/api/agendas/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(agenda)))
            .andExpect(status().isNotFound());
    }

    @Test
    public void api_agendas_id_subjects_numberにPUTリクエストし_ボディにSubjectを持たせ_IllegalArgumentExceptionが投げられた場合_404NotFoundが返される() throws Exception {
        Subject subject = new Subject("title", 5, "user");

        doThrow(new IllegalArgumentException()).when(this.agendaService).updateSubject(1, 0, subject);
        mvc
            .perform(put("/api/agendas/1/subjects/0")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(subject)))
            .andExpect(status().isNotFound());
    }

    @Test
    public void api_agendas_id_subjects_numberにPUTリクエストし_ボディにSubjectを持たせ_アジェンダが返された場合_200OKと更新されたアジェンダが返される() throws Exception {
        Subject subject =  new Subject("title", 5, "user");

        when(this.agendaService.updateSubject(1, 0, subject)).thenReturn(agenda);
        mvc
            .perform(put("/api/agendas/1/subjects/0")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(subject)))
            .andExpect(status().isOk())
            .andExpect(content().json(mapper.writeValueAsString(agenda)));
    }

    @Test
    public void api_agendas_id_subjects_number_timetsにPUTリクエストし_IllegalArgumentExceptionが投げられた場合_400BadRequestが返される() throws Exception {
        doThrow(new IllegalArgumentException()).when(this.agendaService).changeTimerState(1, 0, TimerStateCode.START);
        mvc
            .perform(put("/api/agendas/1/subjects/0/timers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"state\": \"start\"}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void api_agendas_id_subjects_number_timersにPUTリクエストし_IndexOutOfBoundsExceptionが投げられた場合_404NotFoundが返される() throws Exception {
        doThrow(new IndexOutOfBoundsException()).when(this.agendaService).changeTimerState(1, 0, TimerStateCode.START);
        mvc
            .perform(put("/api/agendas/1/subjects/0/timers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"state\": \"start\"}"))
            .andExpect(status().isNotFound());
    }

    @Test
    public void api_agendas_id_subjects_number_timersにPUTリクエストし_ボディに適切なTimerStateを持たせた場合_200OKが返される() throws Exception {
        when(this.agendaService.changeTimerState(1, 0, TimerStateCode.START)).thenReturn(true);
        mvc
            .perform(put("/api/agendas/1/subjects/0/timers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"state\": \"start\"}"))
            .andExpect(status().isOk());
    }
}
