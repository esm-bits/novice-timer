package jp.co.esm.novicetimer.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jp.co.esm.novicetimer.domain.Agenda;
import jp.co.esm.novicetimer.domain.Subject;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * アジェンダを複数保持するクラス。<p>
 * アジェンダを登録してidを割り振ったり
 * idからアジェンダを探すメソッドがある
 */
@Repository
@Transactional
public class AgendaRepository {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate; // インメモリのDB作成

    @Autowired
    private SubjectRepository sr;

    @Data
    @AllArgsConstructor
    static class AllAgendaEntity {
        private int agendaId;
        private int subjectId;
        private String title;
        private int minutes;
        private String idobataUser;
    }

    /**
     * アジェンダの登録または更新を行う。
     * <p>
     * 登録したいアジェンダを受け取り、DBに登録します。その際、idをkeyとします。<br>
     * アジェンダのidが0の場合は、idを自動採番します。<br>
     * アジェンダのidに0以外が格納されている場合は、そのidと同一のidを持つアジェンダを更新します。
     * @param agenda 登録したいアジェンダ
     * @return 登録されたagenda
     */
    public Agenda save(Agenda agenda) {
        if (agenda.getId() == 0) {
            // アジェンダを新規登録する場合
            
            // アジェンダテーブルにアジェンダを登録する
            String sql = "INSERT INTO agendas DEFAULT VALUES;";
            SqlParameterSource param = new MapSqlParameterSource();
            KeyHolder keyHolder = new GeneratedKeyHolder(); // 自動採番されたIDを取得するためのKeyHolder
            jdbcTemplate.update(sql, param, keyHolder);

            // 自動採番されたIDをセットする
            agenda.setId(keyHolder.getKey().intValue());

            // サブジェクトテーブルにサブジェクトを登録する
            sr.insertSubjectList(agenda.getId(), agenda.getSubjects());
        } else if (isExist(agenda.getId())) {
            // アジェンダを更新する場合
            
            // 対象アジェンダに紐づくサブジェクトをすべて削除する
            sr.deleteOneAgenda(agenda.getId());
            
            // サブジェクトテーブルにサブジェクトを登録する
            sr.insertSubjectList(agenda.getId(), agenda.getSubjects());
        } else {
            throw new IllegalArgumentException();
        }
        
        return agenda;
    }

    /**
     * 特定idのアジェンダが存在するか確認。
     * @param id 確認したいアジェンダのid
     * @return true:存在する場合
     *         false:存在しない場合
     */
    public boolean isExist(int id) {
        String sql = "SELECT agendas_id FROM agendas WHERE agendas_id=:id";
        SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
        List<Integer> result = jdbcTemplate.query(sql, param, new RowMapper<Integer>() {
            @Override
            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Integer(rs.getInt("agendas_id"));
            }
        }); // SQL文, パラメータ, 戻り値の型(クラス)
        return result.size() > 0;
    }

    /**
     * 単一のアジェンダの取得。
     * <p>
     * 引数で受け取ったidがMapにあるかを走査して返す。
     * @param id 取得したいアジェンダのid
     * @return idと一致したアジェンダor無かった場合はnull
     */
    public Agenda getAgenda(int id) {
        return isExist(id) ? new Agenda(id, sr.findSubjectsInAgenda(id)) : null;
    }

    /**
     * 登録されているアジェンダを全て取得する。
     * <p>
     * 登録されているアジェンダを全て取得する。
     * @return List型で全アジェンダを返す
     */
    public List<Agenda> getAllAgenda() {
        String sql = "SELECT * FROM agendas INNER JOIN subjects ON agendas.agendas_id = subjects.agenda_id";
        SqlParameterSource param = new MapSqlParameterSource();
        List<AllAgendaEntity> result = jdbcTemplate.query(sql, param, new RowMapper<AllAgendaEntity>() {
            @Override
            public AllAgendaEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new AllAgendaEntity(rs.getInt("agendas_id")
                    , rs.getInt("subjects_id")
                    , rs.getString("title")
                    , rs.getInt("minutes")
                    , rs.getString("idobata_user"));
            }
        });

        List<Integer> agendaId = result.stream().map(AllAgendaEntity::getAgendaId).distinct().sorted().collect(Collectors.toList());

        List<Agenda> agendas = new ArrayList<>();
        for (int id : agendaId) {
            List<Subject> subjects = new ArrayList<>();
            result.stream()
                .filter(agenda -> agenda.getAgendaId() == id)
                .forEach(agenda -> subjects.add(
                    new Subject(agenda.getTitle(), agenda.getMinutes(), agenda.getIdobataUser())));
            agendas.add(new Agenda(id, subjects));
        }
        return agendas;
    }

    /**
     * 1つのアジェンダを削除する
     * @param id 削除するアジェンダのid
     * @return true:削除できた場合
     *         false:削除できなかった場合
     */
    public boolean deleteAgenda(int id) {
        String sql = "DELETE FROM agendas WHERE agendas_id=:id";
        SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
        return jdbcTemplate.update(sql, param) > 0 && sr.deleteOneAgenda(id);
    }

    /**
     * 全アジェンダを削除する。
     * <p>
     * 全てのアジェンダを削除する。
     */
    public void deleteAllAgenda() {
        if (sr.deleteAllSubjects()) {
            String sql = "DELETE FROM agendas";
            SqlParameterSource param = new MapSqlParameterSource();
            jdbcTemplate.update(sql, param);
        }
    }
}
