package jp.co.esm.novicetimer.domain;

import java.util.stream.Stream;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * タイマーの状態を示すステータスの定義。<br>
 */
@JsonDeserialize(using = ConstantsDeserializer.TimerStateCodeDeserializer.class)
public enum TimerStateCode {
    START("start"),
    STOP("stop"),
    ;

    final private String state;

    private TimerStateCode(String str) {
        this.state = str;
    }

    public String getState() {
        return state;
    }

    public static TimerStateCode fromValue(String state) {
        return Stream.of(TimerStateCode.values()).filter(code -> code.getState().equals(state)).findFirst().orElse(null);
    }
}
