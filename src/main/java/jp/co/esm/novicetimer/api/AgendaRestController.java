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
import jp.co.esm.novicetimer.domain.TimerStateCode;
import jp.co.esm.novicetimer.service.AgendaService;

@RestController
@RequestMapping("api/agendas")
public class AgendaRestController {
    @Autowired
    private AgendaService agendaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Agenda creatAgendas(@RequestBody Agenda agenda) {
        return agendaService.create(agenda);
    }

    @PutMapping("{id}/subjects/{number}")
    public ResponseEntity<String> operateTimers(@PathVariable Integer id,
                                                @PathVariable Integer number,
                                                @RequestBody TimerState timerState) {
        TimerStateCode state;
        switch (timerState.getState()) {
        case "start":
            state = TimerStateCode.START;
            break;
        case "stop":
            state = TimerStateCode.STOP;
            break;
        default:
            state = null;
            break;
        }

        HttpStatus status = null;
        switch (agendaService.changeTimerState(id, number, state)) {
        case OK:
            status = HttpStatus.OK;
            break;
        case BAD_REQUEST:
            status = HttpStatus.BAD_REQUEST;
            break;
        case NOT_FOUND:
            status = HttpStatus.NOT_FOUND;
            break;
        default:
            status = HttpStatus.NOT_FOUND;
            break;
        }

        return new ResponseEntity<>(status);
    }
}
