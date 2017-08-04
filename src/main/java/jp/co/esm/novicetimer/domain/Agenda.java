package jp.co.esm.novicetimer.domain;

import java.util.List;

import lombok.Data;

/**
 * アジェンダの登録や、保持に使われるクラス。<br>
 */
@Data
public class Agenda {
    private int id;
    private List<Subject> subjects;
}
