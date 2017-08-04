package jp.co.esm.novicetimer.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * タイマーのステータスを保持するクラス。
 */
@Data
@AllArgsConstructor
public class TimerState {
    private TimerStateCode state;
}
