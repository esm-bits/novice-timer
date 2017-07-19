package jp.co.esm.novicetimer.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
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

    @GetMapping
    public List<Agenda> getAgendas() {
        return agendaService.findAll();
    }

    @GetMapping("{id}")
    public Agenda getAgenda(@PathVariable Integer id) {
        return agendaService.findOne(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Agenda creatAgenda(@RequestBody Agenda agenda) {
        return agendaService.create(agenda);
    }

    @PutMapping("{id}/subjects/{number}")
    public ResponseEntity<String> operateTimer(@PathVariable Integer id,
        @PathVariable Integer number,
        @RequestBody TimerState timerState) {

        try {
            agendaService.changeTimerState(id, number, timerState.getState());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({ IllegalArgumentException.class })
    @ResponseBody
    public void handleIllegalArgumentException() {
    }
}
