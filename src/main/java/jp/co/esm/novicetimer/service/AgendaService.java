package jp.co.esm.novicetimer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.esm.novicetimer.domain.Agenda;
import jp.co.esm.novicetimer.domain.StatusCode;
import jp.co.esm.novicetimer.domain.TimerStateCode;
import jp.co.esm.novicetimer.repository.AgendaRepository;

@Service
public class AgendaService {
    @Autowired
    private AgendaRepository agendaRepository;

    @Autowired
    private TimerService timerService;

    public Agenda create(Agenda agenda) {
        return agendaRepository.save(agenda);
    }

    public StatusCode changeTimerState(int id, int number, TimerStateCode state) {
        Agenda agenda = agendaRepository.getAgenda(id);
        if (agenda == null) {
            return StatusCode.NOT_FOUND;
        } else if (number >= agenda.getSubjects().size() || number < 0) {
            return StatusCode.NOT_FOUND;
        } else if(state == null) {
            return StatusCode.BAD_REQUEST;
        }

        switch (state) {
        case START:
            timerService.startTimer(agenda.getSubjects().get(number));
            return StatusCode.OK;
        case STOP:
            return timerService.stopTimer() ? StatusCode.OK : StatusCode.NOT_FOUND;
        default:
            return StatusCode.BAD_REQUEST;
        }
    }
}
