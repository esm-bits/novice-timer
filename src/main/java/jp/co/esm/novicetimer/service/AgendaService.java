package jp.co.esm.novicetimer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.esm.novicetimer.domain.Agenda;
import jp.co.esm.novicetimer.domain.TimerStateCode;
import jp.co.esm.novicetimer.repository.AgendaRepository;

/**
 * アジェンダの操作を行うクラス<p>
 * アジェンダの登録、取得、タイマーの状態の変更を行えます。
 */
@Service
public class AgendaService {
    @Autowired
    private AgendaRepository agendaRepository;

    @Autowired
    private TimerService timerService;

    /**
     * アジェンダの全取得を行います。
     * <p>
     * 登録されているアジェンダをListにして返します。
     * @return {@literal List<Agenda>}
     */
    public List<Agenda> findAll() {
        return agendaRepository.getAgendas();
    }

    /**
     * 単一のアジェンダを検索します。
     * <p>
     * 検索したいアジェンダのidを受け取り、見つかった場合はアジェンダを返し、
     * 見つからなかった場合は例外を投げます。
     * @param id 検索したいアジェンダのid
     * @return idと対応したアジェンダ
     * @throws java.lang.IllegalArgumentException 対応したアジェンダが無かった場合に投げられます。
     *  */
    public Agenda findOne(Integer id) throws IllegalArgumentException {
        Agenda agenda = agendaRepository.getAgenda(id);
        if (agenda == null) {
            throw new IllegalArgumentException();
        }
        return agenda;
    }

    /**
     * アジェンダの登録を行います。
     * <p>
     * アジェンダをagendaRepositoryに登録し、登録されたagendaを返します。
     * @param agenda 登録したいアジェンダ
     * @return idを割り振られたagenda
     * */
    public Agenda create(Agenda agenda) {
        return agendaRepository.save(agenda);
    }

    /**
     * タイマーの状態を変更します。
     * <p>
     * アジェンダのid,Subjectのindex,タイマーのstateを指定して
     * タイマーを任意の状態に移行させます。
     * @param id 利用するsubjectを持つアジェンダのid
     * @param number 利用するsujectの番号
     * @param state タイマーをどの状態に遷移させるかのステータス
     * @throws java.lang.IllegalArgumentException 指定したidが見つからなかった場合に投げられます
     * @throws java.lang.IllegalArgumentException TimerStateCodeが(START or STOP)以外だった場合に投げられます
     * @throws java.lang.IndexOutOfBoundsException subjectが見つからないかnumberが範囲外だった場合に投げられます
     * @throws java.lang.Exception 上記以外の例外だった場合に投げられます
     * @return true
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
