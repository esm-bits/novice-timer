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

    /**
     * アジェンダの更新を行います。
     * <p>
     * アジェンダをagendaRepositoryに登録することで置き換え、置き換えた後のagendaを返します。
     *
     * @param agenda 更新するアジェンダ
     * @return 更新された後のアジェンダ
     * @throws IllegalArgumentException 更新されるアジェンダが無い場合に投げられます
     */
    public Agenda update(Agenda agenda) throws IllegalArgumentException {
        findOne(agenda.getId());
        return agendaRepository.save(agenda);
    }

    /**
     * サブジェクトの更新を行います。
     * <p>
     * idが対応するアジェンダのnumber番目のサブジェクトを置き換え、置き換えた後のagendaを返します。
     *
     * @param id サブジェクトの更新を行うアジェンダのid
     * @param number 更新されるサブジェクトの番目
     * @param subject 更新するサブジェクト
     * @return サブジェクトを更新した後のアジェンダ
     * @throws IllegalArgumentException idが対応するアジェンダがない場合、number番目のサブジェクトがない場合に投げられます
     */
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
