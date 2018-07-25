package jp.co.esm.novicetimer.domain;

import java.util.List;
import java.util.StringJoiner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * アジェンダの登録や、保持に使われるクラス。<br>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Agenda {
    private int id;
    private List<Subject> subjects;
    
    /**
     * サブジェクトの各タイトルを「」で繋げた文字列を作成して返す。
     * @return サブジェクトタイトル
     */
    public String concatSubjectTitles() {
        
        StringJoiner titleJoiner = new StringJoiner("」「", "「", "」"); 
        subjects.stream().forEach(subject -> titleJoiner.add(subject.getTitle()));
        
        return titleJoiner.toString();
    }
}
