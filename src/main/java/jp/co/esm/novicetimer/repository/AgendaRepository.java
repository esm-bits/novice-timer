package jp.co.esm.novicetimer.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import jp.co.esm.novicetimer.domain.Agenda;

/**
 * アジェンダを複数保持するクラス。<p>
 * アジェンダを登録してidを割り振ったり
 * idからアジェンダを探すメソッドがある
 */
@Repository
public class AgendaRepository {
    private Map<Integer, Agenda> agendaMap = new ConcurrentHashMap<>();
    private int id;

    AgendaRepository() {
        resetId();
    }

    /**
     * アジェンダの登録。
     * <p>
     * 登録したいアジェンダを受け取り、インスタンスの保持するMapに登録する。その際、idをkeyとします。<br>
     * アジェンダのidが自動的に初期化されている場合は、idを割り振ります。
     * @param agenda 登録したいアジェンダ
     * @return 登録されたagenda
     */
    public Agenda save(Agenda agenda) {
        if (agenda.getId() == 0) {
            agenda.setId(id++);
        }

        agendaMap.put(agenda.getId(), agenda);
        return agenda;
    }

    private void resetId() {
        id = 1;
    }

    /**
     * 特定idのアジェンダが存在するか確認。
     * @param id 確認したいアジェンダのid
     * @return true:存在する場合
     * false:存在しない場合
     */
    public boolean isExist(int id) {
        return agendaMap.get(id) != null;
    }

    /**
     * 単一のアジェンダの取得。
     * <p>
     * 引数で受け取ったidがMapにあるかを走査して返す。
     * @param id 取得したいアジェンダのid
     * @return idと一致したアジェンダor無かった場合はnull
     */
    public Agenda getAgenda(int id) {
        return agendaMap.get(id);
    }

    /**
     * 登録されているアジェンダを全て取得する。
     * <p>
     * 登録されているアジェンダを全て取得する。
     * @return List型で全アジェンダを返す
     */
    public List<Agenda> getAgendas() {
        return new ArrayList<>(agendaMap.values());
    }

    /**
     * 1つのアジェンダを削除する
     * @param id 削除するアジェンダのid
     * @return true:削除できた場合
     * false:削除できなかった場合
     */
    public boolean deleteAgenda(int id) {
        return agendaMap.remove(id) != null;
    }

    /**
     * 全アジェンダを削除する。
     * <p>
     * 全てのアジェンダを削除する。
     */
    public void deleteAgendas() {
        agendaMap.clear();
        resetId();
    }
}
