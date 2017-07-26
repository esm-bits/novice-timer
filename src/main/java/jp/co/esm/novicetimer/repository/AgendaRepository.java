package jp.co.esm.novicetimer.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import jp.co.esm.novicetimer.domain.Agenda;

@Repository
public class AgendaRepository {
    private Map<Integer, Agenda> agendaMap = new ConcurrentHashMap<>();
    private int id = 1;

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
            agenda.setId(id);
            id++;
        }

        agendaMap.put(agenda.getId(), agenda);
        return agenda;
    }

    public Agenda getAgenda(int id) {
        return agendaMap.get(id);
    }

    public List<Agenda> getAgendas() {
        return new ArrayList<>(agendaMap.values());
    }
}
