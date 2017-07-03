package jp.co.esm.novicetimer.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jp.co.esm.novicetimer.domain.Agenda;
import jp.co.esm.novicetimer.domain.TimerState;
import jp.co.esm.novicetimer.service.AgendaService;

@RestController
@RequestMapping("api/agendas")
public class AgendaRestController {
    @Autowired
    private AgendaService agendaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Agenda postAgendas(@RequestBody Agenda agenda) {
        return agendaService.create(agenda);
    }

    @PutMapping("{id}/subjects/{number}")
    public ResponseEntity<String> putTimers(@PathVariable Integer id,
                                            @PathVariable Integer number,
                                            @RequestBody TimerState timerState) {

        HttpStatus status = agendaService.changeTimerState(id, number, timerState.getState())
                            ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(status);
    }
}
