package jp.co.esm.novicetimer.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.esm.novicetimer.domain.Agenda;
import jp.co.esm.novicetimer.domain.Subject;
import jp.co.esm.novicetimer.domain.TimerStateCode;
import jp.co.esm.novicetimer.repository.AgendaRepository;

/**
 * アジェンダの操作を行うクラス<p>
 * アジェンダの登録、取得、タイマーの状態の変更を行える。
 */
@Service
public class AgendaService {
    @Autowired
    private AgendaRepository agendaRepository;

    @Autowired
    private TimerService timerService;

    /**
     * 登録されているアジェンダを全て取得する。
     * <p>
     * 登録されているアジェンダを全て取得する。
     * @return List型で全アジェンダを返す
     * @throws Exception
     */
    public List<Agenda> findAll() throws Exception {
        return agendaRepository.getAgendas();
    }

    /**
     * 指定した1つのアジェンダを取得する。
     * <p>
     * 取得したいアジェンダのidを受け取り、見つかった場合はアジェンダを返し、
     * 見つからなかった場合は例外を投げる。
     * @param id 検索したいアジェンダのid
     * @return idと対応したアジェンダ
     * @throws java.lang.IllegalArgumentException 対応したアジェンダが無かった場合に投げられる。
     * @throws java.sql.SQLException DBからのデータ取得に失敗した場合に投げられる
     */
    public Agenda findOne(Integer id) throws IllegalArgumentException, SQLException {
        Agenda agenda = agendaRepository.getAgenda(id);
        if (agenda.getSubjects().size() == 0) {
            throw new IllegalArgumentException();
        }
        return agenda;
    }

    /**
     * アジェンダの登録を行う。
     * <p>
     * アジェンダをagendaRepositoryに登録し、登録されたagendaを返す。
     * @param agenda 登録したいアジェンダ
     * @return idを割り振られたagenda
     * @throws Exception
     * */
    public Agenda create(Agenda agenda) throws Exception {
        return agendaRepository.save(agenda);
    }

    /**
     * @throws SQLException
     * アジェンダの更新を行う。
     * <p>
     * アジェンダをagendaRepositoryに登録することで置き換え、置き換えた後のagendaを返す。
     * @param agenda 更新するアジェンダ
     * @return 更新された後のアジェンダ
     * @throws IllegalArgumentException 更新されるアジェンダが無い場合に投げられます
     * @throws
     */
    public Agenda update(Agenda agenda) throws IllegalArgumentException, SQLException {
        if (!agendaRepository.isExist(agenda.getId())) {
            throw new IllegalArgumentException();
        }

        return agendaRepository.save(agenda);
    }

    /**
     * サブジェクトの更新を行う。
     * <p>
     * idが対応するアジェンダのnumber番目のサブジェクトを置き換え、置き換えた後のagendaを返す。
     * @param id サブジェクトの更新を行うアジェンダのid
     * @param number 更新されるサブジェクトの番目
     * @param subject 更新するサブジェクト
     * @return サブジェクトを更新した後のアジェンダ
     * @throws IllegalArgumentException idが対応するアジェンダがない場合、number番目のサブジェクトがない場合に投げられます
     */
    public Agenda updateSubject(int id, int number, Subject subject) throws IllegalArgumentException, SQLException {
        Agenda agenda = findOne(id);
        if (number < 0 || agenda.getSubjects().size() <= number) {
            throw new IllegalArgumentException();
        }

        agenda.getSubjects().set(number, subject);
        return agendaRepository.save(agenda);
    }

    /**
     * タイマーの状態を変更する。
     * <p>
     * アジェンダのid,Subjectのindex,タイマーのstateを指定して
     * タイマーを任意の状態に移行させる。
     * @param id 利用するsubjectを持つアジェンダのid
     * @param number 利用するsujectの番号
     * @param state タイマーをどの状態に遷移させるかのステータス
     * @throws java.lang.IllegalArgumentException 指定したidが見つからなかった場合に投げられる
     * @throws java.lang.IllegalArgumentException TimerStateCodeが(START or STOP)以外だった場合に投げられる
     * @throws java.lang.IndexOutOfBoundsException subjectが見つからないかnumberが範囲外だった場合に投げられる
     * @throws java.lang.Exception 上記以外の例外だった場合に投げられる
     * @return true
     */
    public boolean changeTimerState(int id, int number, TimerStateCode state) throws Exception {
        Agenda agenda = agendaRepository.getAgenda(id);
        if (!agendaRepository.isExist(id)) {
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

    /**
     * 1つのアジェンダを削除する。
     * <p>
     * 引数で指定されたidのアジェンダを削除する。
     * @param id 削除するアジェンダ
     * @return true:削除できた場合
     * false:削除できなかった場合
     */
    public boolean deleteAgendaProcess(int id) {
        timerService.stopTimer();
        return agendaRepository.deleteAgenda(id);
    }

    /**
     * 全てのアジェンダを削除する。
     * <p>
     * 全てのアジェンダを削除する。
     */
    public void deleteAgendasProcess() {
        timerService.stopTimer();
        agendaRepository.deleteAgendas();
    }

}
