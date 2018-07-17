package jp.co.esm.novicetimer.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * アジェンダの登録や、保持に使われるクラス。<br>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Agenda {
    private int id;
    private List<Subject> subjects;
}
