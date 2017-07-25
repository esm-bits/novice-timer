package jp.co.esm.novicetimer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.esm.novicetimer.domain.Agenda;
import jp.co.esm.novicetimer.domain.Subject;
import jp.co.esm.novicetimer.domain.TimerStateCode;
import jp.co.esm.novicetimer.repository.AgendaRepository;

@Service
public class AgendaService {
    @Autowired
    private AgendaRepository agendaRepository;

    @Autowired
    private TimerService timerService;

    public List<Agenda> findAll() {
        return agendaRepository.getAgendas();
    }

    public Agenda findOne(Integer id) throws IllegalArgumentException {
        Agenda agenda = agendaRepository.getAgenda(id);
        if (agenda == null) {
            throw new IllegalArgumentException();
        }
        return agenda;
    }

    public Agenda create(Agenda agenda) {
        return agendaRepository.save(agenda);
    }

    public Agenda update(Agenda agenda) throws IllegalArgumentException {
        findOne(agenda.getId());
        return agendaRepository.save(agenda);
    }

    public Agenda updateSubject(int id, int number, Subject subject) throws IllegalArgumentException {
        Agenda agenda = findOne(id);
        if (number < 0 || agenda.getSubjects().size() <= number) {
            throw new IllegalArgumentException();
        }

        agenda.getSubjects().set(number, subject);
        return agendaRepository.save(agenda);
    }

    public boolean changeTimerState(int id, int number, TimerStateCode state) throws Exception {
        Agenda agenda = agendaRepository.getAgenda(id);
        if (agenda == null) {
            throw new IllegalArgumentException();
        } else if (number >= agenda.getSubjects().size() || number < 0) {
            throw new IndexOutOfBoundsException();
        }

        switch (state) {
        case START:
            timerService.startTimer(agenda.getSubjects().get(number));
            return true;
        case STOP:
            timerService.stopTimer();
            return true;
        default:
            throw new IllegalArgumentException();
        }
    }
}
