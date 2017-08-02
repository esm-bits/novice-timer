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
    private int id = 1;

    /**
     * アジェンダの登録。
     * <p>
     * 登録したいアジェンダを受け取り、インスタンスの保持するMapに登録する。その際、割り振ったidをkeyとする。
     * @param agenda 登録したいアジェンダ
     * @return 割り振られたidを持つagenda
     */
    public Agenda save(Agenda agenda) {
        agenda.setId(id);
        id++;

        agendaMap.put(agenda.getId(), agenda);
        return agenda;
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

    public List<Agenda> getAgendas() {
        return new ArrayList<>(agendaMap.values());
    }
}
