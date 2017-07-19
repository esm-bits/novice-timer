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

    public Agenda save(Agenda agenda) {
        agenda.setId(id);
        id++;

        agendaMap.put(agenda.getId(), agenda);
        return agenda;
    }

    private void resetId() {
        id = 1;
    }

    public Agenda getAgenda(int id) {
        return agendaMap.get(id);
    }

    public List<Agenda> getAgendas() {
        return new ArrayList<>(agendaMap.values());
    }

    public boolean deleteAgenda(int id) {
        if(agendaMap.remove(id) == null) {
            return false;
        }
        return true;
    }

    public void deleteAgendas() {
        resetId();
        agendaMap.clear();
    }
}
