package jp.co.esm.novicetimer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.esm.novicetimer.domain.Agenda;
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

    /**@throws 対応したアジェンダがなければ IllegalArgmentException*/
    public Agenda findOne(Integer id) {
        Agenda agenda = agendaRepository.getAgenda(id);
        if (agenda == null) {
            throw new IllegalArgumentException();
        }
        return agenda;
    }

    /**@return idを割り振られたagendaが返る*/
    public Agenda create(Agenda agenda) {
        return agendaRepository.save(agenda);
    }

    /**
     * @throws 指定したidが見つからなければ IllegalArgumentException
     * @throws TimerStateCodeが(START or STOP)以外だったら IllegalArgumentException
     * @throws subjectが見つからないかnumberが範囲外なら IndexOutOfBoundsException
     * @return 戻り値はtrueのみ
     * */
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
