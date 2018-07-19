package jp.co.esm.novicetimer.repository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jp.co.esm.novicetimer.domain.Agenda;

/**
 * アジェンダを複数保持するクラス。<p>
 * アジェンダを登録してidを割り振ったり
 * idからアジェンダを探すメソッドがある
 */
@Repository
public class AgendaRepository {
    private int id;
    @Autowired
    private SubjectRepository sr;

    AgendaRepository() {
        resetId();
    }

    /**
     * アジェンダの登録。
     * <p>
     * 登録したいアジェンダを受け取り、インスタンスの保持するMapに登録します。その際、idをkeyとします。<br>
     * アジェンダのidが自動的に初期化されている場合は、idを割り振ります。
     * @param agenda 登録したいアジェンダ
     * @return 登録されたagenda
     * @throws SQLException
     */
    public Agenda save(Agenda agenda) throws SQLException {
        if (agenda.getId() == 0) {
            agenda.setId(id++);
        }
        else if(sr.findSubjectsInAgenda(agenda.getId()).size() != 0) {
            sr.deleteOneAgenda(agenda.getId());
        }

        sr.insertSubjectList(agenda.getId(), agenda.getSubjects());
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
     * @throws SQLException
     */
    public boolean isExist(int id) throws SQLException {
        return sr.findSubjectsInAgenda(id).size() > 0 ? true : false;
    }

    /**
     * 単一のアジェンダの取得。
     * <p>
     * 引数で受け取ったidがMapにあるかを走査して返す。
     * @param id 取得したいアジェンダのid
     * @return idと一致したアジェンダor無かった場合はnull
     * @throws SQLException
     */
    public Agenda getAgenda(int id) throws SQLException {
        return new Agenda(id, sr.findSubjectsInAgenda(id));
    }

    /**
     * 登録されているアジェンダを全て取得する。
     * <p>
     * 登録されているアジェンダを全て取得する。
     * @return List型で全アジェンダを返す
     * @throws SQLException
     */
    public List<Agenda> getAgendas() throws SQLException {
        List<Agenda> allAgendas = new ArrayList<Agenda>();
        for (int i = 0; i < id; i++) {
            allAgendas.add(new Agenda(i, sr.findSubjectsInAgenda(i)));
        }
        return allAgendas;
    }

    /**
     * 1つのアジェンダを削除する
     * @param id 削除するアジェンダのid
     * @return true:削除できた場合
     * false:削除できなかった場合
     */
    public boolean deleteAgenda(int id) {
        return sr.deleteOneAgenda(id);
    }

    /**
     * 全アジェンダを削除する。
     * <p>
     * 全てのアジェンダを削除する。
     */
    public void deleteAgendas() {
        if (sr.deleteAllAgendas()) {
            resetId();
        }
    }
}
