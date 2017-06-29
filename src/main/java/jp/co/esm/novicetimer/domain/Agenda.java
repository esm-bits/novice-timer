package jp.co.esm.novicetimer.domain;

import java.util.List;

public class Agenda {
    private int id;
    private List<Subject> subjects;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }
}
