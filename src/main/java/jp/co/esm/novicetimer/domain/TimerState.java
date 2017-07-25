package jp.co.esm.novicetimer.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *json形式で受け取ったタイマーのステータスを保持するクラスです。
 */
@Data
@AllArgsConstructor
public class TimerState {
    private TimerStateCode state;
}
