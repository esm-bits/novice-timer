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
    private int pointer;

    /**
     * 現在のpointerがあるSubjectを返します。
     * @return
     */
    public Subject getSubject() {
        Subject subject = subjects.get(pointer);
        return subject;
    }

    /**
     * 指定された値にpointerをセットします。
     * pointerの操作はこのメソッドだけにしたい。
     *
     * @param number
     * @throws IndexOutOfBoundsException Subjectsの範囲を超えている場合に投げられます。
     */
    public synchronized void setPointer(int number) {
        if (subjects != null && (number >= subjects.size() || number < 0)) {
            throw new IndexOutOfBoundsException();
        }
        pointer = number;
    }

    /**
     * pointerをインクリメントします。
     * Subjectsのサイズを超えた場合、pointerにゼロをセットします。
     */
    public void incrementPointer() {
        try {
            setPointer(pointer + 1);
        } catch (IndexOutOfBoundsException e) {
            setPointer(0);
        }
    }
}
