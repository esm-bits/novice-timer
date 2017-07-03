package jp.co.esm.novicetimer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.esm.novicetimer.domain.Agenda;
import jp.co.esm.novicetimer.repository.AgendaRepository;

@Service
public class AgendaService {
    @Autowired
    private AgendaRepository agendaRepository;

    private TimerService timerService = new TimerService();

    public Agenda create(Agenda agenda) {
        return agendaRepository.save(agenda);
    }

    //ここでタイマーの開始、停止を行う？
    public boolean changeTimerState(int id, int number, String state) {
        Agenda agenda = agendaRepository.getAgenda(id);
        if(agenda == null) {//アジェンダが無かった場合
            return false;
        }else if (number >= agenda.getSubjects().size()) {//numberがsubjectの大きさを超えていた場合
            return false;
        }
        //もうちょっとfalseを返す条件を増やすと思った

        if (("start").equals(state)) {//stateがスタートだった場合
            timerService.startTimer(agenda.getSubjects().get(number));
            return true;
        } else if (("stop").equals(state)) {//stateがストップだった場合
            return timerService.stopTimer();
        }
        return false;//どちらでもなかった場合
    }
}
