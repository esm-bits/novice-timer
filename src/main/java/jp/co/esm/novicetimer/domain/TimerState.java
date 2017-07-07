package jp.co.esm.novicetimer.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TimerState {
    private TimerStateCode state;
}
