package jp.co.esm.novicetimer.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class Subject extends TimeLimit {
    private String title;
}
