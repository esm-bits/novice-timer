package jp.co.esm.novicetimer.api;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

import jp.co.esm.novicetimer.domain.Subject;
import jp.co.esm.novicetimer.service.TimerService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TimerRestControllerTest {

    private MockMvc mvc;

    @InjectMocks
    private TimerRestController timerRestController;

    @Mock
    private TimerService timerService;

    @Before
    public void setup() throws Exception {
        mvc = MockMvcBuilders.standaloneSetup(this.timerRestController).build();
    }

    @Test
    public void subjectをリクエストボディにしたPOSTリクエストを受けると201を返すこと() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        String title = "テスト";
        int minutes = 1;
        String idobataUser = "hironoriohashi";
        Subject subject = new Subject(title, minutes, idobataUser);

        when(this.timerService.startTimer(subject)).thenReturn(String.valueOf(subject.getMinutes()));
        mvc
            .perform(post("/api/timers/").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(subject)))
            .andExpect(status().isCreated());
    }

    @Test
    public void タイマー動作中にDELETEリクエストを受けると200を返すこと() throws Exception {
        when(this.timerService.stopTimer()).thenReturn(true);
        mvc
            .perform(delete("/api/timers/"))
            .andExpect(status().isOk());
    }

    @Test
    public void タイマーが動作していないときにDELETEリクエストを受けると404を返すこと() throws Exception {
        when(this.timerService.stopTimer()).thenReturn(false);
        mvc
            .perform(delete("/api/timers/"))
            .andExpect(status().isNotFound());
    }
}
