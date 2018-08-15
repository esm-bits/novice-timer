package jp.co.esm.novicetimer.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
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
@Transactional
@Repository
public class AgendaRepository {

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate; // インメモリのDB作成

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

    AgendaRepository() {

    }

    /**
     * アジェンダの登録。
     * <p>
     * 登録したいアジェンダを受け取り、DBに登録します。その際、idをkeyとします。<br>
     * アジェンダのidが自動的に初期化されている場合は、idを自動採番します。
     * @param agenda 登録したいアジェンダ
     * @return 登録されたagenda
     * @throws SQLException
     */
    synchronized public Agenda save(Agenda agenda) {
        if (agenda.getId() == 0) {
            String sql = "INSERT INTO agendas VALUES();";
            SqlParameterSource param = new MapSqlParameterSource();
            jdbcTemplate.update(sql, param);

            String select = "SELECT MAX(id) FROM agendas";
            SqlParameterSource parameter = new MapSqlParameterSource();
            List<Integer> result = jdbcTemplate.query(select, parameter, new RowMapper<Integer>() {
                @Override
                public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new Integer(rs.getInt("MAX(id)"));
                }
            });
            agenda.setId(result.get(0));
        } else if (isExist(agenda.getId())) {
            sr.deleteOneAgenda(agenda.getId());
        } else {
            throw new IllegalArgumentException();
        }

        sr.insertSubjectList(agenda.getId(), agenda.getSubjects());
        return agenda;
    }

    private boolean resetId() {
        String sql = "DELETE FROM agendas";
        SqlParameterSource param = new MapSqlParameterSource();
        return jdbcTemplate.update(sql, param) > 0 ? true : false;
    }

    /**
     * 特定idのアジェンダが存在するか確認。
     * @param id 確認したいアジェンダのid
     * @return true:存在する場合
     * false:存在しない場合
     * @throws SQLException
     */
    public boolean isExist(int id) {
        String sql = "SELECT id FROM agendas WHERE id=:id";
        SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
        List<Integer> result = jdbcTemplate.query(sql, param, new RowMapper<Integer>() {
            @Override
            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Integer(rs.getInt("id"));
            }
        }); // SQL文, パラメータ, 戻り値の型(クラス)
        return result.size() > 0 ? true : false;
    }

    /**
     * 単一のアジェンダの取得。
     * <p>
     * 引数で受け取ったidがMapにあるかを走査して返す。
     * @param id 取得したいアジェンダのid
     * @return idと一致したアジェンダor無かった場合はnull
     * @throws SQLException
     */
    public Agenda getAgenda(int id) {
        return isExist(id) ? new Agenda(id, sr.findSubjectsInAgenda(id)) : null;
    }

    /**
     * 登録されているアジェンダを全て取得する。
     * <p>
     * 登録されているアジェンダを全て取得する。
     * @return List型で全アジェンダを返す
     * @throws SQLException
     */
    public List<Agenda> getAllAgenda() {
        String sql = "SELECT * FROM agendas INNER JOIN subjects ON agendas.id = subjects.agendaId";
        SqlParameterSource param = new MapSqlParameterSource();
        List<AllAgendaEntity> result = jdbcTemplate.query(sql, param, new RowMapper<AllAgendaEntity>() {
            @Override
            public AllAgendaEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new AllAgendaEntity(rs.getInt("agendas.id")
                    , rs.getInt("subjects.id")
                    , rs.getString("subjects.title")
                    , rs.getInt("subjects.minutes")
                    , rs.getString("subjects.idobataUser"));
            }
        });

        List<Integer> agendaId = new ArrayList<Integer>();
        result.stream().filter(agenda -> agendaId.stream().noneMatch(id -> id == agenda.getAgendaId()))
            .forEach(agenda -> agendaId.add(agenda.getAgendaId()));

        List<Subject> subjects = new ArrayList<Subject>();
        List<Agenda> agendas = new ArrayList<Agenda>();
        for (int id : agendaId) {
            subjects.clear();
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
     * false:削除できなかった場合
     */
    public boolean deleteAgenda(int id) {
        String sql = "DELETE FROM agendas WHERE id=:id";
        SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
        return jdbcTemplate.update(sql, param) > 0 && sr.deleteOneAgenda(id);
    }

    /**
     * 全アジェンダを削除する。
     * <p>
     * 全てのアジェンダを削除する。
     */
    public void deleteAllAgenda() {
        if (sr.deleteAllAgendas()) {
            resetId();
        }
    }
}
