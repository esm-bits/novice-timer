package jp.co.esm.novicetimer.domain;

import java.util.List;

import lombok.Data;
/**
 * アジェンダの登録や、保持に使われるクラス。<br>
 * フィールドに{@literal id, List<Subject>}を持つ。{@link Subject}
 */
@Data
public class Agenda {
    private int id;
    private List<Subject> subjects;
}
