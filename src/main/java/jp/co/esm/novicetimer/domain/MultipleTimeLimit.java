package jp.co.esm.novicetimer.domain;

import java.util.List;

public class MultipleTimeLimit {
    private int id;
    private List<TimeLimit> timeLimits;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<TimeLimit> getTimeLimits() {
        return timeLimits;
    }
}
