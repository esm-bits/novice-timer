package jp.co.esm.novicetimer.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class Subject extends TimeLimit {
    private String title;

    public Subject (String title, int minutes, String user) {
        super(minutes, user);
        this.title = title;
    }
}
